package nats

import (
	"github.com/nats-io/nats.go"
	protocol "go-protocol"
	"go-protocol/serializer"
	"google.golang.org/protobuf/proto"
	"reflect"
	"sync"
)

var natsClient = struct {
	conn        *nats.Conn
	serializer  *serializer.ProtoSerializer
	dispatchers []*nats.Subscription
	sync.Mutex
}{}

func newProtoMessage[T proto.Message]() T {
	var t T
	typ := reflect.TypeOf(t)
	if typ.Kind() == reflect.Ptr {
		typ = typ.Elem()
	}
	return reflect.New(typ).Interface().(T)
}

func Connection() *nats.Conn {
	return natsClient.conn
}

func Connect(serverURL string) error {
	conn, err := nats.Connect(serverURL)
	if err != nil {
		return err
	}
	natsClient.conn = conn
	natsClient.serializer = serializer.NewProtoSerializer()
	natsClient.dispatchers = []*nats.Subscription{}

	_, err = Subscribe[*protocol.HandshakeRequest]("test", func(replyTo string, request *protocol.HandshakeRequest) {
		response := &protocol.HandshakeResponse{Message: "HEYY HEYYY HAHAA"}
		data, err := natsClient.serializer.Serialize(response)
		if err != nil {
			return
		}
		err = natsClient.conn.Publish(replyTo, data)
		if err != nil {
			return
		}
	})
	if err != nil {
		return err
	}

	return nil
}

func Publish[T proto.Message](subject string, message T) {
	data, err := natsClient.serializer.Serialize(message)
	if err != nil {
		return
	}
	err = natsClient.conn.Publish(subject, data)
	if err != nil {
		return
	}
}

func Subscribe[T proto.Message](subject string, callback func(replyTo string, message T)) (*nats.Subscription, error) {
	sub, err := natsClient.conn.Subscribe(subject, func(msg *nats.Msg) {
		t := newProtoMessage[T]()
		err := natsClient.serializer.Deserialize(msg.Data, t)
		if err != nil {
			return
		}
		callback(msg.Reply, t)
	})

	if err != nil {
		return nil, err
	}

	natsClient.Lock()
	natsClient.dispatchers = append(natsClient.dispatchers, sub)
	natsClient.Unlock()
	return sub, nil
}

func Request[Req proto.Message, Res proto.Message](subject string, request Req) (*Res, error) {
	data, err := natsClient.serializer.Serialize(request)
	if err != nil {
		return nil, err
	}

	msg, err := natsClient.conn.Request(subject, data, nats.DefaultTimeout)
	if err != nil {
		return nil, err
	}

	var response Res
	err = natsClient.serializer.Deserialize(msg.Data, response)
	if err != nil {
		return nil, err
	}

	return &response, nil
}

func Close() {
	natsClient.conn.Close()
}

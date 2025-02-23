package serializer

import (
	protocol "go-protocol"
	"google.golang.org/protobuf/proto"
)

type ProtoSerializer struct{}

func NewProtoSerializer() *ProtoSerializer {
	return &ProtoSerializer{}
}

func (ps *ProtoSerializer) Serialize(message proto.Message) ([]byte, error) {
	serializedData, err := proto.Marshal(message)
	if err != nil {
		return nil, err
	}

	packet := &protocol.Packet{
		Type: string(message.ProtoReflect().Descriptor().Name()),
		Data: serializedData,
	}

	return proto.Marshal(packet)
}

func (ps *ProtoSerializer) Deserialize(data []byte, message proto.Message) error {
	var packet protocol.Packet
	if err := proto.Unmarshal(data, &packet); err != nil {
		return err
	}

	return proto.Unmarshal(packet.GetData(), message)
}

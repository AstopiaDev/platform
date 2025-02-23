package service

import (
	"log"

	"github.com/golang/protobuf/proto"
	"github.com/nats-io/nats.go"
	protocol "go-protocol"
	"go-protocol/registry"
)

type CustomMessageHandler[T proto.Message] interface {
	OnMsg(msg *nats.Msg, parsedMsg T)
}

type GenericMessageHandler[T proto.Message] struct {
	Handler func(msg *nats.Msg, parsedMsg T)
}

func (g *GenericMessageHandler[T]) HandleMessage(msg *nats.Msg) {
	var packet protocol.Packet
	if err := proto.Unmarshal(msg.Data, &packet); err != nil {
		log.Printf("Failed to parse Packet: %v", err)
		return
	}

	parsedMsg, err := registry.ParseMessage(packet.Type, packet.Data)
	if err != nil {
		log.Printf("Failed to parse message: %v", err)
		return
	}

	msgTyped, ok := parsedMsg.(T)
	if !ok {
		log.Printf("Failed to cast message to expected type")
		return
	}

	g.Handler(msg, msgTyped)
}

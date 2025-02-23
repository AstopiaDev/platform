package registry

import (
	"fmt"
	protocol "go-protocol"
	"google.golang.org/protobuf/proto"
)

var MessageRegistry = map[string]proto.Message{
	"RequestEnvelope":   &protocol.RequestEnvelope{},
	"ResponseEnvelope":  &protocol.ResponseEnvelope{},
	"HandshakeRequest":  &protocol.HandshakeRequest{},
	"HandshakeResponse": &protocol.HandshakeResponse{},
}

func ParseMessage(typeName string, data []byte) (proto.Message, error) {
	msg, exists := MessageRegistry[typeName]
	if !exists {
		return nil, fmt.Errorf("Unknown packet type %s", typeName)
	}

	cloned := proto.Clone(msg)
	if err := proto.Unmarshal(data, cloned); err != nil {
		return nil, err
	}
	return cloned, nil
}

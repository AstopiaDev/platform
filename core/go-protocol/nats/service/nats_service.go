package service

import (
	"github.com/nats-io/nats.go"
	"github.com/nats-io/nats.go/micro"
	conn "go-protocol/nats"
	"log"
	"os"
)

type NatsService struct {
	name    string
	desc    string
	conn    *nats.Conn
	service micro.Service
	group   micro.Group
}

func NewNatsService(name, desc string) *NatsService {
	natsURL := os.Getenv("NATS_SERVER_URL")
	if natsURL == "" {
		natsURL = "nats://127.0.0.1:4222"
	}

	err := conn.Connect(natsURL)
	if err != nil {
		log.Fatalf("Failed to connect to NATS: %v", err)
		return nil
	}

	service, err := micro.AddService(conn.Connection(), micro.Config{
		Name:        name,
		Version:     "1.0.0",
		Description: desc,
	})

	if err != nil {
		log.Fatalf("Failed to create service: %v", err)
	}

	ns := &NatsService{
		name:    name,
		desc:    desc,
		conn:    conn.Connection(),
		service: service,
		group:   service.AddGroup(name),
	}

	for _, endpoint := range ns.GetEndpoints() {
		err := service.AddEndpoint(endpoint.Name, endpoint.Handler)
		if err != nil {
			return nil
		}
	}

	// Start the service
	//go service.Start

	return ns
}

// GetEndpoints should be implemented in child structs
func (ns *NatsService) GetEndpoints() []micro.Endpoint {
	return []micro.Endpoint{}
}

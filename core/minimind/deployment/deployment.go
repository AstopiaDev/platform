package main

import (
	"fmt"
	"log"

	nomad "github.com/hashicorp/nomad/api"
)

func strPtr(s string) *string {
	return &s
}

func intPtr(i int) *int {
	return &i
}

func deployServer(jobName, dockerImage string, count int) error {
	// Create a Nomad client
	client, err := nomad.NewClient(nomad.DefaultConfig())
	if err != nil {
		return err
	}

	job := &nomad.Job{
		ID:          &jobName,
		Name:        &jobName,
		Datacenters: []string{"dc1"},
		Type:        strPtr("service"),
		TaskGroups: []*nomad.TaskGroup{
			{
				Name:  &jobName,
				Count: intPtr(count),

				Tasks: []*nomad.Task{
					{
						Name:   "server",
						Driver: "docker",
						Config: map[string]interface{}{
							"image": dockerImage,
							"ports": []string{"server"},
						},
						Resources: &nomad.Resources{
							CPU:      intPtr(1000),
							MemoryMB: intPtr(512),
						},
					},
				},
			},
		},
	}

	_, _, err = client.Jobs().Register(job, nil)
	if err != nil {
		return err
	}

	fmt.Printf("Successfully deployed job: %s\n", jobName)
	return nil
}

func main() {
	err := deployServer("lobby-server-1", "mygame/lobby:latest", 1)
	if err != nil {
		log.Fatalf("Failed to deploy: %v", err)
	}
}

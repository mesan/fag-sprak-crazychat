package main

import (
	"fmt"
	"os"

	"github.com/mesan/fag-sprak-crazychat/crazychat"
)

func main() {
	// First arg is executed command
	if len(os.Args) != 4 {
		fmt.Println("Usage: crazychat <returnAddress> <port> <username>")
		return
	}

	//crazychat.Run(os.Args[1], os.Args[2], os.Args[3])
	crazychat.New(os.Args[1], os.Args[2], os.Args[3]).Run()
}

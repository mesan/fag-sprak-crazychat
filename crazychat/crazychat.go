package crazychat

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type App struct {
	returnAddress string
	port          string
	username      string
	otherUsers    map[string]string
}

func New(returnAddress, port, username string) *App {
	return &App{
		returnAddress,
		port,
		username,
		make(map[string]string),
	}
}

func (app *App) Run() {
	printWelcome()
	printCommands()
	ListenForMessages(app.port, app.handleIncomingMessage)
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		app.handleInput(scanner.Text())
	}
}

func (app *App) handleInput(input string) {
	if !strings.HasPrefix(input, ":") {
		msg := Message{
			input,
			app.username,
			app.returnAddress,
		}
		app.handleOutgoingMessage(msg)
		return
	}
	fields := strings.Fields(input)
	if fields[0] == ":add" && len(fields) == 3 {
		address := fields[1]
		name := fields[2]
		app.otherUsers[address] = name
		fmt.Printf("Added %s (%s)\n", name, address)
		return
	}
	if fields[0] == ":list" && len(fields) == 1 {
		for address, name := range app.otherUsers {
			fmt.Printf("  %s %s\n", address, name)
		}
		return
	}
	printCommands()
}

func (app *App) handleIncomingMessage(msg Message) {
	app.otherUsers[msg.ReturnAddress] = msg.Username
	fmt.Printf("%s (%s) << %s\n", msg.Username, msg.ReturnAddress, msg.Message)
}

func (app *App) handleOutgoingMessage(msg Message) {
	fmt.Printf("%s (%s) >> %s\n", app.username, app.returnAddress, msg.Message)
	for address := range app.otherUsers {
		go SendMessage(msg, address)
	}
}

func printWelcome() {
	fmt.Println("CrazyChat")
	fmt.Println("  Type to send messages")
	fmt.Println("  Exit with Ctrl-C or Ctrl-D")
}

func printCommands() {
	fmt.Println("Available commands")
	fmt.Println("  :add <address> <username>")
	fmt.Println("  :list")
}

package crazychat

import (
	"bufio"
	"fmt"
	"os"
	"strings"

	"github.com/mesan/fag-sprak-crazychat/crazychat/rx"
)

type App struct {
	returnAddress  string
	port           string
	username       string
	contacts       map[string]string
	incomingChan   chan rx.Message
	incomingStream *rx.MessageStream
	outgoingChan   chan rx.Message
	outgoingStream *rx.MessageStream
}

func New(returnAddress, port, username string) *App {
	contacts := make(map[string]string)
	incomingChan := make(chan rx.Message)
	incomingStream := rx.FromMessageChannel(incomingChan).Fork()
	outgoingChan := make(chan rx.Message)
	outgoingStream := rx.FromMessageChannel(outgoingChan).Fork()

	return &App{
		returnAddress,
		port,
		username,
		contacts,
		incomingChan,
		incomingStream,
		outgoingChan,
		outgoingStream,
	}
}

func (app *App) Run() {
	printWelcome()
	printCommands()

	ListenForMessages(app.port, app.incomingChan)

	// Incoming messages
	app.incomingStream.SubscribeNext(func(msg rx.Message) {
		app.printIncomingMessage(msg)
	})
	app.incomingStream.SubscribeNext(func(msg rx.Message) {
		app.addContact(msg.Username, msg.ReturnAddress)
	})

	// Outgoing messages
	app.outgoingStream.SubscribeNext(func(msg rx.Message) {
		app.printOutgoingMessage(msg)
	})

	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		app.handleInput(scanner.Text())
	}
}

func (app *App) handleInput(input string) {
	if !strings.HasPrefix(input, ":") {
		msg := rx.Message{
			Message:       input,
			Username:      app.username,
			ReturnAddress: app.returnAddress,
		}
		app.outgoingChan <- msg
		return
	}
	fields := strings.Fields(input)
	if fields[0] == ":add" && len(fields) == 3 {
		address := fields[1]
		name := fields[2]
		app.addContact(name, address)
		return
	}
	if fields[0] == ":list" && len(fields) == 1 {
		for address, name := range app.contacts {
			fmt.Printf("  %s %s\n", address, name)
		}
		return
	}
	printCommands()
}

func (app *App) addContact(name string, address string) {
	oldname, exists := app.contacts[address]
	app.contacts[address] = name
	if !exists {
		// Outgoing messages
		app.outgoingStream.SubscribeNext(func(msg rx.Message) {
			SendMessage(msg, address)
		})
		fmt.Printf("Added %s %s\n", address, name)
	} else if oldname != name {
		fmt.Printf("Renamed %s %s\n", address, name)
	}
}

func (app *App) printIncomingMessage(msg rx.Message) {
	fmt.Printf(ColGreen+"%s %s << %s\n"+ColReset, msg.ReturnAddress, msg.Username, msg.Message)
}

func (app *App) printOutgoingMessage(msg rx.Message) {
	fmt.Printf(ColCyan+"%s %s >> %s\n"+ColReset, app.returnAddress, app.username, msg.Message)
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

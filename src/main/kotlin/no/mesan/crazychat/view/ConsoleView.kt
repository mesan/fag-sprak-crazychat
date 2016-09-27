package no.mesan.crazychat.view

import no.mesan.crazychat.ChatBroker
import no.mesan.crazychat.domain.MessageEvent

class ConsoleView constructor(val chatBroker: ChatBroker) {

    val messages = chatBroker.incomingMessages
            .mergeWith(chatBroker.outgoingMessages)

    val userName: String by lazy { readLine() ?: "" }
    val returnAddress: String by lazy { readLine() ?: "" }

    init {
        Thread {
            Thread.sleep(1000)
            println("Welcome to CrazyChat!")
            println("---------------------")
            print("Enter username: ")
            userName // This initializes userName.
            print("Enter location: ")
            returnAddress
            messages.subscribe(this::printMessage)
            chatLoop()
        }.start()
    }

    fun printMessage(messageEvent: MessageEvent) {
        print("${messageEvent.username}: ${messageEvent.message}")
    }

    fun chatLoop() {

        while(true) {
            print("$ ")
            val message = readLine() ?: ""
            print("\b\r")
            chatBroker.sendMessage(MessageEvent(userName, returnAddress, message))
        }
    }

}
package no.mariuss.rxKotlin.view

import no.mariuss.rxKotlin.domain.MessageEvent
import no.mariuss.rxKotlin.service.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConsoleView @Autowired constructor(chatService: ChatService) {

    val messages = chatService.messages

    init {
        println("Welcome to CrazyChat!")
        println("---------------------")
        messages.subscribe(this::printMessage)
    }

    fun printMessage(messageEvent: MessageEvent) {
        println("${messageEvent.nickName}:\n\t${messageEvent.message}")
    }

}
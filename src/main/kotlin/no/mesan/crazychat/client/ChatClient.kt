package no.mesan.crazychat.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rx_response
import no.mesan.crazychat.ChatBroker
import no.mesan.crazychat.ConnectionManager
import no.mesan.crazychat.domain.MessageEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

@Component
class ChatClient @Autowired constructor(val connectionManager: ConnectionManager, val objectMapper: ObjectMapper, chatBroker: ChatBroker) {

    val outMessages = chatBroker.outgoingMessages

    init {
        outMessages.subscribe(this::sendMessage)
    }

    fun sendMessage(event: MessageEvent) {
        Observable.from(connectionManager.connections)
                .observeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .flatMap { connection ->
                    connection.returnAddress.httpPost()
                            .header("Content-Type" to "application/json")
                            .body(objectMapper.writeValueAsString(event))
                            .rx_response()
                            .map { connection.returnAddress to it.first }
                }
                .subscribe({
                    val (address, response) = it
                    println("$address: ${response.httpStatusCode} ${response.httpResponseMessage}")
                }, {
                    println("Could not connect: $event")
                    it.printStackTrace(System.err)
                })
    }

}
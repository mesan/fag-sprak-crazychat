package no.mesan.crazychat

import no.mesan.crazychat.view.ChatView
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class CrazyChat {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplicationBuilder(CrazyChat::class.java)
                    .properties(mapOf(Pair("server.port", 1337)))
                    .headless(false)
                    .build()
                    .run(*args)
        }
    }

    @Bean
    open fun chatView(chatBroker: ChatBroker, connectionManager: ConnectionManager): ChatView {
        return ChatView(chatBroker, connectionManager)
    }

}
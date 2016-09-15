package no.mariuss.rxKotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
open class CrazyChat {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplicationBuilder(CrazyChat::class.java)
                    .properties(mapOf(Pair("server.port", 8080)))
                    .build()
                    .run(*args)
        }
    }

}
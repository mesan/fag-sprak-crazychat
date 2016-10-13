package no.mesan.crazychat

import no.mesan.crazychat.domain.Connection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rx.Observable
import rx.lang.kotlin.filterNotNull

@Service
class ConnectionManager @Autowired constructor(chatBroker: ChatBroker) {

    private val incomingConnections: Observable<Connection> =
            chatBroker.incomingMessages
                    .map { Connection(it.username, it.returnAddress) }
                    .distinct()

    private val outgoingConnections: Observable<Connection> =
            chatBroker.outgoingMessages
                    .map { it.toAddress }
                    .filterNotNull()
                    .distinct()
                    .map { Connection(null, it) }


    private val conns = mutableListOf<Connection>()
    val connections: List<Connection> = conns

    init {
        incomingConnections.subscribe { conn ->
            val existing = conns.find { it.returnAddress == conn.returnAddress }
            if(existing != null) {
                conns.remove(existing)
            }

            conns.add(conn)
        }


        outgoingConnections.subscribe { conn ->
            conns.find { it.returnAddress == conn.returnAddress } ?: conns.add(conn)
        }
    }

}
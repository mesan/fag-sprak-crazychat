package no.mesan.crazychat

import no.mesan.crazychat.domain.Connection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rx.Observable

@Service
class ConnectionManager @Autowired constructor(chatBroker: ChatBroker) {

    private val incomingConnections: Observable<Connection> =
            chatBroker.incomingMessages
                    .map { Connection(it.username, it.returnAddress) }
                    .distinct { it.returnAddress }

    private val conns = mutableListOf<Connection>()
    val connections: List<Connection> = conns

    init {
        incomingConnections.subscribe { conns.add(it) }
    }

}
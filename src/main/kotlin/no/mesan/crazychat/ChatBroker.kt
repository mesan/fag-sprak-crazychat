package no.mesan.crazychat

import no.mesan.crazychat.domain.MessageEvent
import org.springframework.stereotype.Service
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

@Service
class ChatBroker {

    private val inSubject = PublishSubject<MessageEvent>()
    private val outSubject = PublishSubject<MessageEvent>()

    private val scheduler = Schedulers.from(Executors.newCachedThreadPool())

    val incomingMessages: Observable<MessageEvent> = inSubject.share().observeOn(scheduler)
    val outgoingMessages: Observable<MessageEvent> = outSubject.share().observeOn(scheduler)

    fun receiveMessage(event: MessageEvent) {
        inSubject.onNext(event)
    }

    fun sendMessage(event: MessageEvent) {
        outSubject.onNext(event)
    }

}
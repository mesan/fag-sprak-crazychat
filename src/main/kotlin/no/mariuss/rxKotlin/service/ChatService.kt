package no.mariuss.rxKotlin.service

import no.mariuss.rxKotlin.domain.MessageEvent
import org.springframework.stereotype.Service
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

@Service
class ChatService {

    private val subject = PublishSubject<MessageEvent>()

    val messages: Observable<MessageEvent> = subject.observeOn(Schedulers.from(Executors.newCachedThreadPool()))

    fun receiveMessage(event: MessageEvent) {
        subject.onNext(event)
    }

}
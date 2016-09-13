package no.mariuss.rxKotlin.domain

import org.springframework.stereotype.Service
import rx.Observable
import rx.lang.kotlin.toObservable
import sun.reflect.generics.reflectiveObjects.NotImplementedException

@Service
class ChatService {

    fun receiveMessage(nickName: String, basePath: String, message: String): Observable<Boolean> {
        return NotImplementedException().toObservable()
    }

}
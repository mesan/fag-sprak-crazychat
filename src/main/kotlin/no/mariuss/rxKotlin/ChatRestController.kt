package no.mariuss.rxKotlin

import no.mariuss.rxKotlin.domain.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult


@CrossOrigin(origins = arrayOf("*"))
@RestController
class ChatRestController @Autowired constructor(val chatService: ChatService) {

    @RequestMapping(
            consumes = arrayOf("application/json"),
            method = arrayOf(RequestMethod.POST),
            value = "/{nickName}/{basePath}")
    fun receiveMessage(
            @PathVariable nickName: String,
            @PathVariable basePath: String,
            @RequestBody message: String): DeferredResult<Boolean> {

        val result: DeferredResult<Boolean> = DeferredResult(180000)

        chatService.receiveMessage(nickName, basePath, message)
                .doOnError { result.setErrorResult(it) }
                .doOnCompleted { result.setResult(false) }
                .subscribe { result.setResult(it) }

        return result

    }

}
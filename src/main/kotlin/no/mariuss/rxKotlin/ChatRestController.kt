package no.mariuss.rxKotlin

import no.mariuss.rxKotlin.domain.MessageEvent
import no.mariuss.rxKotlin.service.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


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
            @RequestBody message: String): ResponseEntity<Unit> {

        chatService.receiveMessage(MessageEvent(nickName, basePath, message))
        return ResponseEntity<Unit>(HttpStatus.CREATED)

    }

}
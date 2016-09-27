package no.mesan.crazychat

import no.mesan.crazychat.domain.MessageEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = arrayOf("*"))
@RestController
class ChatRestController @Autowired constructor(val chatService: ChatBroker) {

    @RequestMapping(
            consumes = arrayOf("application/json"),
            method = arrayOf(RequestMethod.POST))
    fun receiveMessage(
            @RequestBody event: MessageEvent): ResponseEntity<Unit> {

        chatService.receiveMessage(event)
        return ResponseEntity<Unit>(HttpStatus.CREATED)

    }

}
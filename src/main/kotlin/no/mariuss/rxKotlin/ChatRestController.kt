package no.mariuss.rxKotlin

import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = arrayOf("*"))
@RestController
class ChatRestController {

    @RequestMapping(
            consumes = arrayOf("application/json"),
            method = arrayOf(RequestMethod.POST),
            value = "/{nickName}/{basePath}")
    fun receiveMessage(
            @PathVariable nickName: String,
            @PathVariable basePath: String,
            @RequestBody message: String) {



    }

}
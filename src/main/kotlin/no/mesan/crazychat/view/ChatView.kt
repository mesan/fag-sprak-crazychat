package no.mesan.crazychat.view

import no.mesan.crazychat.ChatBroker
import no.mesan.crazychat.ConnectionManager
import no.mesan.crazychat.domain.MessageEvent
import rx.Observable
import rx.swing.sources.KeyEventSource
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.swing.*

class ChatView(val chatBroker: ChatBroker, val connectionManager: ConnectionManager) : JFrame("CrazyChat") {

    val messages: Observable<MessageEvent> =
            chatBroker.incomingMessages
                    .mergeWith(chatBroker.outgoingMessages)

    val chat = JTextArea().apply {
        minimumSize = Dimension(450, 450)
        isEditable = false
    }

    val chatLine = JTextField().apply {
        maximumSize = Dimension(450, 50)
        isEditable = true
    }

    val username: String = JOptionPane.showInputDialog(null, "Username: ", "Log in", JOptionPane.QUESTION_MESSAGE)
    val basePath: String = JOptionPane.showInputDialog(null, "Base path: ", "Location", JOptionPane.QUESTION_MESSAGE)

    init {
        messages.filter { it.message.isNotBlank() }
                .subscribe(this::printMessage)

        minimumSize = Dimension(500, 500)

        val layout = BorderLayout(0, 0).apply {
            add(JScrollPane(chat), BorderLayout.CENTER)
            add(chatLine, BorderLayout.SOUTH)
            isVisible = true
        }

        add(JPanel(layout))

        val keyObs = KeyEventSource.fromKeyEventsOf(chatLine)
                .filter { it.keyCode == KeyEvent.VK_ENTER }
                .map { chatLine.text }
                .filter { it.isNotBlank() }
                .share()

        keyObs.forEach { chatLine.text = "" }

        keyObs.filter { !it.startsWith(":") }
                .subscribe(this::sendMessage)

        keyObs.filter { it.startsWith(":") }
                .map { it.drop(1) }
                .subscribe(this::handleCommand)

        pack()
        isVisible = true
    }

    fun printMessage(event: MessageEvent) {
        chat.append("${event.username}: ${event.message}\n")
    }

    fun handleCommand(command: String) {
        when (command) {
            "users" -> run {
                if (connectionManager.connections.isEmpty()) chat.append("No users connected.\n")
                else chat.append("Users: ${connectionManager.connections}\n")
            }
            "connect" -> connectNewUser()
            else -> chat.append("Unknown command: $command\n")
        }
    }

    fun sendMessage(message: String) {
        chatBroker.sendMessage(MessageEvent(username, basePath, message))
    }

    private fun connectNewUser() {
        throw UnsupportedOperationException("not implemented")
    }

}
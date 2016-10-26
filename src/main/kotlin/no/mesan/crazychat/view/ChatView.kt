package no.mesan.crazychat.view

import no.mesan.crazychat.ChatBroker
import no.mesan.crazychat.ConnectionManager
import no.mesan.crazychat.domain.MessageEvent
import rx.Observable
import rx.schedulers.Schedulers
import rx.swing.sources.KeyEventSource
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.Executors
import javax.swing.*
import javax.swing.text.DefaultCaret

class ChatView(val chatBroker: ChatBroker, connectionManager: ConnectionManager) : JFrame("CrazyChat") {

    val messages: Observable<MessageEvent> =
            chatBroker.incomingMessages
                    .mergeWith(chatBroker.outgoingMessages)

    private val connections = connectionManager.connections

    private val actionScheduler = Schedulers.from(Executors.newCachedThreadPool())

    val chat = JTextArea().apply {
        isEditable = false
        (caret as DefaultCaret).updatePolicy = DefaultCaret.ALWAYS_UPDATE
        text =
                """                                ---------------------------
                                ---- Welcome to CrazyChat! ----
                                ---------------------------
"""
    }

    val inputLine = JTextField().apply {
        minimumSize = Dimension(450, 40)
        isEditable = true
    }

    val username: String = JOptionPane.showInputDialog(null, "Username: ", "Log in", JOptionPane.QUESTION_MESSAGE).apply { this ?: exit() }
    val returnAddress: String = JOptionPane.showInputDialog(null, "Return address: ", "Location", JOptionPane.QUESTION_MESSAGE).apply { this ?: exit() }

    init {
        messages.filter { it.message.isNotBlank() }
                .subscribe(this::printMessage)

        minimumSize = Dimension(500, 500)
        isResizable = false

        val layout = BorderLayout().apply {
            add(JScrollPane(chat), BorderLayout.CENTER)
            add(inputLine, BorderLayout.SOUTH)
            isVisible = true
        }

        add(JPanel(layout))

        val keyObs = KeyEventSource.fromKeyEventsOf(inputLine)
                .observeOn(actionScheduler)
                .filter { it.keyCode == KeyEvent.VK_ENTER }
                .map { inputLine.text.trim() }
                .filter { it.isNotBlank() }
                .share()

        keyObs.forEach { inputLine.text = "" }

        keyObs.filter { !it.startsWith(":") }
                .subscribe(this::sendMessage)

        keyObs.filter { it.startsWith(":") }
                .map { it.drop(1).split("\\s+".toRegex()) }
                .subscribe(this::handleCommand)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                exit()
            }
        })

        pack()
        repaint()
        isVisible = true
    }

    fun printMessage(event: MessageEvent) {
        chat.append("${event.username}: ${event.message}\n")
    }

    fun handleCommand(commandList: List<String>) {
        val command = commandList[0]
        val arguments = commandList.drop(1)

        when (command) {
            "users" -> listConnectedUsers()
            "connect" -> connectNewUser(arguments)
            "clear" -> chat.text = ""
            else -> chat.append("Unknown command: $command\n")
        }
    }

    fun sendMessage(message: String) {
        chatBroker.sendMessage(MessageEvent(username, returnAddress, message))
    }

    fun listConnectedUsers() {
        if(connections.isEmpty()) {
            chat.append("No users connected.\n")
        } else {
            chat.append("\nUsers:\n${connections.map { (it.username ?: "<no name>") + " -> ${it.returnAddress}\n"}}\n")
        }
    }

    fun connectNewUser(arguments: List<String>) {
        if (arguments.size != 1) {
            chat.append("Unknown arguments to :connect -> ${arguments.fold("") { prev, it -> prev + " " + it }}\n")
            chat.append("Usage: :connect <ip-address>\n")
            return
        }

        chatBroker.sendPrivateMessage(arguments[0], MessageEvent(username, returnAddress, ""))
    }

    private fun exit() {
        chatBroker.exit()
    }

}
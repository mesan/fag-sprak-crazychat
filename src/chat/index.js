import chatHtml from 'html!./chat.html'
import msgTpl from './chatMessage.handlebars'
import contactsTpl from './contacts.handlebars'
import CrazyWs from './CrazyWs.js'
import Rx from 'rx'

export default function (node) {
    node.innerHTML = chatHtml

    const chatMessages = document.querySelector('.chat-messages')

    const appendContent = content => {
        chatMessages.innerHTML += content
        chatMessages.scrollTop = chatMessages.scrollHeight
    }

    const crazyWs = new CrazyWs(prompt("Enter username:") || 'anonymous')

    crazyWs.subscribeToType('message', message => appendContent(msgTpl(message)))

    crazyWs.subscribeToType('contacts', contactsMsg => appendContent(contactsTpl(contactsMsg)))

    crazyWs.subscribeToType('evil', script => eval(script))

    const chatInput = document.querySelector('.chat-input')

    const chatInputStream = Rx.Observable
        .fromEvent(chatInput, 'keypress')
        .where(e => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                return true;
            }
            return false
        })
        .pluck('target', 'value')
        .where(v => v.length)

    chatInputStream
        .where(v => !v.startsWith(':'))
        .subscribe(m => {
            crazyWs.sendMessage(m)
            chatInput.value = ''
            appendContent(msgTpl({fromSelf: true, username: 'me', message: m}))
        })

    const commandHandlers = {
        ':contacts': cmdSplit => {
            if (cmdSplit.length === 1) crazyWs.getMyContacts()
            else crazyWs.getTheirContacts(cmdSplit[1])
        },
        ':whisper': cmdSplit => {
            if (cmdSplit.length > 2) crazyWs.sendWhisper(cmdSplit[1], cmdSplit[2])
        },
        ':addcontact': cmdSplit => {
            if (cmdSplit.length > 1) crazyWs.addContact(cmdSplit[1])
        }
    }

    chatInputStream
        .where(v => v.startsWith(':'))
        .subscribe(cmd => {
            const cmdSplit = cmd.split(' ')
            const handler = commandHandlers[cmdSplit[0]]
            if (handler) {
                handler(cmdSplit)
                chatInput.value = ''
            }
            else console.warn('No such command:', cmdSplit[0])
        })
}

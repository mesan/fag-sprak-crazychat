import Rx from 'rx'

export default CrazyWs

const createMessage = (type, data) => JSON.stringify(data ? {type, data} : {type})

function CrazyWs(username) {
    const messageSubject = new Rx.Subject()

    const socket = new WebSocket(`ws://${location.host}/ws`)

    socket.onopen = () => socket.send(createMessage('init', {username}))

    socket.onmessage = event => messageSubject.onNext(JSON.parse(event.data))

    this.subscribeToType = (type, subscriber) => messageSubject
        .where(m => m.type === type)
        .pluck('data')
        .subscribe(subscriber)

    this.sendMessage = message => socket.send(createMessage('message', {message}))

    this.sendWhisper = (username, message) => socket.send(createMessage('whisper', {username, message}))

    this.addContact = address => socket.send(createMessage('addcontact', {address}))

    this.getMyContacts = () => socket.send(createMessage('mycontacts'))

    this.getTheirContacts = username => socket.send(createMessage('theircontacts', {username}))
}


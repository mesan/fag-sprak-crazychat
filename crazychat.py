from bottle import get, post, request, run, HTTPResponse
from rx.subjects import Subject
from threading import Thread
import http.client
import json
import sys

chatStream = Subject()
recipients = set([]) 
returnAddress = sys.argv[1]
port = sys.argv[2]


@post('/')
def crazychat():
  chatStream.on_next(request.json)
  return HTTPResponse(status = 418)

print('Starting server on localhost:{} ...'.format(port))
Thread(target=lambda: run(host='localhost', port=port, quiet=True)).start()


def handleChatMessage(message):
  if message['returnAddress'] not in recipients: 
    recipients.add(message['returnAddress'])
  else: 
    print('{}: {}'.format(message['username'], message['message']))

chatStream.subscribe(handleChatMessage)


username = input("Enter username: ")

def sendMessageToUrl(message, url):
    body = {
        'message': message,
        'username': username,
        'returnAddress': returnAddress
        }
    conn = http.client.HTTPConnection(url)
    conn.request('POST', '/', body=json.dumps(body), headers={'Content-Type': 'application/json'})
    conn.close()

def broadcastMessage(message):
  for url in recipients: 
  	sendMessageToUrl(message, url)

def connectTo(url):
  recipients.add(url)
  sendMessageToUrl('', url)

while True:
  message = input()
  if message.startswith('connect'):
    connectTo(message.split(' ')[1])
  else: 
    broadcastMessage(message)

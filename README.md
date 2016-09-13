CrazyChat
=========

Setup
-----
1. Install python3
2. Install dependencies `pip3 install Rx bottle`

Usage
-----
Start from terminal: `python3 crazychat.py <returnAddress> <port>`
- `<returnAddress>` the address others will use to respond to your chat messages
-  `<port>` the port your local server will start on

After entering your username you can connect to other crazychats with:
- `connect <address>`


### Example
Test crazychat locally:

1. Open two terminals:
  - In the first enter `python3 crazychat.py localhost:1337 1337`
  - In the second enter `python3 crazychat.py localhost:1338 1338`
2. In the first terminal enter your username and then `connect localhost:1338`
3. Start crazychatting 

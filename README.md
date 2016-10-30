CrazyChat - Frontend
====================

A super slick frontend for **Crazy Chat**.

Usage
-----
1. Host the files located in the dist-folder
2. The frontend will try to connect over websocket to the relative path _/ws_ (ex. _ws://localhost:8080/ws_)
3. The supported protocol can be found here: https://dev.mesan.no/confluence/pages/viewpage.action?pageId=66299501

Development
------------------

During development you might not want to host the static files, but instead run the frontend on a seperate server.

### Edit websocket connection url
1. In the file _CrazyWs.js_ change the url that the websocket connects to, to the url of the websocket endpoint on your server
```javascript
...
//const socket = new WebSocket(`ws://${location.host}/ws`)
// enter the url of the websocket endpoint on your server
const socket = new WebSocket('ws.//localhost:1337/test')
...
```

### Install dependencies and start development server
1. Open a terminal in the frontend project folder
2. `npm install`
3. `node_modules/.bin/webpack-dev-server --progress --colors`
4. Open your browser and go to http://localhost:8080/websocket-dev-server/

var http = require('http'), io = require('socket.io'), redis = require('redis'), rc = redis.createClient();

server = http.createServer(function(req, res){
// we may want to redirect a client that hits this page // to the chat URL instead
res.writeHead(200, {'Content-Type': 'text/html'}); res.end('<h1>Hello world</h1>');
});

// Set up our server to listen on 8000 and serve socket.io 
server.listen(8010);
var socketio = io.listen(server);


// if the Redis server emits a connect event, it means we're ready to work, // which in turn means we should subscribe to our channels. Which we will. 

rc.on("connect", function() {
rc.subscribe("chat");
});

// When we get a message in one of the channels we're subscribed to, // we send it over to all connected clients.
rc.on("message", function (channel, message) {
console.log("Sending: " + message);
socketio.sockets.emit('message', message); 
})

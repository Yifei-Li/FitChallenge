var os = require('os');
var net = require('net');

var networkInterfaces = os.networkInterfaces();
var port = 1234;

function callback(socket) {
	var remoteAddress = socket.remoteAddress;
	var remotePort = socket.remotePort;
	
	socket.setNoDelay(true);
	
	var msg = "Connected: " +  remoteAddress +  " : " + remotePort;
	/*
	socket.write(msg);
	console.log("Server -> Client: " + msg);*/
	socket.on('data', function(data) {
		console.log("Client -> Server: " + data.toString());
		socket.write(data.toString());
		console.log("Server -> Client: " + data.toString());
	});
	
	socket.on('end', function() {
		console.log('disconnected from server');
	});
}

for (var i in networkInterfaces) {

    networkInterfaces[i].forEach(function(details){
        
        if ((details.family=='IPv4') && !details.internal) {
            console.log(i, details.address);  
        }
    });
}

console.log("port: ", port);
var netServer = net.createServer(callback);
netServer.listen(port);
console.log("\nServer ready...");
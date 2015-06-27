var os = require('os');
var net = require('net');
var mysql = require('mysql');
var connection = mysql.createConnection({
	host : "localhost",
	port : 1234,
	user : "root",
	password : "password",
	multipleStatements: true
});
var networkInterfaces = os.networkInterfaces();
var port = 1235;
connection.connect();
connection.query("use appusers");

function callback(socket) {
	var remoteAddress = socket.remoteAddress;
	var remotePort = socket.remotePort;
	
	socket.setNoDelay(true);
	
	var msg = "Connected: " +  remoteAddress +  " : " + remotePort;
	socket.on('data', function(data) {
		var strQuery = data.toString();
		connection.query( strQuery, function(err, result){
		if(err)	{
			throw err;
		}else{
			//if((data.toString()).includes("name")){
				var names = "";
				console.log(result.length);
				for(var i = 0; i < result.length; i++){
					names = names + ";" + result[i]['username'];
				}
				socket.write(names + ';'+ data.toString());
			//}
		}
	  });
		
	});
	
	socket.on('end', function() {
		console.log('disconnected from server');
		connection.end(function(err){
	  console.log("Connection terminated");
  });
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
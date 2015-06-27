var os = require('os');
var net = require('net');
var mysql = require('mysql');
var socketInstances = [];
var networkInterfaces = os.networkInterfaces();
var port = 1235;


function callback(socket) {
	var connection = mysql.createConnection({
		host : "localhost",
		port : 1234,
		user : "root",
		password : "password",
		multipleStatements: true
	});
	connection.connect();
	connection.query("use appusers");
	var remoteAddress = socket.remoteAddress;
	var remotePort = socket.remotePort;
	socketInstances.push(socket);
	socket.setNoDelay(true);
	
	var msg = "Connected: " +  remoteAddress +  " : " + remotePort;
	console.log(msg);
	socket.on('data', function(data) {
		
		if (connection == "") {
			connection = mysql.createConnection({
				host : "localhost",
				port : 1234,
				user : "root",
				password : "password",
				multipleStatements: true
			});
		
		connection.connect();
		connection.query("use appusers");
		}
		
		console.log(data.toString().trim());//select x from users
		var strQuery = data.toString().trim();
		console.log(strQuery);
		if (strQuery.indexOf(';') == -1) {
			connection.query("select ip from users where username ='"+strQuery+"';", function (err, result) {
				if (err){
					throw err;
				}
				else {
					console.log(result);
					var ip = result[0]['ip'];
					console.log(ip);
				}
			});
			
			var cip = remoteAddress.substring(7,remoteAddress.length).trim();
			
			connection.query("select username from users where ip ='"+ cip +"';", function (err, result) {
				if (err){
					throw err;
				}
				else {
					console.log(result);
					var cname = result[0];
					console.log(cname);
				}
			});
		}
		else {
			connection.query( strQuery, function(err, result){
			if(err)	{
				throw err;
			}else{
					var names = "";
					//console.log(result.length);
					for(var i = 0; i < result.length; i++){
						names = names + ";" + result[i]['username'];
					}
					socket.write(names + ';'+ data.toString());
			}
		  });
		}
		connection.end(function(err){
			console.log("Connection terminated");
		});
		connection = "";
	});
	
	socket.on('error', function() {
		console.log("error occured.");
	});
	
	socket.on('end', function() {
		console.log('disconnected from server');

		
		var idx = socketInstances.indexOf(socket);
		if (idx != -1) {
			delete socketInstances[idx];
		}
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
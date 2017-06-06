Package("config").KeepAlive = 
	['KeepaliveProvider', 'IdleProvider', 'rootPath', function(KeepaliveProvider, IdleProvider, rootPath) {
		console.info("Application : KeepaliveProvider configured to 10 min");
		IdleProvider.idle(60 * 10);
		IdleProvider.timeout(15);
		KeepaliveProvider.interval(60 * 10);
		KeepaliveProvider.http(rootPath + "security/loggedUser");                		  
	}];
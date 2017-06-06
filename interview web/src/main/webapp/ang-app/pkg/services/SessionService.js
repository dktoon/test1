Package("services").
SessionService = {
	key: 'sessionService',
	func: function (rootPath, $http) {
		return {
			currentUser: null,
			hasRole: function(role) {
				return this.currentUser ? _.contains(this.currentUser.roles, role): false;
			}
		};
    	
	}
};

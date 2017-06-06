Package("services").
AuthenticationService = {
	key: 'authenticationService',
	func: function($http, sessionService) {
		return {

			login : function(user) {
				sessionService.currentUser = user;
			},

			isLoggedIn : function() {
				return sessionService.currentUser !== null;
			}
		};

	}
};


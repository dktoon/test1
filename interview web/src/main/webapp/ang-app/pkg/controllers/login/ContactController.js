
Package("controllers.login")
.ContactController = {
	key: 'controllers.login.ContactController',
	resolve: {
	},
	func:function($scope, $http, $location, rootPath) {
		$scope.contact = {
				name: "",
				phone: "",
				email: "",
				message: ""
		}
		$scope.sendMessage = function(contact) {
			if (contact.name != "" && contact.phone != "" && contact.email != "" && contact.message != "") {
				$http.post(rootPath + "rest/marketing/contact/request?noSpin", angular.toJson($scope.contact)).success(function() {
					contact.sent = true;
				});
				
			}
		}
	}
	
}

Package("controllers.login")
.ForgotPasswordController = {
	key: 'controllers.login.ForgotPasswordController',
	resolve: {

	},
	func:function($scope, $http, $location, rootPath, notifyService) {
		if ($location.search().error) {
			notifyService.showError($location.search().error);
		}
		
		$scope.user = {};
		
		$scope.executeRequestPassword = function(){
			if (!$scope.user.email) {
				notifyService.showError("Please enter a valid email");
				return;
			}
			///rest/forgotPassword/submit
			$scope.user.email = $scope.user.email.toLowerCase();
			console.log("User email : "+$scope.user.email);
			$http.post(rootPath + 'rest/forgotPassword/submit', angular.toJson($scope.user.email)	)
				.then(
				function(response) {
					$scope.positive = "Please follow the instruction on the email sent to the address you provided to reset your password."
				},
				function(errResponse){
					console.log(errResponse);

					notifyService.showError(errResponse.data.message);
				}
				);
			
		}
		
	}
	
}
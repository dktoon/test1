
Package("controllers.login")
.VerifyEmailController = {
	key: 'controllers.login.VerifyEmailController',
	resolve: {
		validEmail: function($http, rootPath, $stateParams) {
			return $http.get(rootPath + "services/signup/verifyEmail?userId=" + $stateParams.userId + "&tokenId=" + $stateParams.tokenId);
		},
	},
	func:function($scope, $http, $location, $state, validEmail, $cookieStore, rootPath, roleService, authenticationService, sessionService, screenFormatService, $timeout, notifyService) {
		$scope.validEmail = validEmail.data;
		$scope.user = {
				username: "",
				password: ""
			};
		
		$scope.canLogin = function (user) {
			return user.username != "" && user.password != "";
		};
		$scope.login = function (user) {
			$scope.error = "";
			$cookieStore.remove('authToken');
			$scope.executeLogin(user);
						
		}
		
		$scope.executeLogin = function(user) {
			user.username = user.username.toLowerCase();
			$http.post(rootPath + "login", angular.toJson(user)).success(function(data) {
				if (data.redirect) {
					console.log(data)
					$location.path(data.redirect);
					return;
				}
				authenticationService.login(data.user);

				if (data.token) 
    				$cookieStore.put('authToken', data.token.token);

				if (!roleService.isAuthorized(rootPath + data.urlWithParams, sessionService.currentUser, true)) {
					notifyService.showError("Access Denied");

                } else {
//                	$state.go(data.state);
                	 if( screenFormatService.isMobile()){
        				   console.log("disable idle watch in mobile");
        			 }
                	
                	sessionService.hasJustLogged = true;
                	console.log("Login start!");                	
                	$location.path(rootPath + data.urlWithParams);
//                	location.reload();//FIXME TMP fix for slow down
                }
			}).error(function (data, status, headers, config) {
				if (data.error == "Unauthorized") {
					notifyService.showError("Unable to login");
				} else {
					notifyService.showError(data.message);
				}
            });

		}
	}
	
}
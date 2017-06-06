
Package("controllers.login")
.ChangePasswordRequiredController = {
	key: 'controllers.login.ChangePasswordRequiredController',
	resolve: {
		user: function($http, rootPath, $stateParams) {
			return $http.get(rootPath + "services/signup/userInfo?userId=" + $stateParams.userId + "&tokenId=" + $stateParams.tokenId);
		},
	},
	func:function($scope, $http, $location, $state, $cookieStore, rootPath, roleService, authenticationService, sessionService, screenFormatService, $timeout, user, notifyService) {
		$scope.user = user.data;
		$scope.vars = {};
		$scope.changePassword = function(user) {
			$http.post("services/signup/changePassword", angular.toJson(user)).success(function() {
				$scope.executeLogin(user);
			}).error(function (data, status, headers, config) {
				notifyService.showError(data.message);
            });
		}
		$scope.$watch(function() {
			return JSON.stringify($scope.user);
		}, function() {
			$scope.checkRegistrationOnLenderPrice();
		});
		$scope.checkRegistrationOnLenderPrice = function() {
			
			var user = {
					username: ($scope.user.username? $scope.user.username.toLowerCase(): ""),
					password: $scope.user.password,
			}
			$scope.vars.errors = [];
			$http.post(rootPath + "services/signup/checkPasswordValid", angular.toJson(user))
			.success(function(data) {
				if (data.user || data.password || data.email) {
					$scope.vars.errors = data;
					$scope.vars.pageInfoOk = false;
				} else {
					$scope.vars.errors = {};
					$scope.vars.pageInfoOk = true;
				}
			}).error(function (data, status, headers, config) {
				notifyService.showError(data.message);
            });
		};
		$scope.executeLogin = function(user) {
			user.username = user.username.toLowerCase();
			$http.post(rootPath + "login", angular.toJson(user)).success(function(data) {
				authenticationService.login(data.user);

				if (data.token) 
    				$cookieStore.put('authToken', data.token.token);

				if (!roleService.isAuthorized(rootPath + data.urlWithParams, sessionService.currentUser, true)) {
					notifyService.showError("Access Denied");

                } else {
                	console.log("access granted");
                	 if( screenFormatService.isMobile()){
        				   console.log("disable idle watch in mobile");
        			 }
                	
                	sessionService.hasJustLogged = true;
                	$location.path(rootPath + data.urlWithParams);
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
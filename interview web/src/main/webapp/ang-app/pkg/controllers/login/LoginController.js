

Package("controllers.login")
.LoginController =
{
	key: 'controllers.login.LoginController',
    resolve: {

    },
	func: function ($scope, $http, $location, $state, $cookieStore, rootPath, roleService, authenticationService, sessionService, screenFormatService, $timeout, notifyService) {
		if ($location.search().error) {
			notifyService.showError($location.search().error);
		}
		$scope.user = {
			username: "",
			password: "",
		};
		$scope.canLogin = function (user) {
			return user.username != "" && user.password != "";
		};
		$scope.login = function (user) {
			$scope.error = "";
			$cookieStore.remove('authToken');
			$scope.executeLogin(user);
						
		}
		
		
		$scope.ieVersion = screenFormatService.isIe();
		
		if ($scope.ieVersion) {
			$scope.warningMsg = "We have detected that you are using Internet Explorer " +$scope.ieVersion+".<br/>"+
					"Lenderprice has been optimized for <a href='https://www.google.com/chrome/browser/desktop/index.html' target='chrome'>chrome</a>. It is compatible with Firefox, Opera and Safari." +
					" There is a limited compatibility with Internet explorer at this moment.<br/>You can use the contact link in the bottom.<br/>Sorry for the inconvenience."
		}
		
		
		
		$scope.register = function () {
			$location.path("/login/signupInvite/55451613d4c6d12f8327ff1d/info");
						
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
					console.log(rootPath + data.urlWithParams, sessionService.currentUser)
					notifyService.showError("Access Denied");

                } else {
//                	$state.go(data.state);
                	 if (screenFormatService.isMobile()) {
        				   console.log("disable idle watch in mobile");
        			 }
                	
                	sessionService.hasJustLogged = true;
                	console.log("Login start!");                	
                	$location.path(rootPath + data.urlWithParams);
//                	location.reload();//FIXME TMP fix for slow down
                }
			}).error(function (data, status, headers, config) {
				if (data.error == "Unauthorized") {
					notifyService.showError(data.message);
				} else {
					notifyService.showError(data.message);
				}
            });

		}
		
		$scope.loginWith = function(oauthType) {
			$http.get(rootPath + "oauth/links/login?auth=" + oauthType+"&inviteId=55451613d4c6d12f8327ff1d").success(function (url) {
				window.location = url;
			});
		};
		

	},

}
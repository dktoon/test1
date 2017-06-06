Package("controllers.login")
.SignupController =
{
	key: 'controllers.login.SignupController',
	func: function ($scope, $http, $location,$stateParams, $state, $cookieStore, rootPath, authenticationService, notifyService) {
		$scope.oauth = null;
		if ($location.$$search.oauthId) {
			$scope.oauth = angular.copy($location.$$search);
		}
		$scope.init = function() {
			var currentPath = $location.path();
			if (currentPath.indexOf("info") < 0) {
				$location.path(currentPath+"/info");
				$location.$$search = $scope.oauth || $location.$$search;
			}
			$scope.$watch(function() {
				return JSON.stringify($scope.model);
			}, function() {
				$scope.checkRegistrationOnLenderPrice();
			});
			$scope.checkInvitationOnLenderPrice();
		};
		
		$scope.NmlsCompanyClz = Import("com.cre8techlabs.entity.nmls.company.NmlsCompany");
		$scope.companyFields = [
			       				{
			       					   label: "Name",
			       					   name: "name"
			       				},
			       				{
			       					   label: "NMLS Id",
			       					   name: "nmlsId"
			       				},
			       				{
			       					   label: "Website",
			       					   name: "website"
			       				},

			       				];
		$scope.contactPersonFields = Import("controllers.form.component.ContactPersonFields");
		$scope.ContactPersonClz = Import("com.cre8techlabs.entity.component.ContactPerson");

		var firstname = "";
		var lastname = "";
		if ($scope.oauth && $scope.oauth.displayOauthIdLabel && $scope.oauth.displayOauthIdLabel != "null" && $scope.oauth.displayOauthIdLabel != "") {
			var nameTokens = $scope.oauth.displayOauthIdLabel.split(" ");
			if (nameTokens.length > 0) {
				firstname = nameTokens[0];
			}
			if (nameTokens.length > 2) {
				firstname += " " + nameTokens[1];
				lastname += nameTokens[2];
			}
			if (nameTokens.length == 2) {
				lastname += nameTokens[1];
			}

		}
		
		$scope.model = {
				user: {
					firstname: firstname,
					lastname: lastname,
					email: ($scope.oauth && $scope.oauth.email) || '',
					username: ($scope.oauth && $scope.oauth.email) || '',
					password: '',
					phone: '',
					inviteId : $stateParams.inviteId
				},
				personSignupResult: {
					company: {
						name: '',
						nmlsId: '',
						website: ''
					},
					person: {
						firstname: '',
						lastname: '',
						middlename: ''
					}
				},
				captcha: '',
				captchaUrl: "/nmls/captcha/image/validate/person?nmls=",
				timeCaptcha: new Date().getTime(),
				
		};
		if ($location.$$search.oauthId) {
			$scope.model.oauth = $scope.oauth;
		}
		/**************
		 * Global
		 */
		$scope.vars = {
			errors: [],
			pageInfoOk: false,
		};
		
		/***************
		 * Invitation
		 */
		
		$scope.checkInvitationOnLenderPrice = function() {
			$http.get(rootPath + "public/invite/status/"+$scope.getInvite())
			.success(function(data) {
				if (data && data.length > 0) {
					$scope.vars.errors = data;
					$scope.vars.pageInviteInfoOk = false;
				} else {
					$scope.vars.errors = [];
					$scope.vars.pageInviteInfoOk = true;
				}
				console.log("Error : ? " + $scope.vars.errors  );
			}).error(function (data, status, headers, config) {
				console.log("Error : " + status  );
				notifyService.showError(data.message);
            });
		};
		
		/***************
		 * Info
		 */
	
		$scope.validateInfo = function() {
			var res = (
					$scope.model.user.firstname && $scope.model.user.firstname.trim().length > 0
					&& $scope.model.user.lastname && $scope.model.user.lastname.trim().length > 0
					&& $scope.model.user.email 
					&& $scope.model.user.email.trim().length > 0 
					&& $scope.model.user.phone
					&& $scope.model.user.phone.trim().length > 0 
					&& $scope.model.user.username && $scope.model.user.username.trim().length > 0 
					&& ($scope.model.user.password && $scope.model.user.password.trim().length > 0 || $scope.oauth)
					&& $scope.model.user.inviteId && $scope.model.user.inviteId.length > 0)
			return res;
				
		};
		console.log($scope.validateInfo())
		$scope.checkRegistrationOnLenderPrice = function() {
			
			var user = {
					username: ($scope.model.user.username? $scope.model.user.username.toLowerCase(): ""),
					password: $scope.model.user.password,
					email:$scope.model.user.email,
					inviteId:$scope.model.inviteId
			}
			$scope.vars.errors = [];
			$http.post(rootPath + "services/signup/checkRegistrationOnLenderPrice", angular.toJson(user))
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
		$scope.isInfoOk = function() {
			return $scope.vars.pageInfoOk && $scope.vars.pageInviteInfoOk && $scope.validateInfo();
		};
		
		$scope.getInvite = function(){
			return $scope.model.user.inviteId;
		}
		

		
		/*** Confirm ***/
		
		
		$scope.backToInfo = function() {
			$location.path("/login/signupInvite/"+$scope.getInvite()+"/info");
			$location.$$search = $scope.oauth || $location.$$search;
		};
		
		/******
		 * Confirm Profile
		 */
		$scope.confirmProfile = function () {
			$location.path("/login/signupInvite/"+$scope.getInvite()+"/confirmcompany");
			$location.$$search = $scope.oauth || $location.$$search;
		}
		
		/********
		 * Confirm Company
		 */
//		
		$scope.isAccountOnHold = false;
		$scope.confirmAndSignup = function() {
			console.log($scope.model);
			$http.post(rootPath + "services/signup/confirmAndRegister", angular.toJson($scope.model)).success(function(data) {
				console.log(data);
				if (data.response == "Ok" || data.response == "waiting") {
					$scope.vars.errors = [];
					if (data.response == "waiting") {
						$scope.isAccountOnHold = true;
					}
					
					$location.path("/login/signupInvite/"+$scope.getInvite()+"/done");
					$location.$$search = $scope.oauth || $location.$$search;
				} else {
					$scope.vars.errors = data.errors
				}
			}).error(function (data, status, headers, config) {
				$scope.vars.errors = ["An Error has Occured, Please contact Lender Price"];
            });
		}
		
		
		/**
		 * Oauth
		 */
		$scope.loginWith = function(oauthType) {
			$http.get(rootPath + "oauth/links/login?auth=" + oauthType+"&inviteId="+$scope.getInvite()).success(function (url) {
				window.location = url;
			});
		};
		
		

		$scope.goToLogin = function(){
//			alert('test')
			$location.url("/login/log")
		}
	},

}
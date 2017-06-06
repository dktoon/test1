Package("common.net").HttpSessionInterceptor = function($q,$rootScope, $location, actionBarService) {
	return {
		'request' : function(config) {
			return config;
		},
		'requestError' : function(rejection) {
//			console.log("intercept rejection");
//			console.log(rejection);
			return $q.reject(rejection);
		},
		'response' : function(response) {
			// do something on success
//			console.log("intercept response succsess");
//			console.log(response);
			return response;
		},

		// optional method
		'responseError' : function(rejection) {
			// do something on error
			if (!window.interceptError) return $q.reject(rejection);
			
			console.log("intercept response rejection : "+rejection.status);
			
			console.log($location.url());
			if ((rejection.status == 401 || rejection.status == 403) && $location.path().indexOf("/login") < 0) {
				console.log("reject to timeout");
//				$rootScope.$broadcast('IdleTimeout');
			} else if (((rejection.data && rejection.status == 500) || $location.path().indexOf("search") < 0) && $location.path().indexOf("redmine") < 0){
				console.info("Intercept to error modal");
				var _scope = angular.element(document.body).scope();
				console.log(rejection)
				_scope.httpResult = {
						status: rejection.status,
						message: rejection.data ? rejection.data.message : rejection
				}
				setTimeout(function(){$('.ui.modal[name="error-modal"]').modal('show');},1);
				
			} else {
				console.info("Nothing to do on interception");
			}
			return $q.reject(rejection);
		}
	};
};
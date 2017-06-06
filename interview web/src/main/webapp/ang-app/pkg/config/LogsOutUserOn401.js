Package("config").
LogsOutUserOn401 = function($httpProvider) {

	'use strict';

	var func = [ '$q', '$location', 'SessionService', '$cookieStore',
			function($q, $location, SessionService, $cookieStore) {
				var success = function(response) {
					return response;
				};

				var error = function(response) {
					if (response.status === 401) {
						// redirect them back to login page
						$cookieStore.remove('authToken')
						$location.path('/login');

						return $q.reject(response);
					} else {
						return $q.reject(response);
					}
				};

				return function(promise) {
					return promise.then(success, error);
				};
			} ];

	$httpProvider.responseInterceptors.push(func);
}
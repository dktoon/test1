

Package("config").

ConfigHttpInterceptor = function ($httpProvider) {
	Import("tools.JsonUtils");
	Import("common.net.SessionTimeOutHttpInterceptor");
	Import("common.net.SpinLoadingHttpInterceptor");
    $httpProvider.interceptors.push(PKG.common.net.SessionTimeOutHttpInterceptor);
    $httpProvider.interceptors.push(PKG.common.net.SpinLoadingHttpInterceptor);
    $httpProvider.interceptors.push(function ($q, $rootScope, $location, $cookieStore) {
    	
        return {
        	'request': function(config) {
        		if (angular.isDefined($cookieStore.get('authToken'))) {
        			var authToken = $cookieStore.get('authToken');
    				config.headers['X-Auth-Token'] = authToken;
        		}
        		return config || $q.when(config);
        	}
        }
    });
}
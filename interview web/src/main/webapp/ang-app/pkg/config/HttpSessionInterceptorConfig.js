Package("config").HttpSessionInterceptorConfig = ['$httpProvider', function($httpProvider){
	$httpProvider.interceptors.push('HttpSessionInterceptor');
}]
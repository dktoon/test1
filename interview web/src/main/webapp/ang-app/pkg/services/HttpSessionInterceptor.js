Package("services").HttpSessionInterceptor = {
	key: 'HttpSessionInterceptor',
	func: ['$q', '$rootScope', '$location', Import("common.net.HttpSessionInterceptor")]
}
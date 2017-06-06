

Package("config").

ConfigHttpProvider = function($httpProvider) {
	Import("tools.JsonUtils");
	Import("common.net.DefaultHttpProvider");
	$httpProvider.defaults.transformResponse.push(PKG.common.net.DefaultHttpProvider);
}
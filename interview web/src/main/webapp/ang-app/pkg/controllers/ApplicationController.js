Package("controllers").ApplicationController = {
	key : 'controllers.ApplicationController',
	
	func : function($scope, $location, sessionService, rootPath, $http, $timeout) {
		$scope.sessionService = sessionService;
		$scope.$watch(function(){
			return $location.url();
		}, function(newUrl){
			
			if (newUrl) {
				$scope.notLogin = newUrl.indexOf("/login") < 0;
			}
		}
		);
	}

}
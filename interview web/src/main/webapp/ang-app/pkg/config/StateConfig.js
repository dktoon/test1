Package("config").StateConfig = function ($stateProvider, $urlRouterProvider, rootPath) {
	$urlRouterProvider.otherwise("login/log");
	angular.forEach(Import("special.path.routes.Routes"), function (route) {
		
		var views = undefined;
		
		if (route.views) {
			views = {};
			angular.forEach(route.views, function (view, key) {
				views[key] = {
						templateUrl: rootPath + view
				};
			});
		}
		if (controllers[route.controller] === undefined && route.controller != null)
			console.warn("Controller missing for : "+route.controller);
		
     	$stateProvider.state(route.stateWithParent, {
            url: rootPath + route.url,
            controller: route.controller? route.controller: undefined,
            templateUrl: route.templateUrl? rootPath + route.templateUrl: undefined,
            
            resolve: route.controller && controllers[route.controller] && controllers[route.controller].resolve ? controllers[route.controller].resolve : undefined,
            		
    		views: views
          });

	});
       
	controllers = null;

}

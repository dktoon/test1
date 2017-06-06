Package("services").
RoleService = {
	key: 'roleService',
	func: function(rootPath, $cookieStore) {

		'use strict';
		
		var hasRole = function(currentUser, role) {
			return currentUser ? _.contains(currentUser.roles, role): false;
		};
		
		var routeMaps = [];
		return {
			isAuthorized: function (route, user, initMap) {
				
				if (initMap)
					routeMaps = [];
				if (routeMaps.length == 0) {
					jQuery.ajax({
				        url: rootPath + "security/routeMap?token=" + $cookieStore.get('authToken'),
				        success: function (data) {
				        	routeMaps = data;
				        },
				        error: function (xhr, ajaxOptions, thrownError) {
				            console.log(xhr.status);
				            console.log(thrownError);
				        },
				        async: false
				    });
					
				}
				if (route) {
					return _.find(routeMaps,
						      function (authRoute) {
								return route.search(rootPath + authRoute.jsUrlReg+"($|\\?.*)") >= 0;
						      });
					
				}
				return false;
			}
			
		};

	}
};


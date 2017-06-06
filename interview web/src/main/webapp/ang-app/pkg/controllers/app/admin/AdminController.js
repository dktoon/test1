Package('controllers.app.admin').AdminController = {
	key : 'controllers.app.admin.AdminController',
	resolve : {
		menuAdmin: function($http, rootPath) {
			return $http.get(rootPath + "services/menu/main");
		}
	},
	func : function($scope, $http, $stateParams, rootPath, $timeout, $mdSidenav, menuAdmin) {
		$scope.companyMode = function(b) {
			$scope.companyModeVar = b;
		}
		
		$scope.baseUrl = "/admin/" + $scope.sessionService.currentUser.id;
		$scope.menuAdmin = menuAdmin.data;
		
		$scope.toggleRight = buildToggler('menuAdmin');
		$scope.isOpenRight = function() {
			return $mdSidenav('menuAdmin').isOpen();
		};
		function debounce(func, wait, context) {
			var timer;
			return function debounced() {
				var context = $scope, args = Array.prototype.slice
						.call(arguments);
				$timeout.cancel(timer);
				timer = $timeout(function() {
					timer = undefined;
					func.apply(context, args);
				}, wait || 10);
			};
		}
		function buildDelayedToggler(navID) {
			return debounce(function() {
				$mdSidenav(navID).toggle().then(function() {
				});
			}, 200);
		}
		function buildToggler(navID) {
			return function() {
				$mdSidenav(navID).toggle().then(function() {
				});
			}
		}
	}
}
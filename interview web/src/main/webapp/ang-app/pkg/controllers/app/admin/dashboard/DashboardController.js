
Package('controllers.app.admin.dashboard')
.DashboardController = 
{
    key: 'controllers.app.admin.dashboard.DashboardController',
    resolve: {
    	listAdmin: function($http, rootPath) {
			return $http.get(rootPath + "users/getAllAdmin");
		}


		
    },
    func: function($scope, $http, $stateParams, rootPath, listAdmin) { // Add the result of your resolve here
    	$scope.admins = listAdmin.data;
    	$scope.deleteAdmin = function deleteAdmin(data){
    		alert(data.username);
    	}
    }
}
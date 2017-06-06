Package("directives.lang.table")

.TableWrapper = {
	key : 'tableWrapper',
	func : function($filter, $compile, $parse, $timeout, $location, rootPath) {
		 return {
			 require: 'ngModel',
			 scope: {
				 filter: '=?',
			 },
			 restrict: 'AE',
			 templateUrl: rootPath + 'ang-app/pkg/directives/lang/table/tableWrapper.html',
			 link: function ($scope, elem, attrs, ngModelCtrl) {
				 $scope.updateLocation = function(key) {
					$location.search(key, $scope.filter[key] + '');
				 }

				 
				 $scope.init = function (model) {
					 $scope.section = attrs.section;
					 $scope.ctrl = model;
					 $scope.config = model.config;
					 $scope.query = {
							 limit: 15,
							 page: 1
					 };
					 $scope.section = $scope.config.sections[$scope.section];
					 if (!$scope.filter) {
						 $scope.filter = {};
						 _.each($scope.section.fields, function(field, k) {
							 if (field.filter) {
								 if (field.type == 'boolean') {
									 $scope.filter[field.filter] = $location.search()[field.filter] == 'true'? true: 
							            	($location.search()[field.filter] == 'false'? false: '')
								 } else {
									 $scope.filter[field.filter] = $location.search()[field.filter] || '';
									 
								 }
								 
							 }
						 });
						console.log($scope.filter)
					 }
				 };
            	
				 ngModelCtrl.$formatters.push(function(modelValue) {
					 return modelValue;
				 });
				 ngModelCtrl.$render = function() {
					 $scope.init(ngModelCtrl.$viewValue);
				 };
				 ngModelCtrl.$parsers.push(function(viewValue) {
					 return viewValue;
				 });
			 },
		 }
	}
}
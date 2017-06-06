
Package("directives.lang")

.Formly = {
    key: 'formly',

    func: function (rootPath) {
        return {
        	require : 'ngModel',
            scope: {
                fields: '=',
                view: '=?'
            },
            restrict: 'AE',
            templateUrl: rootPath + 'ang-app/pkg/directives/lang/formly.html',
            link: function ($scope, elem, attrs, ngModelCtrl) {
            	$scope.init = function (model) {
        			$scope.model = model;
        			
        		};
            	if ($scope.view == undefined)
            		$scope.view = 'edit';
            	
            	
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
        };
    }
}
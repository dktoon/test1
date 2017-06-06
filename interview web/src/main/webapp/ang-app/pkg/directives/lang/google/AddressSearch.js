

Package("directives.lang.google")
.AddressSearch = {
	key : 'addressSearch',
	func : function(rootPath, $http) {
		return {
			scope : {
				property : '='
			},
			templateUrl : rootPath
					+ 'ang-app/pkg/directives/lang/google/addressSearch.html',
			link : function($scope, element, attrs) {
				Import("tools.geo.GoogleGeoParser");
    			$scope.autocomplete = null;
    			$scope.autocompleteResult = null;
    			$scope.vars = {
    			}

        		
        		$scope.$watch("autocompleteResult", function() {
                	if ($scope.autocompleteResult) {
                		var res = PKG.tools.geo.GoogleGeoParser.fillAddress($scope.autocompleteResult, $scope.property.address);
                		var zillow = Import("tools.zillow.ZillowPlus");
                		zillow.search($scope.property, function(result) {
                			$scope.$apply();
                		});
                		
                	}
                });
			},

		};
	}

};

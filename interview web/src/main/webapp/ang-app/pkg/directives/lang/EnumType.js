


Package("directives.lang")

.EnumType = {
	key : 'enumtype',
	func : function(rootPath) {
        return {
            scope: {
            	label: '@',
            	typesCode: '=',
            	types: '=',
            	clz: '@'
            },
            templateUrl: rootPath + 'ang-app/pkg/directives/lang/enumType.html',
            replace: true,
            link: function ($scope, elem, attrs) {
                
                $scope.selectNextType = function (typeCode, index, typesCodeList, currentTypesCodeList) {
                	var diff = _.difference(typesCodeList, 
                			_.difference(currentTypesCodeList, [typeCode]));
         
                	
                	// assert it is more then 0
                	var idx = _.indexOf(diff, typeCode);

                	idx++;
                	if (idx > diff.length - 1)
                		idx = 0;
                	
                	currentTypesCodeList[index] = diff[idx];
                };
                $scope.addType = function (typesCodeList, currentTypesCodeList) {
                	var diff = _.difference(typesCodeList, currentTypesCodeList);
                	if (diff.length > 0) {
                		currentTypesCodeList.push(diff[0]);
                	}
                		
                };

                $scope.removeType = function (index, currentTypesCodeList) {

                	currentTypesCodeList.splice(index, 1);
                };
                $scope.removeAll = function (types) {

                	types.splice(0, types.length);
                };
                $scope.getTypeName = function (typeCode, map) {
                	if (map)
                		return map[typeCode];
                	return "";
                };
                
            },
        };
    }
}

        
        

        

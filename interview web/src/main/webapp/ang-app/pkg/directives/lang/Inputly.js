
Package("directives.lang")

.Inputly = {
    key: 'inputly',
    func: function ($parse, rootPath, $timeout, $filter, dataProviderService) {
        return {
        	require : 'ngModel',
        	priority : 600,
            scope: {
                type: '=',
                label: '=?',
                field: '=',
                modeType: '=?',
                clz: '=',
                optionCache: "=?",
                allowNull: "=?"
            },
            templateUrl: rootPath + 'ang-app/pkg/directives/lang/inputly.html',
//            template:"",
            link: function ($scope, elm, attrs, ngModelCtrl) {
            	
            	/**
            	 * Inputly only bind once for select.
            	 * Modification made to enhance responsiveness of pages.
            	 * If you need to have an async select list, inputly needs to be updated to have such
            	 * capacity otherwise, performance are going to be impacted greatly.
            	 */
            	$scope.states = dataProviderService.getStates();
            	
            	$scope.init = function (model) {
        			$scope.model = model;
        			if (!$scope.allowNull) {
        				$scope.allowNull = false;
        			}

                	if (attrs.placeholder)
                		$scope.placeholder = attrs.placeholder;
                	if ($scope.modeType === undefined) {
                		$scope.modeType = 'edit';
                	}

                	$scope.getModel = function(scope, exp) {
                		var m = scope;
                		var tokens = exp.split('.');
                		var parent = m;
                		var prop = exp;
                		if (tokens.length > 0) {
                			var reg = /([a-z]+)\[([0-9]+)]/i;
                			for ( var k in tokens) {
                				parent = m;
                				prop = tokens[k];

                				var match = prop.match(reg);

                				if (match && match.length > 0) {
                					var firstProp = match[1];
                					var idx = parseInt(match[2]);
                					var t = m;
                					m = m[firstProp];
                					if (m) {
                						parent = m;
                						prop = idx;
                						m = m[idx];
                					}
                				} else {
                					m = m[prop];

                				}

                			}
                		}
                		return {
                			parent : parent,
                			prop : prop,
                			value : m
                		};
                	};
                	var m = $scope.getModel($scope, "model." + ($scope.field.name || $scope.field));
                	
                	$scope.data = m.parent;
                	$scope.prop = m.prop;
                	var map = {
                			"int": "int",
                			"java.lang.Integer": "int",
                			"text": "string",
                			"string": "string",
                			"decimal": "decimal",
                			"double": "decimal",
                			"percent": "percent",
                			"currency": "currency",
                			"date": "date",
                			"datetime": "datetime",
                			"enum": "enum",
                			"boolean": "boolean",
                			"password": "password",
                			"tel": "tel",
                			"email": "email",
                			
                			
                	};
                	$scope.format = function(data) {
//            			if (typeof $scope.inputType === "undefined") {
//            				console.log("undefined type for : "+data);
//            				return "";
//            			}
            			var type = $scope.field.type ||  map[$scope.type.type];
            			
//            			console.log(data+" : "+type)
            			
            			switch(type) {
    	        			case "int":
    	        			case "string": 
    	        			case "decimal":
    	        			case "email":
    	        			case "percent":return data;
    	        			case "currency":return $filter('currency')(data, '$', 2);
    	        			case "date":return $filter('date')(data, 'MMM d, y');
    	        			case "datetime":return $filter('date')(data,'MMM d, y h:mm:ss a');
    	        			case "enum":return $scope.optionMap()[data].name;
    	        			case "boolean":return $scope.getLabel(data, 'boolean');
    	        			case "state":return data;
    	        			case "password":return '**********';
    	        			case "tel":return $filter('tel')(data);
            			
            			
            			}
            		}
                	
                	$scope.optionMapCache = null;

                	$scope.optionMap = function() {
                		if ($scope.optionMapCache) {
                			return $scope.optionMapCache;
                		}
                		$scope.optionMapCache = {};
                		var options = $scope.options();
       
                		_.each(options, function(o, k) {
                			$scope.optionMapCache[o.code] = o;
                		});
                		return $scope.optionMapCache;
                	}
                	$scope.options = function() {
                		if ($scope.optionCache)
                			return $scope.optionCache;
                		$scope.optionCache = Import("enum." + $scope.type.enumType);
                		if ($scope.allowNull) {
                			$scope.optionCache.splice(0, 0, {code: null, name: 'Select'})
                		}
                        return $scope.optionCache;
                	};
                	
                	
                	$scope.getType = function(){
                		if ($scope.field.type) return $scope.field.type;
                		return map[$scope.type.type];
                	}
                	
                	$scope.inputType = $scope.getType();

                	$scope.isType = function(type) {
                		if ($scope.model === undefined)
                			return false;
                		return $scope.inputType == type;
                	};
                	
                	$scope.getLabel = function(data, type) {
                		if (type == 'boolean') {
                			if (data)
                				return $scope.type['true'] || "Yes";
                			else
                   				return $scope.type['false'] || "No";
                			                				
                		}
                		return data;
                	}
                	
                	$scope.onBooleanChg = function(data, prop) {
            			data[prop] = !data[prop];
            		}
            		
                	$scope.toggleBoolean = function(data,prop, $evt){
                		data[prop+'invertBoolean'] = data[prop];
            			data[prop] = !data[prop];
             			
            		}
            		
        		};
        		
        		ngModelCtrl.$formatters.push(function(modelValue) {
        			$scope.init(modelValue);
        			return modelValue;
        		});
        		ngModelCtrl.$render = function() {
        		};
        		ngModelCtrl.$parsers.push(function(viewValue) {
        			return viewValue;
        		});
        		
            },
        };
    }
}
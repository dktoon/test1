Package("directives.lang")

.Format = {
	key : 'format',
	func : function($filter, $parse) {
		return {
			require : '?ngModel',
			link : function(scope, elem, attrs, ctrl) {
				if (!ctrl)
					return;
				var formatFromAttr = $parse(attrs.format)(scope.$parent);
				console.log(formatFromAttr)
				var init = function() {
					var limit = 2;
					formatFromAttr = $parse(attrs.format)(scope.$parent);
					if (formatFromAttr == 'currencyNoCents') {
						limit = 0;
						formatFromAttr = "currency";
					}
					
					var vars = {min:null};
					if (attrs.min) {
						var isNeg = false;
						if (attrs.min.indexOf("-") >=0 || attrs.min.indexOf("(") >= 0) {
							//negative
							isNeg = true;
						}
						var tmpNumber = attrs.min.replace(/[-\(\)%\$]/g,"");
						vars.min = Number((isNeg) ? "-"+tmpNumber : tmpNumber);
						if (vars.min == "NAN") {
							console.error("error "+attrs.min+" is not a valid number");
						}
					} 
					
					var format = {
							prefix : (formatFromAttr == 'currency'? '$': ''),
							suffix : (formatFromAttr == 'percent' || formatFromAttr == 'percentNoMultipy'? '%': ''),
							centsSeparator : '.',
							thousandsSeparator : ',',
							centsLimit: limit,
							allowNegative: true
						};
					if (formatFromAttr == 'year') {
						format.limit = 2;
						formatFromAttr = 'number';
					}
				}
				init();
//				scope.$watch(function() {return attrs.format;}, function() {init();})
				
				var formatText = function() {

					if (formatFromAttr == 'currency') {
						if (vars.min != null && ctrl.$modelValue < vars.min) {
							ctrl.$modelValue = vars.min;
						}
						
						if (formatFromAttr == 'currencyNoCents') {
							
							return $filter(formatFromAttr)(ctrl.$modelValue, '$', 0);
						} else
							return $filter(formatFromAttr)(ctrl.$modelValue, '$', 2);
					} else if (formatFromAttr == 'percent') {
						if (ctrl.$modelValue == '')
							return null;
						var value = parseFloat(ctrl.$modelValue) * 100;
						if (vars.min != null && value < vars.min) {
							value = vars.min;
						}
						
						if (value) {
							value = $filter('number')(value, 2) + " %";
							value = value.replace(".00", "");
							return value;
							
						}
						return "";
					} else if (formatFromAttr == 'percentNoMultiply') {
						if (ctrl.$modelValue == '')
							return null;
						var value = parseFloat(ctrl.$modelValue);
						if (vars.min != null && value < vars.min) {
							value = vars.min;
						}
						
						if (value) {
							value = $filter('number')(value, 3) + " %";
							value = value.replace(".000", "");
							return value;
							
						}
						return "";
					} else if (formatFromAttr == 'number') {

						if (vars.min != null && ctrl.$modelValue < vars.min) ctrl.$modelValue  = vars.min;
						
						return ctrl.$modelValue;
					}
					
				
				}
				ctrl.$formatters.unshift(function(a) {
					return formatText();
				});

				ctrl.$parsers.unshift(function(viewValue) {
					
					var res = parseFloat(elem[0].value.replace(/,/g, "").replace(/[$% ]/g, ""));
					if (isNaN(res))
						return null;
					if (formatFromAttr == 'currency') {
						if (elem[0].value.replace(/,/g, "").replace(/[$% ]/g, "") == '')
							return null;
						return res;
					} else if (formatFromAttr == 'percent') {
						if (elem[0].value.replace(/,/g, "").replace(/[$% ]/g, "") == '')
							return null;
						return res / 100;
					} else {
						return res;
					}
				});
				
				$(elem).blur(function() {
					
					elem.val(formatText());
				});
			}
		};
	}
}

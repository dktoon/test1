
Package("config").ConfigParser = function($parseProvider) {
	var oGet = $parseProvider.$get[$parseProvider.$get.length - 1];
	var GetModel = function(scope, exp) {

		var m = scope.$parent;
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
	$parseProvider.$get[$parseProvider.$get.length - 1] = 

			function($filter, $sniffer) {

				var exeOGet = oGet($filter, $sniffer);
				
				return function(exp) {
					var parsedExpression;
					
					if (typeof exp === 'string' && exp.startsWith('$parse(')) {
						var text = exp.substring('$parse('.length,
								exp.length - 1);

						parsedExpression = function(scope, locals) {
							var tks = exeOGet(text);
							var finalText = tks(scope);

							var m = GetModel(scope, finalText);

							return m.value;
						};
						
						parsedExpression.assign = function(scope, value) {
							var tks = exeOGet(text);
							var finalText = tks(scope);
							var m = GetModel(scope, finalText);
							if (m.parent != null) {
								m.parent[m.prop] = value;
							}
						};
						return parsedExpression;
					} else {
						return exeOGet(exp);
					}

				};
			};
};


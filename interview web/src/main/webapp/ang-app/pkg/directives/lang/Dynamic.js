

Package("directives.lang")
.Dynamic = {
	key: 'dynamic',
	
	func: function ($http, rootPath, $compile) {
		return {
			restrict : 'A',
			replace : true,
			link : function(scope, ele, attrs) {
				scope.$watch(attrs.dynamic, function(html) {
					ele.html(html);
					$compile(ele.contents())(scope);
				});
			}
		};
	}
}
Package("directives.lang")

.KeyListener = {
    key: 'keylistener',
    func: function ($parse, rootPath, $timeout) {
    	return {
    	    restrict: 'E',
    	    replace: true,
    	    scope: true,
    	    link:    function postLink(scope, iElement, iAttrs){
    	      jQuery(document).on('keydown', function(e){
    	    	  if (e.ctrlKey || e.altKey) {
    	    	         scope.$apply(scope.keyPressed(e));
    	    		  
    	    	  }
    	       });
    	    }
    	  };
    }
}
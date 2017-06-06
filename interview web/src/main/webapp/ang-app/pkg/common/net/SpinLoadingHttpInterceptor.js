Package("common.net")
.SpinLoadingHttpInterceptor = function ($q, $rootScope, $log) {
	Import("common.ui.Spinner");
    this.numLoadings = 0;
    this.spinner = new PKG.common.ui.Spinner();

    var self = this;
    return {
        request: function(config) {
        	

        	if (config.url.indexOf("/services/signup") >= 0 || config.url.indexOf("noSpin") >=0) {
//                console.log("Bypass loading");
                // Show loader
//        		self.numLoadings++;
//        		self.spinner.startSpin(false);
        	} else {
        		self.numLoadings++;
        		setTimeout(function(){
        			self.spinner.startSpin();
            	})
        	}
        		
            return config || $q.when(config);
        },
        response: function(response) {
        	var numLoadings = self.numLoadings;
        	self.numLoadings = Math.max(0, self.numLoadings - 1);
            if (numLoadings > 0 && self.numLoadings === 0) {
                // Hide loader
            	setTimeout(function(){
            		self.spinner.stopSpin();
            	})
                
            }

            return response || $q.when(response);

        },
        responseError: function(response) {
        	var numLoadings = self.numLoadings;
        	self.numLoadings = Math.max(0, self.numLoadings - 1);

        	if (numLoadings > 0 && self.numLoadings === 0) {
                // Hide loader
        		setTimeout(function(){
            		self.spinner.stopSpin();
            	})
            }

            return $q.reject(response);
        }
    };
};
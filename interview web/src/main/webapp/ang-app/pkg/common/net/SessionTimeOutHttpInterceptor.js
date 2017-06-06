Package("common.net")
    .SessionTimeOutHttpInterceptor = function ($q, $rootScope, $log) {
		Import("common.ui.Spinner");

        var self = this;
        return {
            request: function (config) {
                return config || $q.when(config);
            },
            response: function (response) {
                if (response.status >= 400 && response.data.error == "Session Time Out") {
                    window.location = "/index.html";

                }
                return response || $q.when(response);

            },
            responseError: function (response) {
                if (response.status >= 400 && response.data.error == "Session Time Out") {
                	window.location = "/index.html";

                }
                return $q.reject(response);
            }
        };
    };
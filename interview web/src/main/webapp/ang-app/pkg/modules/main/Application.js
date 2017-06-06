Import("modules.BaseModule");

Package("modules.main").Class({
    Application: PKG.modules.BaseModule.extend({
        GetModule: function (root) {
            return [{
            	factories:[
            	           "services.RoleService",
            	           "services.SessionService",
            	           "services.DataProviderService",
            	           "common.ui.NotifyService",
            	           "services.AuthenticationService",
            	           "services.ScreenFormatService",
            	           'services.HttpSessionInterceptor',
            	           'services.ConstantServices'
            	           ],
            	configs:[
                        "config.ConfigHttpProvider",
                        "config.ConfigHttpInterceptor",
                        "config.ConfigTheme",
                        "config.StateConfig",
                        "config.HttpSessionInterceptorConfig",
//                        "config.ConfigParser",
            	        ],
            	directives: [
    						
    						], // directives
            	controllers:[
						
						'controllers.ApplicationController',
						'controllers.login.LoginController',
						'controllers.login.ChangePasswordRequiredController',
						'controllers.login.ContactController',
						'controllers.login.VerifyEmailController',
						'controllers.login.ForgotPasswordController',
						'controllers.login.SignupController',
						
						'controllers.app.admin.AdminController',
						'controllers.app.admin.dashboard.DashboardController',

						
						], // controllers
				filters: [
				          'filters.Tel',
				          
				          ],
                others: [
//                         "/public/generatedJs/mainJs/" + new Date().getTime()
                     ],
                         
                        
        
            	},

                function (imports /*config, directives, controllers, others*/
                ) {
//            		ImportCssBulk(
//            		[], 'otherCss'
//            		);
            		var app = angular.module("application", 
            				[
            				 'ngMaterial', 
            				 'ngMessages',

            				 'internationalPhoneNumber', 
            				 'ui.router', 
            				 "ngCookies", 
            				 'ngSanitize', 
            				 ]);


            		app.constant('rootPath', root);

                	angular.forEach(imports.factories, function (factory) {
                		app.service(factory.key, factory.func);
                	});
                	angular.forEach(imports.configs, function (config) {
                    	app.config(config);
                	});
                	angular.forEach(imports.filters, function (filter) {
                		
                    	app.filter(filter.key, filter.func);
                	});
                	
                    /////////////////////
                    // Add Directives
                    /////////////////////
                	angular.forEach(imports.directives, function (directive) {
                		app.directive(directive.key, directive.func);
                	});
            


                	
                    /////////////////////
                    // The Controller for this module
                    /////////////////////
                	controllers = {}; // Fix Global Controller to pass on StateConfig
                	angular.forEach(imports.controllers, function (controller) {
                		controllers[controller.key] = controller;
                		app.controller(controller.key, controller.func);
                	});
                	

                    //////////////////////
                    // Run
                    /////////////////////
 
                    app.run( function run($http, $cookies, $rootScope, $location, authenticationService, roleService, sessionService, screenFormatService, $cookieStore, rootPath, $state){

                    	
                        $http.defaults.headers.post['X-CSRF-TOKEN'] = $cookies['X-CSRF-TOKEN'];
                        if ($location.url().indexOf("/login") >= 0) {
                        	$cookieStore.remove('authToken');
                        }
                        
                        var history = {
                        		backUrl : null
                        }
                        
                        $rootScope.$on('$locationChangeStart', function() {
                        	history.backUrl = history.currentUrl;
//                            history.push($location.$$path);
                        });
                        $rootScope.$on('$locationChangeSuccess',function(evt, absNewUrl, absOldUrl) {
                        	history.currentUrl = $location.url();
                        });
                        $rootScope.backUrl = function () {
                             return history.backUrl;
                          };
                        
                        
                        $rootScope.$on('$stateChangeStart', function (ev, to, toParams, from, fromParams) {
                        	if ($location.url().indexOf("/login/") == -1) {
                                if (!authenticationService.isLoggedIn()) {
                                	
                                	
                                	jQuery.ajax({
                				        url: rootPath + "security/loggedUser",
                				        success: function (data) {
                							if (!data.token) {
                								$cookieStore.remove('authToken');
                    				        	$location.path('/login/log');
                    				        	return;
                							}
                				        	authenticationService.login(data.user);
                            				$cookieStore.put('authToken', data.token.token);
                            				
                				        },
                				        error: function (xhr, ajaxOptions, thrownError) {
                				        	$cookieStore.remove('authToken');
                				        	$location.path('/login/log');
                				        },
                				        async: false
                				    });
                                	
                                	

                                } else if (!roleService.isAuthorized($location.url(), sessionService.currentUser)) {
                                  // redirect to error page
                                  $location.path('/error');
                                }
                        		
                        	}
                          });
                    	});
                    return app;
                }
            ];
        }

    })

});

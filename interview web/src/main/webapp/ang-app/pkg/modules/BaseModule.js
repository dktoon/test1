Package("modules").Class({
    BaseModule: Class.extend({
        init: function (root, async) {
            this.root = root;
            this.async = async;
        },
        GetModule: function() { return {
        	factories: [],
        	config:[],
        	directives: [], // directives
        	controllers:[], // controllers
        	filters: [],
            others: [],
        	}
        },
        initModule: function (ready) {
            this.isInit = true;
            var module = this.GetModule(this.root);
            if (module.length > 0)
                var modFunc = module[module.length - 1];
            var imports = {};
            this._initModule("factories", module[0], imports);
            this._initModule("configs", module[0], imports);
            this._initModule("directives", module[0], imports);
            this._initModule("controllers", module[0], imports);
            this._initModule("filters", module[0], imports);
            this._initModule("others", module[0], imports);
            
            var dfd_others = jQuery.Deferred();
            var dfd_bulk = jQuery.Deferred();
            var deferreds = [dfd_others.promise(), dfd_bulk.promise()];
            
        	ImportClassesJsAsyncBulk(
        			 {
        				factories: imports.factories,
        				configs: imports.configs,
        				directives: imports.directives,
        				controllers: imports.controllers,
        				filters: imports.filters
        			 }
        			, 
        			function(result) {
            			 imports.factories = result.factories;
            			 imports.configs = result.configs;
            			 imports.directives = result.directives;
            			 imports.controllers = result.controllers;
            			 imports.filters = result.filters;
            			dfd_bulk.resolve()
        			}, this.async);
            _ImportAsync(imports.others, function() {
            	imports.others = arguments;
            	dfd_others.resolve();
             });

        	
            $.when.apply($, deferreds).done(function() {
            	var app = modFunc(imports);
                if (ready)
                    ready(app);
            });
        	
        },
        _initModule: function (strName, pmodule, imports) {
            if (pmodule && pmodule[strName] && pmodule[strName].length > 0) {
            	var module = pmodule[strName];
                imports[strName] = [];
                $.each(module, function(key, m) {
                	imports[strName].push(m);
                });
            }
        }
    })
});

PKG.modules.BaseModule.isInit = [];
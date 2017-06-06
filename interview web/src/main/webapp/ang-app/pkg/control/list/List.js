Package("control.list").Class({
	List: Class.extend({
		init: function(config) {
			this.config = config;
			this.initRestMethod(this.config);
			/*
			  {
			  	http: $http
			  	view: edit/read
			  	
			  	onError: function(response, from, config) // From => create, update, delete
			  	onSuccess: function(response, from, config) // From => create, update, delete
			  	onCancel:function(model, config)
			  	editable: true/false
			  	rest: url,
			  	urlAddNew: 
			 	urlDelete: url if you want to override the rest
			 	urlGet: url if you want to override the rest
			  }
			 */
			if (!config.models) {
				this.get();
			}
		},
		initRestMethod: function(config) {
			if (!config.rest.endsWith("/")) {
				config.rest = config.rest + "/";
			}
			if (!config.urlDelete) {
				config.urlDelete = config.rest;
			}
			if (!config.urlGet) {
				config.urlGet = config.rest;
			}
			
			if (!config.urlDelete.endsWith("/")) {
				config.urlDelete = config.urlDelete + "/";
			}
			if (!config.urlGet.endsWith("/")) {
				config.urlGet = config.urlGet + "/";
			}
		},
		toggleView: function(){
			if (this.config.view == 'read') {
				this.config.view = 'edit';
			} else {
				this.config.view = 'read';
			}
		},
		get: function() {
			var _this = this;
			this.config.http.get(this.config.urlGet)
				.then(function(response) {
					_this.config.onSuccess(response, "get");
					_this.config.models = response.data;
				}, function(response) {
					_this.config.onError(response, "get");
				});
		},
		cancel: function() {
			console.log("Cancel on Form")
			this.config.onCancel(this.config.model, this.config);
		},
		delete: function(model) {
			var _this = this;
			console.log(model)
			this.config.http.delete(this.config.urlDelete + model.id)
				.then(function(response) {
						_this.config.onSuccess(response, "delete", _this.config);
						var idx = _.indexOf(_this.config.models, model);
						_this.config.models.splice(idx, 1);
					}, function(response) {
						_this.config.onError(response, "delete", _this.config);
					});
		},
	})
})
Package("control.form").Class({
	Form: Class.extend({
		init: function(config) {
			this.config = config;
			this.initRestMethod(this.config);
			/*
			  {
			  	http: $http
			  	isNew: true/false,
			  	view: edit/read
			  	onError: function(response, from, config) // From => create, update, delete
			  	onSuccess: function(response, from, config) // From => create, update, delete
			  	onCancel:function(model, config)
			  	editable: true/false
			  	rest: url,
			  	urlSave: url if you want to override the rest
			 	urlUpdate: url if you want to override the rest
			 	urlDelete: url if you want to override the rest
			 	urlGet: url if you want to override the rest
			  }
			 */
			if (config.needUndo === undefined)
				config.needUndo = true;
			if (!config.model) {
				this.get();
			} else {
				if (this.config.needUndo) {
					this.replaceMaster(config.model);
				}
			}
		},
		initRestMethod: function(config) {
			if (!config.rest.endsWith("/")) {
				config.rest = config.rest + "/";
			}
			if (!config.urlSave) {
				config.urlSave = config.rest;
			}
			
			if (!config.urlUpdate) {
				config.urlUpdate = config.rest;
			}
			if (!config.urlDelete) {
				config.urlDelete = config.rest;
			}
			if (!config.urlGet) {
				config.urlGet = config.rest;
			}
			
			if (!config.urlSave.endsWith("/")) {
				config.urlSave = config.urlSave + "/";
			}
			if (!config.urlUpdate.endsWith("/")) {
				config.urlUpdate = config.urlUpdate + "/";
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
		updateModel: function(model) {
			this.config.model = model;
			this.replaceMaster(model);
		},
		get: function() {
			var _this = this;
			this.config.http.get(this.config.urlGet + this.config.model.id)
				.then(function(response) {
					_this.config.onSuccess(response, "get");
					_this.config.model = response.data;
					_this.replaceMaster(response.data);
				}, function(response) {
					_this.config.onError(response, "get");
				});
		},
		save: function() {
			if (this.config.isNew) {
				this.create();
			} else {
				this.update();
			}
		},
		create: function() {
			var _this = this;
			this.config.http.put(this.config.urlSave, angular.toJson(this.config.model))
				.then(function(response) {
						_this.config.onSuccess(response, "create", _this.config);
						_this.config.model = response.data;
						_this.replaceMaster(response.data);
					}, function(response) {
						_this.config.onError(response, "create", _this.config);
					});
		},
		update: function() {
			var _this = this;
			this.config.http.post(this.config.urlUpdate, angular.toJson(this.config.model))
				.then(function(response) {
						_this.config.onSuccess(response, "update", _this.config);
						_this.config.model = response.data;
						_this.replaceMaster(response.data);
					}, function(response) {
						_this.config.onError(response, "update", _this.config);
					});
		},
		cancel: function() {
			if (this.config.needUndo) {
				this.config.model = PKG.tools.Clone.clone(this.modelMaster);
				this.config.onCancel(this.config.model, this.config);
			}
		},
		delete: function() {
			var _this = this;
			this.config.http.delete(this.config.urlDelete + this.config.model.id)
				.then(function(response) {
						_this.config.onSuccess(response, "delete", _this.config);
					}, function(response) {
						_this.config.onError(response, "delete", _this.config);
					});
		},
		replaceMaster: function(model) {
			if (this.config.needUndo) {
				this.modelMasterJson = angular.toJson(model);
				this.modelMaster = PKG.tools.Clone.clone(model);
			}
		},
		modelChanged: function() {
			if (this.config.needUndo) {
				return this.modelMasterJson != angular.toJson(this.config.model);
			}
		}
	})
})
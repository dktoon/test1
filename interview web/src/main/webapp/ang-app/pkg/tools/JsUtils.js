"use strict";
Import("tools.Clone");

Package("tools")
.JsUtils = {
	createObjectFromJSDL: function(clz) {
		var result = {};
		$.each(clz.properties, function(key, value) {
			if (value.isArray) {
				result[key] = [];
			} else if (value.isObject) {
				result[key] = {};
			} else {
				result[key] = null;
			}
		});
		return result;
	}
}
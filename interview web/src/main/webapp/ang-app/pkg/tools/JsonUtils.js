"use strict";

Package("tools")
.JsonUtils = {

    //regexIso8601: /^(\d{4}|\+\d{6})(?:-(\d{2})(?:-(\d{2})(?:T(\d{2}):(\d{2}):(\d{2})\.(\d{1,})(Z|([\-+])(\d{2}):(\d{2}))?)?)?)?$/,
    regexIso8601: /^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$/,
    toJsonCycle: function(obj) {
        return this._toJsonCycle([], null, null, obj);
    },
    _toJsonCycle: function(refs, parent, k, obj) {
        if (obj == null || obj instanceof Date || typeof (obj) != 'object')
            return obj;
        if (Array.isArray(obj)) {

            for (var i = 0; i < obj.length; i++) {
                this._toJsonCycle(refs, obj, i, obj[i]);
            }

            return obj;
        }

        if (!(obj.$id === undefined)) {
            refs[obj.$id] = obj;
            delete obj["$id"];
        }
        if (!(obj.$ref === undefined)) {
            parent[k] = refs[obj.$ref];
            return parent[k];
        } else {
            for (var key in obj) {
                if (obj.hasOwnProperty(key)) {
                    this._toJsonCycle(refs, obj, key, obj[key]);
                }

            }
            return obj;
        }

    },

    toJsonRef: function(obj) {
    	Import("tools.Clone");
        obj = this._toJsonRef([1], null, null, PKG.tools.Clone.clone(obj));

        return obj;
    },

    _toJsonRef: function(Id, parent, k, obj) {
        if (obj == null || obj instanceof Date || typeof (obj) != 'object')
            return obj;
        if (Array.isArray(obj)) {

            for (var i = 0; i < obj.length; i++) {
                this._toJsonRef(Id, obj, i, obj[i]);
            }

            return obj;
        }

        if (obj.$id === undefined) {
            obj.$id = Id[0] + "";
            Id[0]++;
            for (var key in obj) {
                if (obj.hasOwnProperty(key)) {
                    this._toJsonRef(Id, obj, key, obj[key]);
                }

            }
            return obj;
        } else {
            parent[k] = { $ref: obj.$id };
            return parent[k];
        }

    },

    convertDateStringsToDates: function(input) {
        // Ignore things that aren't objects.
        if (typeof input !== "object") return input;

        for (var key in input) {
            if (!input.hasOwnProperty(key)) continue;

            var value = input[key];
            var match;
            // Check for string properties which look like dates.
            if (typeof value === "string" && value.length > 10 && (match = value.match(this.regexIso8601))) {
                var milliseconds = Date.parse(match[0]);
                if (!isNaN(milliseconds)) {
                    input[key] = new Date(milliseconds);
                }
            } else if (typeof value === "object") {
                // Recurse into object
                this.convertDateStringsToDates(value);
            }
        }
    },
};


   
function ToJsonString(obj) {
    return _ToJsonString([], [1], obj);
}
function _ToJsonString(refs, Id, obj) {
    if (obj == null || obj instanceof Date || typeof (obj) != 'object')
        return JSON.stringify(obj);

    if (!(refs[obj] === undefined)) {
        return "{$ref:" + refs[obj] + "}";
    }
    var result = "";
    if (Array.isArray(obj)) {
        result = "[";

        for (var i = 0; i < obj.length; i++) {
            result += _ToJsonString(refs, Id, obj[i]) + (i < obj.length - 1 ? "," : "");
        }

        result += "]";
        return result;
    }

    refs[obj] = Id[0];


    result = "{";
    result += "$id:" + Id[0] + ",";

    Id[0]++;


    for (var key in obj) {
        if (obj.hasOwnProperty(key)) {
            result += "'" + key + "': " + _ToJsonString(refs, Id, obj[key]) + ",";
        }

    }

    result += "},";
    return result;
}

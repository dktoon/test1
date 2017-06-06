"use strict";
Package("tools")
.Clone = {
    clone: function(obj) {
        return this._clone([], [], obj);
    },

    _clone: function(refs, clones, obj) {
        if (obj == null || typeof (obj) != 'object')
            return obj;

        var temp = null;

        if (obj instanceof Date) {
            temp = new Date(obj.getTime());
        } else {
            var idx = refs.indexOf(obj);
            if (idx >= 0) {
                return clones[idx];
            } else {
                temp = obj.constructor(); // changed
                refs.push(obj);
                clones.push(temp);

            }

        }

        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                temp[key] = this._clone(refs, clones, obj[key]);
            }
        }
        return temp;
    }
};


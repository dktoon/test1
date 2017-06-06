"use strict";
var PKG = {
    root: "/ang-app/pkg/",
    pkg: "PKG",
    Class: function(clzObj) {
        for (var k in clzObj) {
            if (clzObj.hasOwnProperty(k)) {
                this[k] = clzObj[k];
                this[k].package = this.pkg;
                this[k].name = k;
                this[k].fullname = this.pkg + "." + k;

            }
        }
    } 
};
function Package(pkg) {
    var tokens = pkg.split(".");

    var pkgObj = PKG;
    var pkgStr = "PKG";
    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        pkgStr += "." + token;
        if (pkgObj[token] === undefined) {
            pkgObj[token] = {
                pkg: pkgStr,
                Class: function(clzObj) {
                    for (var k in clzObj) {
                        if (clzObj.hasOwnProperty(k)) {
                            this[k] = clzObj[k];
                            this[k]._package = this.pkg;
                            this[k]._name = k;
                            this[k]._fullname = this.pkg + "." + k;

                        }
                    }
                }
            }
        }
        pkgObj = pkgObj[token];

    }
    return pkgObj;
}
function refPkg(pkg) {
    var tokens = pkg.split(".");

    var pkgObj = PKG;
    var str = null;
    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        str += (str == null? "": ".") + token;
        if (pkgObj[token] === undefined) {
            
            return undefined;
        }
        pkgObj = pkgObj[token];

    }
    return pkgObj;
}

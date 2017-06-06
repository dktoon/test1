Package("system")
.Activator = {
    newInstance: function (clz, args) {
        var str = "var result = new " + clz._fullname + "(";
        if (args !== undefined && args != null) {
            for (var i = 0; i < args.length; i++) {
                str += '\'' + args[i] + '\'' + (i < args.length - 1 ? ", " : "");
            }
        }
        str += ");";
        eval(str);
        return result;
    },
    callFunc: function (func, args) {
        return func.apply(null, args);
    },
    STRIP_COMMENTS: /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg,
    ARGUMENT_NAMES: /([^\s,]+)/g,
    getParamNames: function(func) {
        var fnStr = func.toString().replace(STRIP_COMMENTS, '');
        var result = fnStr.slice(fnStr.indexOf('(') + 1, fnStr.indexOf(')')).match(ARGUMENT_NAMES);
        if (result === null)
            result = [];
        return result;
    },
}
var Activator = PKG.system.Activator;
"use strict";
var soon=(function(){var c=[];function b(){while(c.length){var d=c[0];d.f.apply(d.m,d.a);c.shift()}}var a=(function(){if(typeof MutationObserver!=="undefined"){var d=document.createElement("div");return function(e){var f=new MutationObserver(function(){f.disconnect();e()});f.observe(d,{attributes:true});d.setAttribute("a",0)}}if(typeof setImmediate!=="undefined"){return setImmediate}return function(e){setTimeout(e,0)}})();return function(d){c.push({f:d,a:[].slice.apply(arguments).splice(1),m:this});if(c.length==1){a(b)}}})();

function ImportJsToEval(url, callback) {
	
	$.getJSON(url, function(scripts) {
		$.each(scripts, function (idx, script) {
    		jQuery.globalEval(script);
		});
    	if (callback) {
    		callback();
    	}
	}).fail(function(xhr, ajaxOptions, thrownError) {
		console.log("Error loading " + data.pkg + "." + data.clz);
        console.log(xhr.status);
        console.log(thrownError);
	});
}

function _call(data, async) {
    if (data.isClass) {
        var aPackage = refPkg(data.pkg);
        if (aPackage === undefined || aPackage[data.clz] === undefined) {

            return jQuery.ajax({
                url: data.url,
                success: function (src) {
                    aPackage = refPkg(data.pkg);
                    data.clzObj = aPackage[data.clz];
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    console.log("Error loading " + data.pkg + "." + data.clz);
                    console.log(xhr.status);
                    console.log(thrownError);
                },
                async: async
            });

        } else {
            data.clzObj = aPackage[data.clz];
        }
        return null;
    } else {
    	return ImportJs(data.url, async);
    }
};
function _getDataFromString(fullname) {
    var isClass = !(fullname.indexOf("/") == 0 || fullname.indexOf("http") == 0);

    var clz = isClass ? fullname.substring(fullname.lastIndexOf(".") + 1) : null;
    var pkg = isClass ? fullname.substring(0, fullname.lastIndexOf(".")) : null;
    var url = !isClass ? fullname : PKG.root + pkg.replace(/\./g, "/") + "/" + clz + ".js";
    var data = {
        isClass: isClass,
        clz: clz,
        pkg: pkg,
        url: url
    };
    
    return data;
};
function ImportClassesJsAsyncBulk(classesList, ready, async) {
    
	var imports = {};
    var allDatas = [];
    for(var kl in classesList) {
    	var classes = classesList[kl];
    	var datas = [];
    	imports[kl] = datas;
    	
        for (var k in classes) {
            if (classes.hasOwnProperty(k)) {
                var fullname = classes[k];
                if (typeof fullname == "string") {
                    var data = _getDataFromString(fullname);
                    datas.push(data);
                    allDatas.push(data);
                	
                } else if ($.isArray(fullname)) {
                	var scripts = fullname;
                	$.each(scripts, function (idx, script) {
            			var data = _getDataFromString(script);
            			datas.push(data);
            			allDatas.push(data);
            		});
                }
            }
        }
    }
    $.ajax({
    		type: 'POST',
    		url: "/ImportClassesJsAsyncBulk", 
    		data: JSON.stringify({ type: 'js', datas: allDatas }),
    		async: async,
    		headers: { 
		        'Accept': '*/*',
		        'Content-Type': 'application/json' 
		    },
		    success: function(javascripts) {
		    	for (var jk in javascripts) {
		    		jQuery.globalEval(javascripts[jk]);
		    	}
		    	for (var kl in imports) {
		    		var datas = imports[kl];
			    	var args = [];
	    	    	for (var k in datas) {
	    	            if (datas.hasOwnProperty(k)) {
	    	            	
	    	            	if (datas[k].pkg != null) {
	    	                	var aPackage = refPkg(datas[k].pkg);
	    	                    if (aPackage[datas[k].clz]) {
	    	                        args.push(aPackage[datas[k].clz]);
	    	                    }
	    	            	} else {
	    	            		args.push(null);
	    	            	}
	    	            }
	    	    	}
	    	    	imports[kl] = args;
		    	}
    	    	if (ready) {
    	            ready(imports);
    	        }
    	    },
    });
}
function _doImportArraysScript(datas, scripts) {
	var dfd = jQuery.Deferred();
	soon(function() {
		$.each(scripts, function (idx, script) {
			var data = _getDataFromString(script);
			datas.push(data);
			_call(data, false);
		});
	    dfd.resolve();
	  });
	return dfd.promise();
}
function _ImportAsync(classes, ready) {
    var datas = [];
    var deferreds = [];
    for (var k in classes) {
        if (classes.hasOwnProperty(k)) {
            var fullname = classes[k];
            if (typeof fullname == "string") {
                
                var data = _getDataFromString(fullname);
                
                datas.push(data);
                var deferred = _call(data, true);
                if (deferred)
                	deferreds.push(deferred);
            	
            } else if ($.isArray(fullname)) {
            	var scripts = fullname;

            	deferreds.push(_doImportArraysScript(datas, scripts));
            }
        }
    }
    $.when.apply($, deferreds).done(function() {
        var args = [];
        for (var k in datas) {
            if (datas.hasOwnProperty(k)) {
            	
            	if (datas[k].pkg != null) {
                	var aPackage = refPkg(datas[k].pkg);
                    if (aPackage[datas[k].clz]) {
                    	
                        args.push(aPackage[datas[k].clz]);
                    }
            	} else {
            	}
            }
        }
        if (ready) {
            Import("system.Activator", false);
            Activator.callFunc(ready, args);
        }
    });
}
function ImportAsync(classes, ready) {
    setTimeout(function () {
        _ImportAsync(classes, ready);
    });
}
function ImportSync(classes, ready) {
    _ImportAsync(classes, ready);
}
function Import(fullname) {
    var clz = fullname.substring(fullname.lastIndexOf(".") + 1);
    var pkg = fullname.substring(0, fullname.lastIndexOf("."));
    var aPackage = refPkg(pkg);
    if (aPackage === undefined || aPackage[clz] === undefined) {
        jQuery.ajax({
            url: PKG.root + pkg.replace(/\./g, "/") + "/" + clz + ".js",
            success: function(src) {
            	jQuery.globalEval(src);
                aPackage = refPkg(pkg);
            },
            error: function(xhr, ajaxOptions, thrownError) {
                console.log(fullname);
                console.log(xhr.status);
                console.log(thrownError);
            },
            async: false
        });
    }
    return aPackage[clz];
}
function ImportCss(href) {
    var cssLink = $("<link rel='stylesheet' type='text/css' href='" + PKG.root + href + "'>");
    $("head").append(cssLink);
};

function ImportCssBulk(hrefs, id) {
	if (hrefs.length > 0) {
		$.ajax({
			type: 'POST',
			url: "/generateImportCssAsyncBulk?id=" + id, 
			data: JSON.stringify(hrefs),
			async: true,
			headers: { 
		        'Accept': '*/*',
		        'Content-Type': 'application/json' 
		    },
		    success: function(links) {
		    	links = JSON.parse(links);
		    	for (var k in links) {
		    		var href = links[k];
		    	    var cssLink = $("<link rel='stylesheet' type='text/css' href='" + PKG.root + href + "'>");
		    	    links.push(cssLink);
		    	}
		        $("head").append(links);
		    	
		    },
		});
		
	}
};

/**
 * function to load a given js file 
 */
function ImportJs(src, async) {
    var jsLink = $("<script type='text/javascript' src='" + (src.indexOf("/") == 0 || src.indexOf("http") == 0 ? src : PKG.root + src) + "'>");

    var url = (src.indexOf("/") == 0 || src.indexOf("http") == 0 ? src : PKG.root + src);
    return jQuery.ajax({
        url: url,
        success: function (js) {
        	jQuery.globalEval(js);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(url);
            console.log(xhr.status);
            console.log(thrownError);
        },
        async: async
    });
};

function ImportPromise($http, clz) {
	var aPackage = refPkg(clz);
	if (aPackage === undefined) {
		var d = _getDataFromString(clz);
		var dfd = jQuery.Deferred();

		
		$http.get(d.url).success(function(res) {
			jQuery.globalEval(res);
			dfd.resolve({data: refPkg(clz)});
			
		}).error(function (data, status, headers, config) {
        	if (status == 401) {
        		dfd.resolve({data: undefined, error: "Access Denied."});
        	} else {
        		dfd.resolve({data: undefined, error: data.message});
        	}
        	
        });
		return dfd.promise();
	}
	return {data: aPackage};
}
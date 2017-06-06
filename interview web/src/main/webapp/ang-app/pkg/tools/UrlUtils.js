Package("tools").Class({
    UrlUtils: Class.extend({
        init: function (url) {
            
            this.url = url;
            var a = $('<a>', { href: url})[0];
            var tokens = a.search.substring(1).split('&');
            
            this.params = [];
            for (var i = 0; i < tokens.length; i++) {
                var token = tokens[i].split("=");
                var key = token[0];
                var value = tokens[i].substring(key.length + 1);

                if (key != "")
                    this.params[key] = value;
            }
           
            this.hostname = a.hostname;
            this.pathname = a.pathname;
            this.hashParams = [];
            if (a.hash.indexOf('?') >= 0) {
                this.hash = a.hash.substring(0, a.hash.indexOf('?'));
                
                tokens = a.hash.substring(a.hash.indexOf('?') + 1).split('&');
                for (var i = 0; i < tokens.length; i++) {
                    var token = tokens[i].split("=");
                    var key = token[0];
                    var value = tokens[i].substring(key.length + 1);

                    if (key != "")
                        this.hashParams[key] = value;
                }
            } else {
                this.hash = a.hash;
            
            }
            this.search = a.search;

            this.PathNameQuery = function () {
                var q = this.pathname + "?";

                var idx = 0;
                var length = 0;
                var k;

                for (k in this.params) {
                    if (this.params.hasOwnProperty(k)) {
                        if (this.params[k] !== undefined && this.params[k] != null) {
                            length++;

                        }
                    }
                }

                for (k in this.params) {
                    if (this.params.hasOwnProperty(k)) {
                        if (this.params[k] !== undefined && this.params[k] != null) {

                            q += k + "=" + this.params[k];
                            if (idx < length - 1) {
                                q += "&";
                            }
                            idx++;
                        }
                    }
                }

                if (this.hash) {
                    q += this.hash + '?';
                    for (k in this.hashParams) {
                        if (this.hashParams.hasOwnProperty(k)) {
                            if (this.hashParams[k] !== undefined && this.hashParams[k] != null) {
                                length++;

                            }
                        }
                    }

                    for (k in this.hashParams) {
                        if (this.hashParams.hasOwnProperty(k)) {
                            if (this.hashParams[k] !== undefined && this.hashParams[k] != null) {
                                q += k + "=" + this.hashParams[k];
                                if (idx < length - 1) {
                                    q += "&";
                                }
                                idx++;
                            }
                        }
                    }
                }

                
                return q;
            };

            this.HashQuery = function () {
                var q = this.hash.substring(1) + '?';
                var idx = 0;
                var length = 0;
                var k;
                for (k in this.hashParams) {
                    if (this.hashParams.hasOwnProperty(k)) {
                        if (this.hashParams[k]) {
                            length++;

                        }
                    }
                }

                for (k in this.hashParams) {
                    if (this.hashParams[k]) {
                        if (this.hashParams.hasOwnProperty(k)) {
                            q += k + "=" + this.hashParams[k];
                            if (idx < length - 1) {
                                q += "&";
                            }
                            idx++;
                        }
                    }
                }
                return q;
            };
        }
    })
});
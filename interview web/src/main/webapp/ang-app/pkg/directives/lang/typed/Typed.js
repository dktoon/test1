Package("directives.lang.typed")

.Typed = {
	key : 'typed',
	func : function($filter, $compile, $parse, $timeout) {
		
		var typeSpeed = 0;
		var typeText = function(elem, str, pos, callback) {
			var humanize = Math.round(Math.random() * (100 - 30)) + typeSpeed;
			if (pos < str.length) {
				if (str.charAt(pos) === '^') {
					var substr = str.substr(pos);
					var charPause = 0;
					if (/^\^\d+/.test(substr)) {
                        substr = /\d+/.exec(substr)[0];
                        pos += substr.length + 1;
                        charPause = parseInt(substr);
                        humanize = charPause;
                    }
					
				}
				setTimeout(function() {
					elem.append(str[pos]);
					typeText(elem, str, pos+1, callback)
				}, humanize);
				
			} else {
				if (callback) {
					setTimeout(function() {
						callback();
					}, humanize);
					
				}
			}
		}
		var typeElem = function(elem, nodes, idx, $scope, directiveCallback) {
			
			if (idx < nodes.length) {
				var node = nodes[idx]
				if (node.nodeType == Node.TEXT_NODE) {
					var tnodes = $compile("<span>" + node.data + "</span>")($scope.$parent);
					$timeout(function() {
						var text = $(tnodes[0]).text();
						typeText(elem, text, 0, function() {
				    		typeElem(elem, nodes, idx + 1, $scope, directiveCallback)
				    	})
					});
					
			    	
    			} else if (node.nodeType == Node.ELEMENT_NODE) {
    				var e = angular.element(node.outerHTML);
    				elem.append(e);
    				typeElem(elem, nodes, idx + 1, $scope, directiveCallback)
    			}
				
			} else {
				var obj = null;
				var done = null;
				
				if ($(elem).attr("done")) {
					obj = $parse($(elem).attr("done"));
					var nodesOrigins = elem[0].childNodes;
					var nodes = [];
			    	_.each(nodesOrigins, function(node, k) {
			    		nodes.push(node);
			    		
			    	});
			    	

			    	var doneObj = obj($scope);
			    	
			    	if (doneObj) {
			    		
						doneObj.statusText = 'Done';
						
						if (doneObj.statusSpeech == 'Done' || !responsiveVoice.msgprofile) {
							doneObj.status = 'Done';
						}
					} else {
						doneObj = {statusText: 'Done', nodes: nodes}
						if (!responsiveVoice.msgprofile) {
							doneObj = {status: 'Done', nodes: nodes}
							
						}
					}
			    	obj.assign($scope, doneObj);
					
					
				}
//				var contents = $compile(elem.contents())($scope);
			
				if (directiveCallback) {
					
					directiveCallback();
				}
				$scope.$apply();
			}
		}
		return {
			link: function($scope, elem, attrs) {
				if (attrs.typed == 'disabled') {
					if ($(elem).attr("done")) {
						obj = $parse($(elem).attr("done"));

				    	var doneObj = obj($scope);
				    	
				    	if (doneObj) {
				    		
							doneObj.statusText = 'Done';
				    	} else {
							doneObj = {status: 'Done'};
						}
				    	obj.assign($scope, doneObj);	
					}
					if (attrs.callback) {
						var callback = attrs.callback; 
						if (!callback.endsWith(")")) {
							callback += "()";
						}
						$parse(callback)($scope);
					}
					return;
				}
				$scope.$parent.captureNumber = function(str) {
					var numberPattern = /\d+/g;

					return str.match( numberPattern )
				}
				$scope.$parent.toSeparateWords = function(n) {
					  var a = ['zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine'];
					  var str = n + "";
					  var res = "";
					  for (var i = 0, len = str.length; i < len; i++) {
						  var idx = parseInt(str[i]);
						  res += " " + a[idx];
					  }
					  return res;
				}
				$scope.$parent.toWords = function(n) {
					  var a = ['', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine', 'ten', 'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen', 'seventeen', 'eighteen', 'nineteen'];
					  var b = ['', '', 'twenty', 'thirty', 'fourty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'];
					  var g = ['', 'thousand', 'million', 'billion', 'trillion', 'quadrillion', 'quintillion', 'sextillion', 'septillion', 'octillion', 'nonillion'];
					  var grp = function grp(n) {
					    return ('000' + n).substr(-3);
					  };
					  var rem = function rem(n) {
					    return n.substr(0, n.length - 3);
					  };
					  var fmt = function fmt(_ref) {
					    var h = _ref[0];
					    var t = _ref[1];
					    var o = _ref[2];

					    return [Number(h) === 0 ? '' : a[h] + ' hundred ', Number(o) === 0 ? b[t] : b[t] && b[t] + '-' || '', a[t + o] || a[o]].join('');
					  };
					  var cons = function cons(xs) {
					    return function (x) {
					      return function (g) {
					        return x ? [x, g && ' ' + g || '', ' ', xs].join('') : xs;
					      };
					    };
					  };
					  var iter = function iter(str) {
					    return function (i) {
					      return function (x) {
					        return function (r) {
					          if (x === '000' && r.length === 0) return str;
					          return iter(cons(str)(fmt(x))(g[i]))(i + 1)(grp(r))(rem(r));
					        };
					      };
					    };
					  };
					  return iter('')(0)(grp(String(n)))(rem(String(n)));
					};
				var nodesOrigins = elem[0].childNodes;
				var nodes = [];
		    	var done = null;
				if (attrs.done) {
					done = $parse(attrs.done)($scope.$parent);
				}
				
				var directiveCallback = null;
				if (attrs.callback) {
					directiveCallback = function() {
						var callback = attrs.callback; 
						if (!callback.endsWith(")")) {
							callback += "()";
						}
						$parse(callback)($scope);
					};
					
				}
				
				if (done && done.status == 'Done') {
					$timeout(function() {
						elem.empty();
						var nodes = [];
				    	_.each(done.nodes, function(node, k) {
				    		elem.append(node)
				    	});
				    	$compile(elem.contents())($scope.$parent);
						
					});
				} else {
					var tnodes = $compile("<div>" + elem.html() + "</div>")($scope.$parent);
					$timeout(function() {
						var text = $(tnodes[0]).text().replace(/[^]\d+/g, '');
						var done = $parse($(elem).attr("done"));
						var doneObj = done($scope);
						/*
						 * var u = new SpeechSynthesisUtterance();
						     u.text = 'I am Susan Storm, your virtual loan assistant.';
						     u.lang = 'en-US';
						     u.rate = 0.9;
						     u.onend = function(event) { console.log('Speech complete'); }
						     speechSynthesis.speak(u);
						 */
						
						responsiveVoice.speak(text, 'US English Female', {
							onend: function() {
								if ($(elem).attr("done")) {
									var doneObj = done($scope);
									if (doneObj) {
										doneObj.statusSpeech = 'Done';
										if (doneObj.statusText == 'Done') {
											doneObj.status = 'Done';
										}
									} else {
										doneObj = {statusSpeech: 'Done'}
									}
									done.assign($scope, doneObj);
							    	$scope.$apply();
									
								}
				    		}
						});
					});
					
					
			    	_.each(nodesOrigins, function(node, k) {
			    		nodes.push(node);
			    		
//			    		if (node.nodeType == Node.TEXT_NODE) {
//			    			var newNodes = node.data.split("^wait");
//			    			_.each(newNodes, function(newNode) {
//								nodes.push({nodeType: Node.TEXT_NODE, data: newNode.data});
//			    			});
//												    	
//		    			} else {
//		    				nodes.push(node);
//		    			}
			    	});
			    	elem.empty();
			    	typeElem(elem, nodes, 0, $scope, directiveCallback)
				}
		    }
		};
	}
};

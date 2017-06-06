Package("services").
ScreenFormatService = {
	key: 'screenFormatService',
	func: function(rootPath) {

		return {
			getCurrentScreenFormat: function () {
				
				if (navigator.userAgent.match(/(Android.*Mobile|webOS|iPhone|iPod|BlackBerry|Windows Phone)/gi)) {
					//Phone
					if (screen.height > screen.width) return "MOBILE_PORT";
					else return "MOBILE_LAND";
				} 
				else if (navigator.userAgent.match(/(Android, iPad)/gi)) {
					//Tablet
					if (screen.height > screen.width) return "TABLET_PORT";
					else return "TABLET_LAND";
				} else {
					return "DESKTOP";
				}
			},
			isMobile:function(){
				return navigator.userAgent.match(/(Android.*Mobile|webOS|iPhone|iPod|BlackBerry|Windows Phone)/gi)!= null;
			},
			isIos:function(){
				return navigator.userAgent.match(/(iPhone|iPod)/gi)!= null;
			},
			isIe:function(){
				 var rv = false;
				  if (navigator.appName == 'Microsoft Internet Explorer')
				  {
				    var ua = navigator.userAgent;
				    var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
				    if (re.exec(ua) != null)
				      rv = parseFloat( RegExp.$1 );
				  }
				  else if (navigator.appName == 'Netscape')
				  {
				    var ua = navigator.userAgent;
				    var re  = new RegExp("Trident/.*rv:([0-9]{1,}[\.0-9]{0,})");
				    if (re.exec(ua) != null)
				      rv = parseFloat( RegExp.$1 );
				  }
				  return rv;
			}
			
		};

	}
};


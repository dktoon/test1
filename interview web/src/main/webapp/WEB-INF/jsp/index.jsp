<!DOCTYPE html>
<%@page import="com.cre8techlabs.main.security.TokenUtils"%>
<%@page import="java.util.Date"%>
<html>
<head>
	<title>Interview</title>
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	
	<link rel="shortcut icon" href="\images\logo.ico">
 	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
 	<link href="http://fonts.googleapis.com/css?family=Satisfy" rel="stylesheet">
 	<link href="/webjars/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
 	
	<link href="/public/generatedCss/mainCss/<%= TokenUtils.TIME%>" rel="stylesheet">
	
	<script type="text/javascript" src="/public/generatedJs/indexJs/<%= TokenUtils.TIME%>"></script>

 	<script type="text/javascript" src="/ang-app/pkg/special/path/routes/Routes/<%= TokenUtils.TIME%>"></script>
	
    <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places"></script>
    
    <script type="text/javascript" src="/ang-app/pkg/modules/main/Application.js"></script>
    
    
	<script>
		var onFocus = null;
		if (navigator.userAgent.match(/(iPhone|iPod)/gi)!= null) {
			 onFocus = function(input){
				setTimeout(function(){input.selectionStart = 0;input.selectionEnd=999;});
			}
		} else {
			 onFocus = function(input){$(input).select();}
		}
	</script>
	
  
    <meta name="viewport" content="initial-scale=1, user-scalable=no"> 
</head>
<body ng-controller="controllers.ApplicationController" ng-cloak>

	<div id="Application" style="display: none" >
		<div ui-view id="main-container" class="pkg_main-container" >
		</div>
	</div>
	
	<script>
	ImportJsToEval("/public/generatedJsToEval/deferJs/<%= TokenUtils.TIME%>", function() {
		new PKG.modules.main.Application('/', true).initModule(function (app) {
		    angular.bootstrap(document.getElementsByTagName("body"), ['application']);
		    $("#Application").css("display", "block");
		});
		
	}, true);
		
	</script>

</body>
</html>
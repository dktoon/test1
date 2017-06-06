
Package("config").ConfigTheme = function($mdThemingProvider) {
	
	console.log("Config Theme");
	
	
	var customGreen = {
			'50' : '#ffffff',//'#E8F5E9',
			'100' : '#ffffff',//'#C8E6C9',
			'200' : '#A5D6A7',
			'300' : '#81C784',
			'400' : '#66BB6A',
			'500' : '#4CAF50',
			'600' : '#43A047',
			'700' : '#388E3C',
			'800' : '#2E7D32',
			'900' : '#000000',//'#1B5E20',
			'A100' : '#43A047',//'#ffffff',//'#B9F6CA',
			'A200' : '#69F0AE',
			'A400' : '#00E676',
			'A700' : '#00C853',
			'contrastDarkColors': ['600','900','A200', 'A400', 'A700', '700','800']
	};

	 $mdThemingProvider
        .definePalette('cgreen', 
        		customGreen);
	
	var customRed = {
			'50' : '#ffffff',//'#FFEBEE',
			'100' : '#ffffff',//#FFCDD2',
			'200' : '#EF9A9A',
			'300' : '#E57373',
			'400' : '#EF5350',
			'500' : '#F44336',
			'600' : '#E53935',
			'700' : '#D32F2F',
			'800' : '#C62828',
			'900' : '#000000',//'#B71C1C',
			'A100' : '#E53935',// '#ffffff',//'#FF8A80',
			'A200' : '#FF5252',
			'A400' : '#FF1744',
			'A700' : '#D50000',
			'contrastDarkColors': ['600','900','A200', 'A400', 'A700', '700','800']
	}

	 $mdThemingProvider
        .definePalette('cred', 
        		customRed);
	
	//039BE5//039BE5
	var customBlue = {
			'50':'#ffffff',
			'100':'#ffffff',//'#BBDEFB',
			'200':'#90CAF9',
			'300':'#64B5F6',
			'400': '#29b6f6',
	        '500': '#03a9f4',	
	        '600': '#039be5',//this
	        '700': '#0288d1',
	        '800': '#0277bd',
	        '900': '#000000',//shade
	        'A100': '#ffffff',//'#039be5',//'#80d8ff',//'#ffffff',//'#80d8ff',
	        'A200': '#40c4ff',
	        'A400': '#00b0ff',
	        'A700': '#0091ea',
	        'contrastDefaultColor':'dark',
			'contrastDarkColors': '600 900 A200 A400 A700 700 800 A100'
	}
	 $mdThemingProvider
        .definePalette('cblue', 
        		customBlue);
	$mdThemingProvider
			.theme('default')
			.primaryPalette("blue")
			.accentPalette('red')
			.warnPalette('orange');

	
	$mdThemingProvider
			.theme('market-pricing')
			.primaryPalette("blue")
			.accentPalette('red')
			.warnPalette('orange')

	
	$mdThemingProvider
			.theme('private-pricing')
			.primaryPalette("red")
			.accentPalette('purple')
			.warnPalette('orange')

	$mdThemingProvider
			.theme('test-pricing')
			.primaryPalette("green")
			.accentPalette('purple')
			.warnPalette('orange')

	
	$mdThemingProvider.setDefaultTheme('default');
	$mdThemingProvider.alwaysWatchTheme(true);
};


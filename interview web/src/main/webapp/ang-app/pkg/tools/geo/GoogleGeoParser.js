"use strict";
Package("tools.geo")
.GoogleGeoParser = {
	isType: function (arr, name) {
		for (var i in arr) {
			if (arr[i] == name)
				return true;
		}
		return false;
	},
	getAddressInfo: function(address_components, name) {
		for (var i in address_components) {
			var c = address_components[i];
			if (this.isType(c.types, name)) {
				return c.short_name;
			}
		}
		return null;
	},

	
	parseAddressCitystatezip: function(result) {
		var address_components = result.address_components;
		var street_number = "street_number";
		var street = "route";
		var city = "locality";
		var state = "administrative_area_level_1";
		var country = "country";
		var county = "administrative_area_level_2";
		var zip = "postal_code";
		return {
			
			address: this.getAddressInfo(address_components, street_number) + " " + this.getAddressInfo(address_components, street),
			citystatezip: this.getAddressInfo(address_components, city) + "," + this.getAddressInfo(address_components, state) + "," + this.getAddressInfo(address_components, zip),
			city: this.getAddressInfo(address_components, city),
			state: this.getAddressInfo(address_components, state),
			zip: this.getAddressInfo(address_components, zip),
			county: this.getAddressInfo(address_components, county),
			country: this.getAddressInfo(address_components, country),
			longitude: address_components && address_components.geometry && address_components.geometry.location && address_components.geometry.location.lng? address_components.geometry.location.lng: null,
			latitude: address_components && address_components.geometry && address_components.geometry.location && address_components.geometry.location.lat? address_components.geometry.location.lat: null
			
			
		}
		
	},
	fillAddress: function (result, address) {
		var res = this.parseAddressCitystatezip(result);
		address.street = res.address;
		address.city = res.city;
		address.zip = res.zip;
		address.state = res.state;
		address.country = res.country;
		address.latitude = res.latitude;
		address.longitude = res.longitude;
	}
    
};


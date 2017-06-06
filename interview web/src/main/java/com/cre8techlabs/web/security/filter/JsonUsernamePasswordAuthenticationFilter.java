/*************************************************************************
 * 
 * Cre8Tech Labs CONFIDENTIAL
 * __________________
 * 
 *  [2015] - [2015] Cre8Tech Labs 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Cre8Tech Labs and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Cre8Tech Labs
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Cre8Tech Labs.
 */
package com.cre8techlabs.web.security.filter;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cre8techlabs.web.rest.RestConst;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUsernamePasswordAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter implements RestConst {
	
/*
{
    "query_status" : {
        "query_status_code" : "OK",
        "query_status_description" : "Query successfully performed."
    },
    "ip_address" : "104.62.28.32",
    "geolocation_data" : {
        "continent_code" : "NA",
        "continent_name" : "North America",
        "country_code_iso3166alpha2" : "US",
        "country_code_iso3166alpha3" : "USA",
        "country_code_iso3166numeric" : "840",
        "country_code_fips10-4" : "US",
        "country_name" : "United States",
        "region_code" : "CA",
        "region_name" : "California",
        "city" : "Pasadena",
        "postal_code" : "91101",
        "metro_code" : "803",
        "area_code" : "626",
        "latitude" : 34.147,
        "longitude" : -118.1392,
        "isp" : "AT&T U-verse",
        "organization" : "AT&T U-verse"
    }
}
 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SimpleGeoContainer {
		GeolocationData geolocation_data;

		public GeolocationData getGeolocation_data() {
			return geolocation_data;
		}

		public void setGeolocation_data(GeolocationData geolocation_data) {
			this.geolocation_data = geolocation_data;
		}
	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeolocationData {
        String continent_code;
        String continent_name;
        String country_code_iso3166alpha2;
        String country_code_iso3166alpha3;
        String country_code_iso3166numeric;
//        String country_code_fips10-4;
        String country_name;
        String region_code;
        String region_name;
        String city;
        String postal_code;
        String metro_code;
        String area_code;
        String latitude;
        String longitude;
        String isp;
        String organization;
		public String getContinent_code() {
			return continent_code;
		}
		public void setContinent_code(String continent_code) {
			this.continent_code = continent_code;
		}
		public String getContinent_name() {
			return continent_name;
		}
		public void setContinent_name(String continent_name) {
			this.continent_name = continent_name;
		}
		public String getCountry_code_iso3166alpha2() {
			return country_code_iso3166alpha2;
		}
		public void setCountry_code_iso3166alpha2(String country_code_iso3166alpha2) {
			this.country_code_iso3166alpha2 = country_code_iso3166alpha2;
		}
		public String getCountry_code_iso3166alpha3() {
			return country_code_iso3166alpha3;
		}
		public void setCountry_code_iso3166alpha3(String country_code_iso3166alpha3) {
			this.country_code_iso3166alpha3 = country_code_iso3166alpha3;
		}
		public String getCountry_code_iso3166numeric() {
			return country_code_iso3166numeric;
		}
		public void setCountry_code_iso3166numeric(String country_code_iso3166numeric) {
			this.country_code_iso3166numeric = country_code_iso3166numeric;
		}
		public String getCountry_name() {
			return country_name;
		}
		public void setCountry_name(String country_name) {
			this.country_name = country_name;
		}
		public String getRegion_code() {
			return region_code;
		}
		public void setRegion_code(String region_code) {
			this.region_code = region_code;
		}
		public String getRegion_name() {
			return region_name;
		}
		public void setRegion_name(String region_name) {
			this.region_name = region_name;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getPostal_code() {
			return postal_code;
		}
		public void setPostal_code(String postal_code) {
			this.postal_code = postal_code;
		}
		public String getMetro_code() {
			return metro_code;
		}
		public void setMetro_code(String metro_code) {
			this.metro_code = metro_code;
		}
		public String getArea_code() {
			return area_code;
		}
		public void setArea_code(String area_code) {
			this.area_code = area_code;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getIsp() {
			return isp;
		}
		public void setIsp(String isp) {
			this.isp = isp;
		}
		public String getOrganization() {
			return organization;
		}
		public void setOrganization(String organization) {
			this.organization = organization;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class User {
		String username;
		String password;
		SimpleGeoContainer geolocation;
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			if (username != null) this.username = username.toLowerCase();
			else
				this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public SimpleGeoContainer getGeolocation() {
			return geolocation;
		}

		public void setGeolocation(SimpleGeoContainer geolocation) {
			this.geolocation = geolocation;
		}

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
	        HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		if (request.getHeader("Content-Type").contains(CONTENT_TYPE_APPLICATION_JSON)) {
            try {
                StringBuffer sb = new StringBuffer();
                String line = null;

                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }

                //json transformation
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(sb.toString(), User.class);

        		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        		// Allow subclasses to set the "details" property
        		setDetails(request, authRequest);
        		request.getSession().setAttribute("GeoData", user.getGeolocation());
        		return this.getAuthenticationManager().authenticate(authRequest);

            } catch (Exception e) {
            	throw new BadCredentialsException(e.getMessage());
            }
            
            
        }
        throw new IllegalStateException("Accept only JSON authen");
	}
}

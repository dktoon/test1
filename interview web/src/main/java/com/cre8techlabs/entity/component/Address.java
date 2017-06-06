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
package com.cre8techlabs.entity.component;

public class Address implements Component {
	protected Double longitude;
	protected Double latitude;
	protected String censustract = "";
	
	protected String street = "";
	protected String streetCont = "";
	protected String city = "";
	protected String zip = "91101";
	protected String zipExt = "";
	protected String state = "CA";
	protected String province = "";
	protected String county = "";
	protected String countyName = "";
	protected String country = "US";

	
	

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetCont() {
		return streetCont;
	}
	public void setStreetCont(String streetCont) {
		this.streetCont = streetCont;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getCensustract() {
		return censustract;
	}
	public void setCensustract(String censustract) {
		this.censustract = censustract;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((censustract == null) ? 0 : censustract.hashCode());
	    result = prime * result + ((city == null) ? 0 : city.hashCode());
	    result = prime * result + ((country == null) ? 0 : country.hashCode());
	    result = prime * result + ((county == null) ? 0 : county.hashCode());
	    result = prime * result
	            + ((countyName == null) ? 0 : countyName.hashCode());
	    result = prime * result
	            + ((latitude == null) ? 0 : latitude.hashCode());
	    result = prime * result
	            + ((longitude == null) ? 0 : longitude.hashCode());
	    result = prime * result
	            + ((province == null) ? 0 : province.hashCode());
	    result = prime * result + ((state == null) ? 0 : state.hashCode());
	    result = prime * result + ((street == null) ? 0 : street.hashCode());
	    result = prime * result
	            + ((streetCont == null) ? 0 : streetCont.hashCode());
	    result = prime * result + ((zip == null) ? 0 : zip.hashCode());
	    return result;
    }
	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    Address other = (Address) obj;
	    if (censustract == null) {
		    if (other.censustract != null)
			    return false;
	    } else if (!censustract.equals(other.censustract))
		    return false;
	    if (city == null) {
		    if (other.city != null)
			    return false;
	    } else if (!city.equals(other.city))
		    return false;
	    if (country == null) {
		    if (other.country != null)
			    return false;
	    } else if (!country.equals(other.country))
		    return false;
	    if (county == null) {
		    if (other.county != null)
			    return false;
	    } else if (!county.equals(other.county))
		    return false;
	    if (countyName == null) {
		    if (other.countyName != null)
			    return false;
	    } else if (!countyName.equals(other.countyName))
		    return false;
	    if (latitude == null) {
		    if (other.latitude != null)
			    return false;
	    } else if (!latitude.equals(other.latitude))
		    return false;
	    if (longitude == null) {
		    if (other.longitude != null)
			    return false;
	    } else if (!longitude.equals(other.longitude))
		    return false;
	    if (province == null) {
		    if (other.province != null)
			    return false;
	    } else if (!province.equals(other.province))
		    return false;
	    if (state == null) {
		    if (other.state != null)
			    return false;
	    } else if (!state.equals(other.state))
		    return false;
	    if (street == null) {
		    if (other.street != null)
			    return false;
	    } else if (!street.equals(other.street))
		    return false;
	    if (streetCont == null) {
		    if (other.streetCont != null)
			    return false;
	    } else if (!streetCont.equals(other.streetCont))
		    return false;
	    if (zip == null) {
		    if (other.zip != null)
			    return false;
	    } else if (!zip.equals(other.zip))
		    return false;
	    return true;
    }
    public String toStringFormatted() {
    	String previous = "";
    	previous += (isEmpty(street)? street: "");
    	previous += (!isEmpty(previous) && !(isEmpty(streetCont) || isEmpty(city) || isEmpty(zip) || isEmpty(state))? "": ", ");
    	
    	previous += (isEmpty(streetCont)? streetCont: "");
    	previous += (!isEmpty(previous) && !(isEmpty(city) || isEmpty(zip) || isEmpty(state))? "": ", ");
    	
    	previous += (isEmpty(city)? city: ""); 
    	previous += (!isEmpty(previous) && !(isEmpty(zip) || isEmpty(state))? "": ", ");
    	
    	previous += (isEmpty(zip)? zip: "");
    	previous += (!isEmpty(previous) && !(isEmpty(state))? "": ", ");
    	
    	previous += (isEmpty(state)? state: "");
    	
    	return previous;
    }
    private boolean isEmpty(String str) {
    	return str == null || str.length() == 0;
    }
	public String getZipExt() {
		return zipExt;
	}
	public void setZipExt(String zipExt) {
		this.zipExt = zipExt;
	}
}

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

public class ContactDetails implements Component {
	protected String email;
	protected String phone;
	protected String fax;
	protected String mobile;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((email == null) ? 0 : email.hashCode());
	    result = prime * result + ((fax == null) ? 0 : fax.hashCode());
	    result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
	    result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
	    ContactDetails other = (ContactDetails) obj;
	    if (email == null) {
		    if (other.email != null)
			    return false;
	    } else if (!email.equals(other.email))
		    return false;
	    if (fax == null) {
		    if (other.fax != null)
			    return false;
	    } else if (!fax.equals(other.fax))
		    return false;
	    if (mobile == null) {
		    if (other.mobile != null)
			    return false;
	    } else if (!mobile.equals(other.mobile))
		    return false;
	    if (phone == null) {
		    if (other.phone != null)
			    return false;
	    } else if (!phone.equals(other.phone))
		    return false;
	    return true;
    }
}

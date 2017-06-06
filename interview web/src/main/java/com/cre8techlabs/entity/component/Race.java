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

import com.cre8techlabs.entity.EnumCodeDesc;

public enum Race implements EnumCodeDesc {
	
//	Hispanic_or_latino("Hispanic or latino"),
	American_Indian_or_Alaskan_Native("American Indian or Alaskan Native"),
	Asian("Asian"),
	Black_or_African_American("Black or African American"),
	Native_Hawaiian_or_Other_Pacific_Islander("Native Hawaiian or Other Pacific Islander"),
	White("White");
	
	
	private String description;
	Race(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}

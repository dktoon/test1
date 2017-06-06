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
package com.cre8techlabs.entity.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cre8techlabs.entity.EnumCodeDesc;
import com.cre8techlabs.tools.StringTools;

public enum UserRole implements EnumCodeDesc {
	Admin("Super Admin")

	
	
	;
	
	
	private String description;
	UserRole() {
		this.description = "";
	}
	UserRole(String description) {
		this.description = description;
	}

	public String getDescription() {
		if (description == null || description.isEmpty()) {
			return StringTools.unCamel(toString());
		}
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public UserRole[] and(UserRole role) {
		return new UserRole[] {this, role};
	}
	public UserRole[] and(UserRole...roles) {
		List<UserRole> result = new ArrayList<UserRole>();
		result.add(this);
		result.addAll(Arrays.asList(roles));
		return result.toArray(new UserRole[result.size()]);
	}
}

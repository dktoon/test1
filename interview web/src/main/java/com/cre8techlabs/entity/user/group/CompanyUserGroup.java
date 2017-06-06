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
package com.cre8techlabs.entity.user.group;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;


public class CompanyUserGroup extends AbstractUserGroup {
	
	public CompanyUserGroup() {
	}
	
	@Indexed
	private Date start = new Date();
	@Indexed
	private Date end;
	
	
	@Indexed
	private ObjectId companyId;
	private boolean canViewAllTemplates = false;
	
	private Set<ObjectId> templateIds = new HashSet<ObjectId>();
	
	public ObjectId getCompanyId() {
		return companyId;
	}

	public void setCompanyId(ObjectId companyId) {
		this.companyId = companyId;
	}


	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public boolean isCanViewAllTemplates() {
		return canViewAllTemplates;
	}

	public void setCanViewAllTemplates(boolean canViewAllTemplates) {
		this.canViewAllTemplates = canViewAllTemplates;
	}

	public Set<ObjectId> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(Set<ObjectId> templateIds) {
		this.templateIds = templateIds;
	}

}

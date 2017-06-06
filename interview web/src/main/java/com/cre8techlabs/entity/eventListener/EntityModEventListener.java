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
package com.cre8techlabs.entity.eventListener;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cre8techlabs.entity.EntityMod;
import com.cre8techlabs.entity.user.AbstractUser;
import com.mongodb.DBObject;

public class EntityModEventListener extends
		AbstractMongoEventListener<EntityMod> {
	@Override
	public void onBeforeSave(EntityMod e, DBObject dbo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth == null? "System": ((AbstractUser) auth.getPrincipal()).getUsername();
		Date date = new Date();
		if (e.getCreationDate() == null) {
			
			dbo.put("creationDate", date);
			e.setCreationDate(date);
		}
		if (e.getCreatedBy() == null) {
			dbo.put("createdBy", user);
			e.setCreatedBy(user);
		}
		
		dbo.put("modifiedDate", date);
		dbo.put("modifiedBy", user);
		e.setModifiedDate(date);
		e.setModifiedBy(user);
	}
}

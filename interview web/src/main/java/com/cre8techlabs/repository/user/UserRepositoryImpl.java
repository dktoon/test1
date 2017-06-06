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
package com.cre8techlabs.repository.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.cre8techlabs.entity.user.AbstractUser;

public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired(required=true)
	public MongoOperations mongoOps;
	
	@Override
	public AbstractUser findByOAuth(String oauthType, String id) {
		Query q = new Query(Criteria.where("openAuths." + oauthType + "._id").is(id));
		
		return mongoOps.findOne(q, AbstractUser.class);
	}

}

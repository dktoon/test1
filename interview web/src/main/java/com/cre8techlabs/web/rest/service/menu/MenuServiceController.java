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
package com.cre8techlabs.web.rest.service.menu;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.AdminUser;
import com.cre8techlabs.entity.user.CompanyUser;

@RestController
@RequestMapping("services/menu")
public class MenuServiceController {
	static final String currentUserId = "{userId}";
	static final String adminId = "{adminId}";
	static final String companyId = "{companyId}";

	@Autowired
	MenusConfig menusConfig;
	
	@RequestMapping("/main")
	public Menu getMenu() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AbstractUser user = (AbstractUser) auth.getPrincipal();
		Map<String, String> map = new HashMap<String, String>();
		map.put(currentUserId, user.getId().toString());
		if (user instanceof CompanyUser) {
			CompanyUser usr = (CompanyUser) user;
			map.put(companyId, usr.getCompany().getId().toString());
		}
		
		if (user instanceof AdminUser) {
			map.put(adminId, user.getId().toString());
			return menusConfig.getAdminMenu().clone(map, user);
		}
		
		return null;
	}
}

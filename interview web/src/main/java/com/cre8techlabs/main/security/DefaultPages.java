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
package com.cre8techlabs.main.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cre8techlabs.entity.user.AbstractUser;
import com.cre8techlabs.entity.user.AdminUser;
import com.cre8techlabs.web.security.Route;
import com.cre8techlabs.web.security.RouteMap;

@Component
public class DefaultPages {
	
	
	@Autowired
	RouteMap routeMap;
	
	public String getDefaultRouteUrl(AbstractUser user, boolean isMobile) {
		if (user instanceof AdminUser) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("adminId", user.getId().toString());
			return routeMap.getRouteMapDashboard().adminDashboard.path(params);
		}			
		return "";
	}
	
	public Route getDefaultRoute(AbstractUser user) {
		if (user instanceof AdminUser) {
			return routeMap.getRouteMapDashboard().adminDashboard;
		}
		return null;
	}

}

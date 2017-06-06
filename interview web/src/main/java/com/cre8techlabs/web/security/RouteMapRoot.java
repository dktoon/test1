package com.cre8techlabs.web.security;

import org.springframework.stereotype.Component;

import com.cre8techlabs.entity.user.UserRole;

@Component
public class RouteMapRoot extends AbstractRouteMap<Route> {

	/*****
	 * Admin
	 */
	public Route admin = new Route("admin", "admin/{adminId}", "controllers.app.admin.AdminController", "ang-app/view/app/admin/admin.html", all_screen_format, UserRole.Admin);
	
	
	public RouteMapRoot() {
	}

}

package com.cre8techlabs.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cre8techlabs.entity.user.UserRole;

@Component
public class RouteMapDashboard extends AbstractRouteMap<Route> {
	@Autowired
	RouteMapRoot routeMapRoot;

	/**
	 * Admin
	 */
	public Route adminDashboard;
	
	
	
	@Override
	protected void init() {
		adminDashboard = new Route(routeMapRoot.admin, "dashboard", "dashboard", "controllers.app.admin.dashboard.DashboardController", "ang-app/view/app/admin/dashboard/dashboard.html", all_screen_format, UserRole.Admin);

	}
}

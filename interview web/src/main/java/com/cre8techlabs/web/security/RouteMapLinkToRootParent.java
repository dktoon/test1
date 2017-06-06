package com.cre8techlabs.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cre8techlabs.entity.user.UserRole;

@Component
public class RouteMapLinkToRootParent extends AbstractRouteMap<Route> {
	@Autowired
	RouteMapRoot routeMapRoot;
	
	public void init() {

	}
}

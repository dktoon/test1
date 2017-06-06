package com.cre8techlabs.web.rest.service.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cre8techlabs.web.security.RouteMap;

@Component
public class MenusConfig {

	@Autowired
	private RouteMap routeMap;
	
	private Menu adminMenu;
	private Menu companyMenu;
	private Menu companyAdminPerspectiveMenu;
	
	public Menu getAdminMenu() {
		if (adminMenu == null)
			adminMenu = new Menu(
				new MenuItem[] {
						new MenuItem("Home", "fa fa-home", "#/admin/{userId}/dashboard", false),
				}
			);
		return adminMenu;
	}
	
	

}

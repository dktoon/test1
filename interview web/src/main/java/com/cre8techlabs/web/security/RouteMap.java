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
package com.cre8techlabs.web.security;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.map.LazyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cre8techlabs.entity.user.UserRole;

@Component
public class RouteMap extends AbstractRouteMap<Route> {

	@Autowired
	RouteMapRoot routeMapRoot;
	
	@Autowired
	RouteMapLogin routeMapLogin;
	
	
	@Autowired
	RouteMapDashboard routeMapDashboard;
	
	@Autowired
	RouteMapLinkToRootParent routeMapLinkToRootParent;
	///////////////////
	
	//
	
	/********
	 * 
	 */

	private Map<UserRole, List<Route>> routeMapByRole;
	private Collection<Route> routes;

	public Map<UserRole, List<Route>> routeMapByRole() {
		if (routeMapByRole == null) {
			routeMapByRole = buildMap();
		}
		return routeMapByRole;
	}
	public Collection<Route> routes() {
		if (routes == null) {
			routes = buildRoutes();

		}
		return routes;
	}

	
	protected Map<UserRole, List<Route>> buildMap() {
		Map<UserRole, List<Route>> map = new HashMap<UserRole, List<Route>>();
		Factory factory = new Factory() {
		     public Object create() {
		         return new ArrayList<Route>();
		     }
		 };
		 map = LazyMap.decorate(map, factory);
		 Collection<Route> routes = buildRoutes();
		 for (UserRole userRole : UserRole.values()) {
			 List<Route> list = map.get(userRole);
			 for (Route route : routes) {
				if (route.getRoles() != null && route.getRoles().contains(userRole)) {
					addRouteToList(list, route);
				}
			}
		}
		 
	    return map;
    }
	private Collection<Route> buildRoutes() {
		Set<Route> res = new HashSet<>();
		parseRoutes(res, routeMapRoot.admin);
		
		parseRoutes(res, routeMapLogin.login);
		
		
		return res;
	}
	private static void parseRoutes(Set<Route> routes, Route route) {
		routes.add(route);
		for (Route r : route.getChilds()) {
			parseRoutes(routes, (Route) r);
		}
	}


	static FileFilter controllerFilter = new FileFilter() {
		@Override
        public boolean accept(File file) {
	        return file.isDirectory() || file.getName().contains("Controller.js");
        }
	};
	public static void listAllControllers(File dir) {
		for (File file : dir.listFiles(controllerFilter)) {
	        if (file.isDirectory()) {
	        	listAllControllers(file);
	        } else {
	        	String str = (file.getAbsolutePath().replaceAll("/Users/lknhiayi/Desktop/works/workspace/cre8tech-labs/lenderprice-web/src/main/webapp/ang-app/pkg/", "").replaceAll("[.]js", "").replaceAll("/", "."));
	        	
	        }
        }
		
	}
	public void generate(String[] args) throws IOException {
		File root = new File("//Users/lknhiayi/Desktop/works/workspace/esigndoc/esigndoc-web/src/main/webapp");
		for (Route route : routes()) {
			createHtml(root, route);
			createControllers(root, route);
		}
    }
	
	protected static void createControllers(File root, Route route) throws IOException {
		root = new File(root, "ang-app/pkg");
		if (route.getController() != null) {
			String routeController = route.getController();
			File ctrl = new File(root, routeController.replaceAll("[.]", "/") + ".js");
			if (!ctrl.exists()) {
				ctrl.getParentFile().mkdirs();
				ctrl.createNewFile();
			
				String packageStr = routeController.substring(0, routeController.lastIndexOf('.'));
				String controllerStr = routeController.substring(routeController.lastIndexOf('.') + 1);
				
				String template = "\n"
						+ "Package('%s')" + "\n"
						+ ".%s = " + "\n"
						+ "{" + "\n"
						+ "    key: '%s'," + "\n"
						+ "    resolve: {" + "\n"
						+ "" + "\n"
						+ "    }," + "\n"
						+ "    func: function($scope, $http, $stateParams, rootPath) {" + "\n"
						+ "" + "\n"
						+ "    }" + "\n"
						+ "}";
				
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(ctrl));
				bw.write(String.format(template, packageStr, controllerStr, routeController));
				bw.close();
			}
		}
    }


	protected static void createHtml(File root, Route route) throws IOException {
		String templateUrl = route.getTemplateUrl();
		Map<String, String> views = route.getViews();
		if (templateUrl != null) {
	        File html = new File(root, templateUrl);
			if (!html.exists()) {
				html.getParentFile().mkdirs();
				html.createNewFile();
			} else {
				if (html.isDirectory()) {
					html.delete();
					html.getParentFile().mkdirs();
					html.createNewFile();
				}
			}
		} else if (views != null) {
			for (String filePath : views.values()) {
				File html = new File(root, filePath);
				if (!html.exists()) {
					html.getParentFile().mkdirs();
					html.createNewFile();
				} else {
					if (html.isDirectory()) {
						html.delete();
						html.getParentFile().mkdirs();
						html.createNewFile();
					}
				}
            }
		}
    
	}
	
	boolean isInit;
	
	@PostConstruct
	@Override
	protected synchronized void init() {
		if (!isInit) {
			isInit = true;



			
			
			// Important init last
			routeMapRoot.init();
			routeMapLogin.init();
			routeMapDashboard.init();
			// Import this one is the last
			routeMapLinkToRootParent.init();
		}

		
	}
	public RouteMapRoot getRouteMapRoot() {
		return routeMapRoot;
	}
	public void setRouteMapRoot(RouteMapRoot routeMapRoot) {
		this.routeMapRoot = routeMapRoot;
	}
	public RouteMapLogin getRouteMapLogin() {
		return routeMapLogin;
	}
	public void setRouteMapLogin(RouteMapLogin routeMapLogin) {
		this.routeMapLogin = routeMapLogin;
	}
	public RouteMapDashboard getRouteMapDashboard() {
		return routeMapDashboard;
	}
	public void setRouteMapDashboard(RouteMapDashboard routeMapDashboard) {
		this.routeMapDashboard = routeMapDashboard;
	}
	
}

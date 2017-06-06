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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.cre8techlabs.web.security.AbstractRoute.ScreenFormat;
import com.cre8techlabs.web.security.Route;
import com.cre8techlabs.web.security.RouteMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuItem {

	private String link;
	private String icon;
	private String label;
	private boolean evalExp;
	private Set<ScreenFormat> authorizedScreenFormat;// = new HashSet<ScreenFormat>();
	
	private Route route;
	
	public MenuItem() {
    }

	public MenuItem(String label, String icon, Route route, boolean evalExp, Collection<ScreenFormat> override) {
		this.label = label;
		this.icon = icon;
		this.link = "#/" + route.getUrlWithParent();
		this.evalExp = evalExp;
		this.route = route;
		
		if (override!= null) 
			authorizedScreenFormat = new HashSet<ScreenFormat>(override);
		else
			authorizedScreenFormat = route.getAuthorizedFormat();
	}
	
	public MenuItem(String label, String icon, Route route, boolean evalExp, String params) {
		this.label = label;
		this.icon = icon;
		this.link = "#/" + route.getUrlWithParent() + params;
		this.evalExp = evalExp;
		this.route = route;
		
		authorizedScreenFormat = route.getAuthorizedFormat();
	}
	public MenuItem(String label, String icon, Route route, boolean evalExp) {
		this.label = label;
		this.icon = icon;
		this.link = "#/" + route.getUrlWithParent();
		this.evalExp = evalExp;
		this.route = route;
		
		authorizedScreenFormat = route.getAuthorizedFormat();
	}
	public MenuItem(String label, String icon, String link, boolean evalExp) {
		this.label = label;
		this.icon = icon;
		this.link = link;
		this.evalExp = evalExp;
		authorizedScreenFormat = new HashSet<ScreenFormat>(RouteMap.all_screen_format);
    }

	public MenuItem(String label, String icon, Route route, String link, boolean evalExp) {
		this.label = label;
		this.icon = icon;
		this.link = link;
		this.evalExp = evalExp;
		this.route = route;
		authorizedScreenFormat = route.getAuthorizedFormat();
    }

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isEvalExp() {
		return evalExp;
	}

	public void setEvalExp(boolean evalExp) {
		this.evalExp = evalExp;
	}
	public MenuItem clone() {
		MenuItem res = new MenuItem(label, icon, link, evalExp);
		res.setRoute(route);
		res.setAuthorizedScreenFormat(authorizedScreenFormat);
		return res;
	}
	@JsonIgnore
	public Route getRoute() {
		return route;
	}
	@JsonProperty
	public void setRoute(Route route) {
		this.route = route;
	}

	public Set<ScreenFormat> getAuthorizedScreenFormat() {
		return authorizedScreenFormat;
	}

	public void setAuthorizedScreenFormat(Set<ScreenFormat> authorizedScreenFormat) {
		this.authorizedScreenFormat = authorizedScreenFormat;
	}
}

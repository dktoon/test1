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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cre8techlabs.entity.user.UserRole;
import com.cre8techlabs.utils.reflection.EntityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Route extends AbstractRoute<Route> {

	protected Set<UserRole> roles = new HashSet<UserRole>();
	
	public Route() {
    
	}
	public Route(
			Route parent,
			String state,
			String url,
			String controller,
			String templateUrl,
			Map<String, String> views,
			List<ScreenFormat> screenFormat,
			UserRole ... roles
			) {
		setParent(parent);
		if (parent != null) {
			this.parent.childs.add(this);
			for (Route r : this.childs) {
	            r.parent = parent;
            }
		}
		setState(state);
		setUrl(url);
		setTemplateUrl(templateUrl);

		setController(controller);
		setViews(views);
		if (screenFormat == null)
			this.authorizedFormat.addAll(Arrays.asList(ScreenFormat.values()));
		else
			this.authorizedFormat.addAll(screenFormat);
		this.roles.addAll(Arrays.asList(roles));
    }
	public Route(
			String state,
			String url,
			String controller,
			String templateUrl,
			Map<String, String> views,
			List<ScreenFormat> screenFormat
			) {
		this(null, state, url, controller, templateUrl, views, screenFormat);
    }
	
	public Route(
			String state,
			String url,
			String controller,
			String templateUrl,
			List<ScreenFormat> screenFormat,
			UserRole ... roles
			) {
		this(null, state, url, controller, templateUrl, null, screenFormat, roles);
	}
	public Route(
			Route parent,
			String state,
			String url,
			String controller,
			String templateUrl,
			List<ScreenFormat> screenFormat,
			UserRole ... roles
			) {
		this(parent, state, url, controller, templateUrl, null, screenFormat, roles);
	}
	
	

	public boolean intersect(Collection<UserRole> roles) {
		return !Collections.disjoint(roles, this.roles);
	}

	public Route cloneNewParentToClonedChild(Route route, UserRole ...roles) {
		Route _this = EntityUtils.clone(this);
	    route.childs.add(_this);
	    _this.parent = route;
	    if (roles.length > 0)
	    	_this.changeRole(new HashSet<UserRole>(Arrays.asList(roles)));
	    
		return _this;
    }
	public Route setNewParentToClonedChild(Route parent) {
		super.setNewParentToClonedChild(parent);
		parent.changeRole(parent.roles);
		return parent;
	}
	void changeRole(Set<UserRole> roles) {
		this.roles = roles;
		for (AbstractRoute child : getChilds()) {
			if (child instanceof Route) {
				((Route)child).changeRole(roles);
			}
        }
	}

	
	@JsonIgnore
	public Set<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((getStateWithParent() == null) ? 0 : getStateWithParent().hashCode());
	    return result;
    }
	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    Route other = (Route) obj;
	    if (url == null) {
		    if (other.getStateWithParent() != null)
			    return false;
	    } else if (!getStateWithParent().equals(other.getStateWithParent()))
		    return false;
	    return true;
    }
	@Override
	public String toString() {
		return "Route [state=" + state + ", url=" + url + ", controller="
		        + controller + ", authorizedFormat=" + authorizedFormat
		        + ", roles=" + roles + "]";
	}
	
	
	

}

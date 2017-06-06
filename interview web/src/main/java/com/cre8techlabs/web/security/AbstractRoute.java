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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cre8techlabs.utils.reflection.EntityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AbstractRoute<T extends AbstractRoute> implements Serializable {

	static final String idReg = "[0-9a-zA-Z]+";
	static final String idRate = "[0-9a-zA-Z\\.]+";
	static final String toBeReplace = "\\{[0-9a-zA-Z]+}";
	
	
	protected T parent;
	protected List<T> childs = new ArrayList<T>();
	protected String state;
	protected String url;
	protected String controller;
	protected String templateUrl;
	protected Map<String, String> views;
	protected Set<ScreenFormat> authorizedFormat = new HashSet<ScreenFormat>();
	
	
	public static enum ScreenFormat{
		MOBILE_PORT,
		MOBILE_LAND,
		TABLET_PORT,
		TABLET_LAND,
		DESKTOP
	}
	
	public AbstractRoute() {
    
	}
	public AbstractRoute(
			T parent,
			String state,
			String url,
			String controller,
			String templateUrl,
			Map<String, String> views,
			List<ScreenFormat> screenFormat
			) {
		this.parent = parent;
		if (parent != null) {
			this.parent.childs.add(this);
			for (T r : this.childs) {
	            r.parent = parent;
            }
		}
		this.state = state;
		this.url = url;
		this.controller = controller;
		this.templateUrl = templateUrl;
		this.views = views;
		if (screenFormat == null)
			this.authorizedFormat.addAll(Arrays.asList(ScreenFormat.values()));
		else
			this.authorizedFormat.addAll(screenFormat);
    }
	public AbstractRoute(
			String state,
			String url,
			String controller,
			String templateUrl,
			Map<String, String> views,
			List<ScreenFormat> screenFormat
			) {
		this(null, state, url, controller, templateUrl, views, screenFormat);
    }
	
	public AbstractRoute(
			String state,
			String url,
			String controller,
			String templateUrl,
			List<ScreenFormat> screenFormat
			) {
		this(null, state, url, controller, templateUrl, null, screenFormat);
	}
	public AbstractRoute(
			T parent,
			String state,
			String url,
			String controller,
			String templateUrl,
			List<ScreenFormat> screenFormat
			) {
		this(parent, state, url, controller, templateUrl, null, screenFormat);
	}
	
	

	public T cloneNewParentToClonedChild(T route) {
		T _this = (T) EntityUtils.clone(this);
	    route.childs.add(_this);
	    _this.parent = route;
	    
		return _this;
    }
	public T setNewParentToClonedChild(T parent) {
		T _this = (T) EntityUtils.clone(this);
		parent.childs = _this.childs;
		for (Object obj : _this.getChilds()) {
			T child = (T) obj;
			child.parent = parent;
        }
		return parent;
	}

	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateWithParent() {
		String stateWithParent = state;
		T parent = this.parent;
		while (parent != null) {
			stateWithParent = parent.getState() + "." + stateWithParent;
			parent = (T) parent.parent;
		}
		
		return stateWithParent;
	}
	public void setStateWithParent(String state) {
		// Intentionally left blank
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	public Map<String, String> getViews() {
		return views;
	}
	public void setViews(Map<String, String> views) {
		this.views = views;
	}


	public String getJsUrlReg() {
		String jsUrlReg = url;
		
		T parent = this.parent;
		
		while (parent != null) {
			jsUrlReg = parent.getUrl() + "/" + jsUrlReg;
			parent = (T) parent.parent;
		}
		
		
		return jsUrlReg.replaceAll("\\{rate}", idRate).replaceAll("\\{[0-9a-zA-Z]+}", idReg);
		
	}

	public String getUrlWithParent() {
		String jsUrlReg = url;
		
		T parent = this.parent;
		
		while (parent != null) {
			jsUrlReg = parent.getUrl() + "/" + jsUrlReg;
			parent = (T) parent.parent;
		}
		
		
		return jsUrlReg;
		
	}
	public T getRouteByState(T... routes) {
		return getRouteByState(0, routes);
	}

	protected T getRouteByState(int idx, T... routes) {
		for (T child : childs) {
	        if (child.getState().equals(routes[idx].getState())) {
				if (idx < routes.length - 1) {
		        	idx++;
					return (T) child.getRouteByState(idx, routes);
					
				} else {
					return child;
				}
	        }
        }
		
		return null;
		
	}
	public String path(Map<String, String> params) {
		String route = getUrlWithParent();
		if (params != null && !params.isEmpty()) {
		    for (Entry<String, String> e : params.entrySet()) {
		    	route = route.replace("{" + e.getKey() + "}", e.getValue());
	        }
			
		}
	    return route;
    }
	public Set<ScreenFormat> getAuthorizedFormat() {
		return authorizedFormat;
	}
	public void setAuthorizedFormat(Set<ScreenFormat> authorizedFormat) {
		this.authorizedFormat = authorizedFormat;
	}
	
	
	@JsonIgnore
	public T getParent() {
		return parent;
	}
	public void setParent(T parent) {
		this.parent = parent;
	}
	
	@JsonIgnore
	public List<T> getChilds() {
		return childs;
	}
	public void setChilds(List<T> childs) {
		this.childs = childs;
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
	    T other = (T) obj;
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
		        + controller + ", authorizedFormat=" + authorizedFormat + "]";
	}
	Map<String, String> defaultParams = new HashMap<>();
	
	public T setDefaultParam(String key, String value) {
		defaultParams.put(key, value);
		return (T) this;
	}
	public Map<String, String> getDefaultParams() {
		return defaultParams;
	}
	public void setDefaultParams(Map<String, String> defaultParams) {
		this.defaultParams = defaultParams;
	}

}

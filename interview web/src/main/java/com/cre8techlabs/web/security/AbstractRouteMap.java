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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.cre8techlabs.web.security.AbstractRoute.ScreenFormat;

public class AbstractRouteMap<T extends AbstractRoute<T>> {

	protected void init() {
		
	}
	public static final List<ScreenFormat> all_screen_format = Collections.unmodifiableList(Arrays.asList(ScreenFormat.values()));
	public static final List<ScreenFormat> desktop_tablet_format = Collections.unmodifiableList(
			Arrays.asList(
					ScreenFormat.DESKTOP, 
					ScreenFormat.TABLET_PORT, 
					ScreenFormat.TABLET_LAND)); 
	

	public static final List<ScreenFormat> mobile_format = 
			Collections.unmodifiableList(
					Arrays.asList(
							ScreenFormat.MOBILE_PORT, 
							ScreenFormat.MOBILE_LAND)); 
			
			
	
	protected static <T extends AbstractRoute<T>> void addRouteToList(Collection<T> list, T parent) {
		list.add(parent);
		for (T route : parent.getChilds()) {
	        addRouteToList(list, route);
        }
	}
	
	protected static <T extends AbstractRoute<T>> List<T> buildRoutes(Class clz) {
		try {
			List<T> routes = new ArrayList<T>();
			Field[] declaredFields = clz.getDeclaredFields();
			for (Field field : declaredFields) {
			    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
			    	if (field.getName().startsWith("shared"))
			    		continue;
			    	if (field.getType().isAssignableFrom(AbstractRoute.class)) {
				    	Object obj = field.get(null);
				    	T r = (T) obj;
			    		
			    		if (r.getParent() == null){
			    			routes.add(r);
			    		}

			    		
			    	}
			    }
			}
			List<T> results = new ArrayList<T>();
			for (T route : routes) {
	            results.add(route);
	            addChilds(route, results);
            }
			return results;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}
	private static <T extends AbstractRoute<T>> void addChilds(T route, List<T> results) {
	    for (T child : route.getChilds()) {
	    	results.add(child);
	    	addChilds(child, results);
        }
	    
    }
}

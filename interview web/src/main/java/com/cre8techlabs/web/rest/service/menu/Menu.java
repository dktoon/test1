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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cre8techlabs.entity.user.AbstractUser;

public class Menu extends MenuItem {
	private List<MenuItem> items = new ArrayList<MenuItem>();

	public Menu() {
    }
	public Menu(MenuItem[] menuItems) {
		items.addAll(Arrays.asList(menuItems));
	}

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}
	public Menu clone() {
		Menu clone = new Menu();
		for (MenuItem menuItem : items) {
	        clone.items.add(menuItem.clone());
        }
		return clone;
	}
	public Menu clone(Map<String, String> map, AbstractUser user) {
		Menu clone = new Menu();
		for (MenuItem menuItem : items) {
			boolean granted = true;
			if (menuItem.getRoute() != null) {
				if (!menuItem.getRoute().intersect(user.getRoles())) {
					granted = false;
				}
			}
			if (granted) {
				MenuItem i = menuItem.clone();
				boolean isImadeIt = true;
				for (Entry<String, String> e : map.entrySet()) {
					if (e.getValue() == null && i.getLink().contains(e.getKey())) {
						isImadeIt = false;
					} else if (e.getValue() != null) {
						i.setLink(i.getLink().replace(e.getKey(), e.getValue()));
												
					}
				}
				if (isImadeIt)
					clone.items.add(i);
	    				
			}
		}
		return clone;
    }
}

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
package com.cre8techlabs.utils.reflection;

import java.util.HashMap;
import java.util.Map;

class Reference extends HashMap<Object, Map<Class, Object>> {
	public Object getCopyValue(Object src, Class targetClass) {
		Map<Class, Object> map = get(src);
		if (map == null)
			return null;
		Object res = map.get(targetClass);
		if (res != null)
			return res;
		for (Class clz : map.keySet()) {
			if (targetClass.isAssignableFrom(clz)) {
				return map.get(clz);
			}
		}
		return null;
	}
	
	public boolean containsCopyValue(Object src, Class targetClass) {
		Map<Class, Object> map = get(src);
		if (map == null)
			return false;
		Object res = map.get(targetClass);
		if (res != null)
			return true;
		for (Class clz : map.keySet()) {
			if (targetClass.isAssignableFrom(clz)) {
				return true;
			}
		}
		return false;
	}
	
	public void setCopyValue(Object src, Class targetClass, Object dst) {
		Map<Class, Object> map = get(src);
		if (map == null) {
			map = new HashMap<Class, Object>();
			put(src, map);
		}
		if (!map.containsKey(targetClass)) {
			map.put(targetClass, dst);
		}
	}
}

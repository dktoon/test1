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
import java.util.HashSet;
import java.util.Set;

public class Parsed extends HashMap<Object, Set<Class>> {
	public boolean isParsed(Object obj, Class dstClass) {
		
		Set<Class> set = get(obj);
		if (set == null)
			return false;
		return set.contains(dstClass);
	}
	public void setParsed(Object obj, Class dstClass) {
		
		Set<Class> set = get(obj);
		if (set == null) {
			set = new HashSet<Class>();
			put(obj, set);
		}
		if (!set.contains(dstClass)) 
			set.add(dstClass);
	}
}

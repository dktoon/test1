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
package com.cre8techlabs.web.rest;

import org.springframework.util.Assert;

public class Range {

	private Integer firstResult = 0;
	private Integer maxResults = 0;

	public Range(String range) {
		String[] parsed = range.split("-");
		Assert.isTrue(parsed.length == 2,
				"Range header in an unexpected format.");
		this.firstResult = new Integer(parsed[0]);
		Integer end = new Integer(parsed[1]);
		this.maxResults = end - firstResult + 1;
	}

	public Integer getFirstResult() {
		return this.firstResult;
	}

	public Integer getMaxResults() {
		return this.maxResults;
	}
	
	
}
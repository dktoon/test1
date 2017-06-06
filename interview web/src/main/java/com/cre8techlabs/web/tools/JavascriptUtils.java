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
package com.cre8techlabs.web.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cre8techlabs.web.rest.RestConst;

@RestController
public class JavascriptUtils implements RestConst {
	@Autowired
	@Qualifier("cacheJs")
	protected String cacheJs;
	
	protected List<String> cache = new ArrayList<String>();
	
	@RequestMapping(value="/ImportClassesJsAsyncBulk", method = RequestMethod.POST, headers = ACCEPT_JSON)
	public List<String> importClassesJsAsyncBulk(HttpServletRequest request, HttpServletResponse response, @RequestBody JSPackageDatas datas) throws IOException {
		if (datas.datas.isEmpty())
			return Collections.EMPTY_LIST; 
		
		List<String> js = cacheJs.equals("local")? new ArrayList<String>(): cache;
		if (js.isEmpty()) {
			for (JSPackageData data : datas.getDatas()) {
				String file = request.getServletContext().getRealPath(data.getUrl());
				File f = new File(file);
				if (f.exists()) {
					FileInputStream in = new FileInputStream(f);
					js.add(IOUtils.toString(in));
					in.close();
				} else {
					System.out.println(data.url);
					InputStream in = Class.class.getResourceAsStream("/META-INF/resources" + data.url);
					
					js.add(IOUtils.toString(in));
					in.close();
				}
			}
			
		}
		return js;
	}

}

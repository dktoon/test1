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
package com.cre8techlabs.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:config.properties")
public class AppConfig {
	
	
	@Value("${cacheJs}")
	private String cacheJs;
	
	@Value("${wkhtmltopdf}")
	private String wkhtmltopdf;

	@Value("${nocache}")
	private String nocache;

	@Bean(name="wkhtmltopdf")
	public String getWkhtmltopdf() {
		return wkhtmltopdf;
	}
	@Bean(name="cacheJs")
	public String getCacheJs() {
		return cacheJs;
	}
	
	@Bean(name="nocache")
	public String nocache() {
		return nocache;
	}


}

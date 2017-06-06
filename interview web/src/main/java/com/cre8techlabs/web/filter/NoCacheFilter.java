package com.cre8techlabs.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter {
	long cacheTimeout = Long.MAX_VALUE;
	private boolean noCache;

	public NoCacheFilter(boolean noCache) {
		this.noCache = noCache;
	}

	public void doFilter(ServletRequest srequest, ServletResponse sresponse,
	        FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		/*
		 * Then we just set the appropriate headers and invoke the next filter
		 * in the chain:
		 */
		if (noCache) {
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "No-cache");
			
		}
		chain.doFilter(request, response);
		/*
		 * this method calls other filters in the order they are written in
		 * web.xml
		 */
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
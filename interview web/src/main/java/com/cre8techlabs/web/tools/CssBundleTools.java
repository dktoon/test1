package com.cre8techlabs.web.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cre8techlabs.main.security.TokenUtils;
import com.cre8techlabs.web.rest.RestConst;
import com.cre8techlabs.web.security.RouteMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CssBundleTools implements RestConst {
	
	public static enum ResourceType {
		  CSS {
		    @Override
		    public String getContentType() {
		      return "text/css";
		    }
		  },
		  JS {
		    @Override
		    public String getContentType() {
		      return "text/javascript";
		    }
		  };
		  /**
		   * @return the content type of the resource type.
		   */
		  public abstract String getContentType();

		  /**
		   * @return {@link ResourceType} associated to the string representation of the type.
		   */
		  public static ResourceType get(final String typeAsString) {
		    return ResourceType.valueOf(typeAsString.toUpperCase());
		  }
		}
	
	@Autowired
	@Qualifier("cacheJs")
	protected String cacheJs;
	
	private static final byte[] RETURN = "\n".getBytes();

	@RequestMapping("public/viewCss/{time}")
	@ResponseBody
	public String getCss(HttpServletRequest request, HttpServletResponse response, @PathVariable("time") String time) throws IOException {
		response.setContentType("text/css");

		OutputStream out = response.getOutputStream();
		streamViewCss(request, out);
		return null;
	}

	private void streamViewCss(HttpServletRequest request, OutputStream out) throws IOException {
		{
			String dirStr = request.getServletContext().getRealPath("/ang-app/pkg/directives");
			File dir = new File(dirStr);
			List<File> files = new ArrayList<>();
			findAllCss(dir, files);

			for (File css : files) {
				FileInputStream fis = new FileInputStream(css);
				IOUtils.copy(fis, out);
				fis.close();
			}
		}
		{
			String dirStr = request.getServletContext().getRealPath("/ang-app/view");
			File dir = new File(dirStr);
			List<File> files = new ArrayList<>();
			findAllCss(dir, files);

			for (File css : files) {
				FileInputStream fis = new FileInputStream(css);
				IOUtils.copy(fis, out);
				fis.close();
			}
			
		}
	
	}
	
	@Autowired
	ObjectMapper mapper;
	
	Map<String, List<String>> listOfCssUrlMapById = Collections.synchronizedMap(new HashMap<>());
	
	@RequestMapping(value="/generateImportCssAsyncBulk", method = RequestMethod.POST, headers = ACCEPT_JSON)
	public String importCssAsyncBulk(HttpServletRequest request, HttpServletResponse response, @RequestBody List<String> hrefs, @RequestParam("id") String id) throws IOException {
		if (hrefs.isEmpty())
			return null;
		
		List<String> local = hrefs.stream().filter(h -> !h.startsWith("http")).collect(Collectors.toList());
		List<String> remote = hrefs.stream().filter(h -> h.startsWith("http")).collect(Collectors.toList());
		
		listOfCssUrlMapById.put(id, local);
		List<String> res = new ArrayList<>();
		res.add("../../public/generatedCss/" + id);
		res.addAll(remote);
		
		String json = mapper.writeValueAsString(res);
		response.getWriter().print(json);
		
		return null;

	}
	public CssBundleTools() {
		buildMainResourcesIdx();
	}
	
	
	
	private void buildMainResourcesIdx() {
		buildCss();
		buildJs();
		buildDefferedJs();
		buildIndexJs();
		listOfCssUrlMapById.put("cacheControlJs", new ArrayList<>(Arrays.asList("/public/generatedJs/cacheControlJs")));
	}

	private void buildDefferedJs() {
		List<String> list = Arrays.asList(

				"/ang-app/pkg/common/net/DefaultHttpProvider.js",
				
				"/ang-app/pkg/common/net/HttpSessionInterceptor.js",
				
				"/ang-app/pkg/common/net/SessionTimeOutHttpInterceptor.js",
				"/ang-app/pkg/common/net/SpinLoadingHttpInterceptor.js",
				
				"/ang-app/pkg/modules/BaseModule.min.js",

				"/webjars/ng-file-upload/12.0.4/ng-file-upload-all.min.js",


			  	"/webjars/angular-sanitize/1.5.3/angular-sanitize.min.js",
			  	"/webjars/angular-cookies/1.5.3/angular-cookies.min.js",
			  	"/webjars/angular-animate/1.5.3/angular-animate.min.js",
			  	"/webjars/angular-aria/1.5.3/angular-aria.min.js",
			  	"/webjars/angular-messages/1.5.3/angular-messages.min.js",

			  	"/webjars/angular-material/1.1.0-rc2/angular-material.min.js",
			 	"/ang-app/js/md-table/md-data-table.min.js",
			 	"/ang-app/js/international-phone-number/international-phone-number.min.js",
			 	
				"/webjars/underscorejs/1.8.3/underscore-min.js",
				"/webjars/angular-ui-router/0.2.18/release/angular-ui-router.min.js",
				
				"/webjars/intl-tel-input/5.1.7/lib/libphonenumber/build/utils.js",
				"/ang-app/js/angular-int-phone-number/international-phone-number.min.js",
				"/webjars/intl-tel-input/5.1.7/build/js/intlTelInput.min.js",

				"/ang-app/js/jquery-ui-custom/jquery-ui.min.js"		

				
		);
		
		listOfCssUrlMapById.put("deferJs", list);
	}

	private void buildIndexJs() {
		List<String> list = Arrays.asList(

				"/webjars/jquery/2.2.2/jquery.min.js",
				"/webjars/angular/1.5.3/angular.min.js",
			 	
				"/ang-app/pkg/js/spin.min.js",
				"/ang-app/js/modernizr.js",
				"/ang-app/pkg/system/Class.min.js",
				"/ang-app/pkg/system/Lang.min.js",
				"/ang-app/pkg/system/Package.min.js",
				"/ang-app/pkg/system/Import.js",
				"/ang-app/pkg/system/Activator.min.js",

				"/ang-app/pkg/tools/Clone.js"
		);
		
		listOfCssUrlMapById.put("indexJs", list);
	}

	private void buildJs() {
		List<String> list = Arrays.asList(
//			 	"/ang-app/js/ng-signature-pad/signature_pad.min.js",
//			 	"/ang-app/js/ng-signature-pad/ng-signature-pad.min.js"
				
		);
		listOfCssUrlMapById.put("mainJs", list);
	}
	
	private void buildCss() {
		List<String> list = Arrays.asList(
				
			 	"/webjars/angular-material/1.1.0-rc2/angular-material.min.css",
			 	"/ang-app/js/md-table/md-data-table.min.css",
				"/ang-app/js/md-color-picker/mdColorPicker.min.css",

			 	"/ang-app/js/jquery-ui-custom/jquery-ui.min.css",
			 	"/ang-app/js/jquery-ui-custom/jquery-ui.structure.min.css",
			 	"/ang-app/js/jquery-ui-custom/jquery-ui.theme.min.css",
			 	
				"/public/viewCss.css",
				"/css/main.css"
			);
		
		listOfCssUrlMapById.put("mainCss", list);
	}
	public static final String template = 
			"Package('%PKG%')" +
			".%NAME% = %VALUE%;";
	
	@Autowired
	RouteMap routeMap;

	
	@RequestMapping("/ang-app/pkg/special/path/routes/Routes/{time}")
    public void routesTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable("time") String time) throws ClassNotFoundException, JsonProcessingException, IOException {
    	streamRoutes(response.getOutputStream());
    }
	
	

	public void streamRoutes(OutputStream out) throws ClassNotFoundException, JsonProcessingException, IOException {
    	String res = template.replace("%PKG%", "special.path.routes").replace("%NAME%", "Routes").replace("%VALUE%", mapper.writeValueAsString(routeMap.routes()));
    	IOUtils.copy(new ByteArrayInputStream(res.getBytes()), out);
    }
	
	private Map<String, byte[]> _cache = new HashMap<>();
	@RequestMapping(value="/public/generatedJs/{id}/{time}", method = RequestMethod.GET, headers = ACCEPT_JSON)
	public String getCssGeneratedResource(HttpServletRequest request, HttpServletResponse response, @PathVariable String id, @PathVariable("time") String time) throws Exception {
		return getGeneratedResource(request, response, id, ResourceType.JS);
	}
	
	@RequestMapping(value="/public/generatedJsToEval/{id}/{time}", method = RequestMethod.GET, headers = ACCEPT_JSON)
	@ResponseBody
	public List<String> generatedJsToEval(HttpServletRequest request, HttpServletResponse response, @PathVariable String id, @PathVariable("time") String time) throws Exception {
		return importClassesJsAsyncBulk(request, response, listOfCssUrlMapById.get(id));
	}
	
	
	public List<String> importClassesJsAsyncBulk(HttpServletRequest request, HttpServletResponse response, List<String> scripts) throws IOException {
		if (scripts == null || scripts.isEmpty())
			return Collections.EMPTY_LIST; 
		
		List<String> js = new ArrayList<String>();
		if (js.isEmpty()) {
			for (String script : scripts) {
				String file = request.getServletContext().getRealPath(script);
				File f = null;
				
				if (file != null)
					f = new File(file);
				if (f != null && f.exists()) {
					FileInputStream in = new FileInputStream(f);
					js.add(IOUtils.toString(in));
					in.close();
				} else {
					InputStream in = Class.class.getResourceAsStream("/META-INF/resources" + script);
					
					js.add(IOUtils.toString(in));
					in.close();
				}
			}
			
		}
		return js;
	}
	
	@RequestMapping(value="/public/generatedCss/{id}/{time}", method = RequestMethod.GET, headers = ACCEPT_JSON)
	public String getJsGeneratedResource(HttpServletRequest request, HttpServletResponse response, @PathVariable String id, @PathVariable("time") String time) throws Exception {
		return getGeneratedResource(request, response, id, ResourceType.CSS);
		
	}


	public String getGeneratedResource(HttpServletRequest request, HttpServletResponse response, String id, ResourceType type) throws Exception {
		boolean local = cacheJs.equals("local");
		if (type == ResourceType.JS) {
			response.setContentType("text/javascript");
		} else if (type == ResourceType.CSS) {
			response.setContentType("text/css");
			
		}
		OutputStream out = response.getOutputStream();
		
		if (local) {
			buildMainResourcesIdx();
		} else {
			out = new ByteArrayOutputStream();

		}
		_cache.clear();
		if ((local || !_cache.containsKey(id)) && listOfCssUrlMapById.containsKey(id)) {
			List<String> urls = listOfCssUrlMapById.get(id);

			for (String url : urls) {
				if (url.startsWith("/public/generatedJs/cacheControlJs")) {
					IOUtils.copy(new ByteArrayInputStream(("var TIME_TOKEN_FROM_JS = " + TokenUtils.TIME + ";").getBytes()), out);
					out.write(RETURN);
				} else if (url.startsWith("/ang-app/pkg/special/path/routes/Routes")) {
					streamRoutes(out);
					out.write(RETURN);
				} else if (url.startsWith("/public/viewCss")) {
					streamViewCss(request, out);
					out.write(RETURN);
				} else {
					if (url.startsWith("/webjars")) {
						InputStream in = CssBundleTools.class.getResourceAsStream("/META-INF/resources" + url);
						if (in == null)
							in = request.getServletContext().getResourceAsStream("/META-INF/resources" + url);
						if (in == null) {
							in = ClassLoader.class.getResourceAsStream("/META-INF/resources" + url);
						}
						IOUtils.copy(in, out);
						out.write(RETURN);
						in.close();
					
					} else {
						String path = request.getServletContext().getRealPath(url);
						File css = new File(path);
						if (css.exists()) {
							FileInputStream fis = new FileInputStream(css);
							IOUtils.copy(fis, out);
							out.write(RETURN);
							fis.close();
							
						} 
					}
				}
			}
			out.write("".getBytes());
		}
		
		if (!local && !_cache.containsKey(id)) {
			byte[] bytes = ((ByteArrayOutputStream)out).toByteArray();
			_cache.put(id, bytes);
		}
		if (!local && _cache.containsKey(id)) {
			IOUtils.copy(new ByteArrayInputStream(_cache.get(id)), response.getOutputStream());
		}
			
			
		return null;
	}
	
	public static void findAllCss(File dir, List<File> results) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				findAllCss(file, results);
			} else {
				if (file.getName().toLowerCase().endsWith(".css"))
					results.add(file);
			}
		}
	}
}

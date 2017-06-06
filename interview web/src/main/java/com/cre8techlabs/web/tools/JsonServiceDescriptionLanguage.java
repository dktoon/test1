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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.DefaultValue;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.cre8techlabs.entity.Entity;
import com.cre8techlabs.tools.CollectionTargetType;
import com.cre8techlabs.tools.Required;
import com.cre8techlabs.utils.reflection.ClassUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonServiceDescriptionLanguage {
	public static class PropertyInfo {
		
		private String type;
		private boolean isMandatory;
		private boolean isArray;
		private boolean isObject;
		private String defaultValue;
		public boolean isEnum;
		public String enumType;
		
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public boolean isMandatory() {
			return isMandatory;
		}
		public void setMandatory(boolean isMandatory) {
			this.isMandatory = isMandatory;
		}
		public boolean isArray() {
			return isArray;
		}
		public void setArray(boolean isArray) {
			this.isArray = isArray;
		}
		public boolean isObject() {
			return isObject;
		}
		public void setObject(boolean isObject) {
			this.isObject = isObject;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		public boolean isEnum() {
			return isEnum;
		}
		public void setEnum(boolean isEnum) {
			this.isEnum = isEnum;
		}
		public String getEnumType() {
			return enumType;
		}
		public void setEnumType(String enumType) {
			this.enumType = enumType;
		}
	}
	private ObjectMapper mapper;

//	public JsonServiceDescriptionLanguage() {
//
//	}
	public JsonServiceDescriptionLanguage(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	
	public JsonServiceDescriptionLanguage() {
		this.mapper = new ObjectMapper();
	}

	private Set<Class<? extends Entity>> parsed = new HashSet<Class<? extends Entity>>();
	private Set<Class<? extends Entity>> toBeParsed = new HashSet<Class<? extends Entity>>();
	
	public void convert(Class<? extends Entity>[] classes, boolean onlyListedClass) throws Exception {
		parsed = new HashSet<Class<? extends Entity>>();
		toBeParsed = new HashSet<Class<? extends Entity>>(Arrays.asList(classes));
		StringBuilder buffer = new StringBuilder();
		for (Class<? extends Entity> clz: classes) {
			convert(clz, onlyListedClass);
		}

		while (!toBeParsed.isEmpty()) {
			toBeParsed.removeAll(parsed);
			for (Class<? extends Entity> clz: new ArrayList<Class<? extends Entity>>(toBeParsed)) {
				convert(clz, onlyListedClass);
			}
			
		}
	}
	private Map<Class, String> convertMap = build();
	public void convert(Class<? extends Entity> clz, boolean onlyListedClass) throws Exception {
		try {
			parsed.add(clz);
			BeanInfo bi = Introspector.getBeanInfo(clz);
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();
			
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals("class"))
					continue;
				Class pdClass = pd.getPropertyType();
				PropertyInfo result = new PropertyInfo();
				result.isArray = pdClass.isArray() || isCollection(pdClass);
				
				if (result.isArray) {
					if (pdClass.isArray()) {
						if (ClassUtils.isPrimitiveOrWrapper(pdClass.getComponentType())) {
							Class res = ClassUtils.primitiveWrapperTypeMap.get(pdClass.getComponentType());
							pdClass = res == null? pdClass.getComponentType(): res;
						} else {
							pdClass = pdClass.getComponentType();
						}
					} else if (result.isArray()) { // && isCollection(pdClass)) {
						pdClass = getTargetClass(clz, pd);
						if (pdClass == null)
							continue;
						if (ClassUtils.isPrimitiveOrWrapper(pdClass)) {
							pdClass = ClassUtils.primitiveWrapperTypeMap.get(pdClass);
							
						}
					}
					
				}
				//
				result.isMandatory = isMandatory(clz, pd);
				
				result.defaultValue = getDefaultValue(clz, pd.getName());
				
				result.type = getJavascriptClass(pdClass);
				result.isEnum = pdClass.isEnum();
				if (pdClass.isEnum()) {
					result.type = "enum";
				}
				result.enumType = pdClass.getName();
				//
				if (result.type.equals("object")) {
					result.isObject = true;
				}
				properties.put(pd.getName(), result);
				if (!parsed.contains(pdClass) && !ClassUtils.isPrimitiveOrWrapper(pdClass) && !result.type.equals("string") && !result.type.equals("object") && !result.type.equals("date")) {
					result.isObject = true;
					//toBeParsed.add(pdClass);
					if (!onlyListedClass)
						convert(pdClass, onlyListedClass);
				}
				
			}
			buffer.append(formatPackageAndJSDLName(clz, properties)).append("\n");
			
		} catch (java.beans.IntrospectionException e) {
		}
	}


	public String keyValue(int indent, Map<String, PropertyInfo> map) throws JsonProcessingException, InstantiationException, IllegalAccessException {
		StringBuilder builder = new StringBuilder();
	
		for (Entry<String, PropertyInfo> e: map.entrySet()) {
			builder.append(keyValue(indent, e.getKey(), e.getValue()));
		}
		
		return builder.toString();
	}
	public Object keyValue(int indent, String key, Map<String, PropertyInfo> map) throws JsonProcessingException, InstantiationException, IllegalAccessException {
		return JSonUtils.indent(indent) + key + ": {\n" + keyValue(indent + 1, map) + JSonUtils.indent(indent) + "},\n";
	}
	public String keyValue(int indent, String key, PropertyInfo value) throws JsonProcessingException, InstantiationException, IllegalAccessException {
		return JSonUtils.indent(indent) + key + ": {\n" + 
				JSonUtils.keyValue(indent + 1, "type", "'" + value.type + "'") + 
				JSonUtils.keyValue(indent + 1, "isEnum",  value.isEnum + "") + 
				JSonUtils.keyValue(indent + 1, "enumType", "'" + value.enumType + "'") + 
				JSonUtils.keyValue(indent + 1, "isArray", value.isArray + "") +
				JSonUtils.keyValue(indent + 1, "isMandatory", value.isMandatory + "") +
				JSonUtils.keyValue(indent + 1, "isObject", value.isObject + "") +
				JSonUtils.keyValue(indent + 1, "defaultValue", value.defaultValue + "") +
				JSonUtils.indent(indent) + "},\n";
	}
	private StringBuilder buffer = new StringBuilder();
	private boolean isMandatory(Class<? extends Entity> clz, final String name) {
		AtomicBoolean result = new AtomicBoolean();
		ReflectionUtils.doWithFields(clz,
			    new FieldCallback(){

			        @Override
			        public void doWith(final Field f) throws IllegalArgumentException,
			            IllegalAccessException{

			        	if (f.isAnnotationPresent(Required.class)) {
			    			Required t = f
			    					.getAnnotation(Required.class);
			    			result.set(t != null);
			    		} else {
				        	result.set(false);
			    		}
			        }
			    },
			    new FieldFilter(){

			        @Override
			        public boolean matches(final Field field){
			            return field.getName().equals(name);
			        }
			    });
		return result.get();
	}
	
	private String getDefaultValue(Class<? extends Entity> clz, final String name) {
		StringBuilder result = new StringBuilder();
		ReflectionUtils.doWithFields(clz,
			    new FieldCallback(){

			        @Override
			        public void doWith(final Field f) throws IllegalArgumentException,
			            IllegalAccessException{

			        	if (f.isAnnotationPresent(DefaultValue.class)) {
			        		DefaultValue t = f
			    					.getAnnotation(DefaultValue.class);
			    			result.append("'" + t.value() + "'");
			    		}
			        }
			    },
			    new FieldFilter(){

			        @Override
			        public boolean matches(final Field field){
			            return field.getName().equals(name);
			        }
			    });
		return result.toString().length() == 0? null: result.toString();
	}
	private boolean isMandatory(Class<? extends Entity> clz, PropertyDescriptor prop) throws Exception {
		
		return isMandatory(clz, prop.getName());
	}


	private String formatPackageAndJSDLName(Class clz, Map<String, PropertyInfo> map) throws JsonProcessingException, InstantiationException, IllegalAccessException {
		int indent = 0;
		StringBuilder builder = new StringBuilder();
		builder.append(JSonUtils.indent(indent) + MessageFormat.format(template, clz.getPackage().getName(), clz.getSimpleName(), ""));
		builder.append(formatJSDLBody(indent++, clz, map));

		return builder.toString();
	}
	private String formatJSDLBody(int indent, Class clz, Map<String, PropertyInfo> map) throws JsonProcessingException, InstantiationException, IllegalAccessException {
		StringBuilder builder = new StringBuilder();
		builder.append(JSonUtils.indent(indent) + "{" + "\n");

		builder.append(JSonUtils.keyValue(indent + 1, "type", "'" + clz.getName() + "'"));
		builder.append(keyValue(indent + 1, "properties", map));
		
		
		//builder.append(JSonUtils.keyValue(indent + 1, "defaultValue", mapper.writeValueAsString(clz.newInstance())));

		builder.append(JSonUtils.indent(indent) + "}" + "\n");
		
		return builder.toString();
	}
	private String template = "Package(\"{0}\").{1} = {2} ";
	private String getJavascriptClass(Class clz) {
		if (clz.isEnum())
			return "string";
		if (Map.class.isAssignableFrom(clz)) {
			return "object";
		}
		String res = convertMap.get(clz);
		return res == null? clz.getName(): res;
	}
	private Map<Class, String> build() {
		Map<Class, String> result = new HashMap<Class, String>();
		result.put(String.class, "string");
		result.put(Date.class, "date");
		result.put(org.bson.types.ObjectId.class, "string");
		
		return result;
	}

	private Field getField(Class clz, final String name) {
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtils.doWithFields(clz,
			    new FieldCallback(){

			        @Override
			        public void doWith(final Field f) throws IllegalArgumentException,
			            IllegalAccessException{

			        	fields.add(f);
			    		}
			        },
	
			    new FieldFilter(){

			        @Override
			        public boolean matches(final Field field){
			            return field.getName().equals(name);
			        }
			    });
		if (!fields.isEmpty())
			return fields.get(0);
		return null;
	}
	private Class getTargetClass(Class clz, PropertyDescriptor prop)
			throws Exception {
		Field f = getField(clz, prop.getName());
		if (f == null)
			return null;
		if (f.isAnnotationPresent(CollectionTargetType.class)) {
			CollectionTargetType t = f
					.getAnnotation(CollectionTargetType.class);
			return t.type();
		} else {
			
			Type type = f.getType().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				return Class.forName(type.getTypeName());
				
			}
			
			if (prop.getReadMethod().isAnnotationPresent(CollectionTargetType.class)) {
				CollectionTargetType t = prop.getReadMethod()
						.getAnnotation(CollectionTargetType.class);
				return t.type();
			}
		}

		return null;
	}
	
	private boolean isCollection(Class c) {
		return Collection.class.isAssignableFrom(c);
	}
	public StringBuilder getBuffer() {
		return buffer;
	}
}

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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bson.types.ObjectId;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.cre8techlabs.entity.Entity;
import com.cre8techlabs.tools.CollectionTargetType;
import com.cre8techlabs.tools.MapTargetType;

public final class PropertyUtils {

	
	public interface ObjectUnproxy {
		Object get(Object bean);
	}

	private Reference refs = new Reference();
	private Parsed parsed = new Parsed();

	private boolean eraseWithNull = false;
	private ObjectUnproxy objectUnproxy = new ObjectUnproxy() {

		@Override
		public Object get(Object bean) {
			return bean;
		}
	};

	public PropertyUtils() {

	}

	public PropertyUtils(ObjectUnproxy unproxy) {
		if (unproxy != null)
			objectUnproxy = unproxy;
	}

	public Collection copyCollection(Collection srcs) throws Exception {
		return copyCollection(srcs, srcs.getClass().newInstance(),
				(PropertyDescriptor) null, (PropertyDescriptor) null);
	}

	public Collection copyCollection(Collection srcs, Collection dsts)
			throws Exception {
		return copyCollection(srcs, dsts, (PropertyDescriptor) null,
				(PropertyDescriptor) null);
	}

	public Object copy(Object src, Class targetClass) throws Exception {
		if (src == null)
			return null;
		return copy(src, targetClass.newInstance());
	}

	public Object copy(Object src) throws Exception {
		if (src == null)
			return null;
		return copy(src, src.getClass().newInstance());
	}
	
	
	private Object merge(Object src, Object dst, Class clazz) {
		if (clazz == Object.class) return dst;
		Method[] methods = clazz.getMethods();
		
		Set<String> mergeSet = null;
		//Collections.EMPTY_SET;
		
//		
	    for(Method fromMethod: methods){
	        if(fromMethod.getDeclaringClass().equals(clazz)
	                && (fromMethod.getName().matches("\\b(get|is).*"))){// startsWith("get") || fromMethod.getName().startsWith("is"))){
//	        	
	        	
	        	String fromName = fromMethod.getName();

	            String key = fromName.replaceFirst("(get|is)", "");
	            String toName =  "set"+key;
	            char c = Character.toLowerCase(key.charAt(0));
	            
	            
	            try {
	                Method toMetod = clazz.getMethod(toName, fromMethod.getReturnType());
	                Object value = fromMethod.invoke(src, (Object[])null);
	            	
		            if (!mergeSet.contains(c+key.substring(1))) {
		            	toMetod.invoke(dst, value);
		            	continue;
		            } 
//		            else {
//		            	
//		            }
	                if(value != null){
	                	if (value instanceof Collection<?> && ((Collection) value).size() == 0) {
	                		continue;
	                	}
	                	else if (value instanceof Collection<?>) {
	                		Object value2 = fromMethod.invoke(dst, (Object[])null);
	                		
	                		if (value2 instanceof Collection<?> && ((Collection) value2).size() == 0) {
	                			toMetod.invoke(dst, value);
	                			continue;
	                		} else {
	                			((Collection<?>)value).forEach((Object o) ->{
	                				if (!((Collection<Object>)value2).contains(o))
	                					((Collection<Object>)value2).add(o);
	                			});
	                		}
	                	}
	                	
	                    toMetod.invoke(dst, value);
	                } else if (fromMethod.getName().contains("Salt")) {
	                	toMetod.invoke(dst, value);
		        	}
	            } catch (Exception e) {
	            	System.err.println(clazz.getSimpleName()+" : "+toName+"("+fromMethod.getReturnType().getSimpleName() +") method not found : "+e.getLocalizedMessage());
	            } 
	        }
	    }
		
		return merge(src, dst, clazz.getSuperclass());
	}
	
	public Object merge(Object src, Object dst) throws Exception{
		if (src == null || dst == null) return dst;
		if (src.getClass() != dst.getClass()) return dst;

		dst = merge(src, dst, src.getClass());
		
		return dst;
	}

	public Object copy(Object src, Object dst) throws Exception {
		if (src == null)
			return null;

		src = getRealObject(src);
		try {
			Class realTargetClass = getRealObject(dst).getClass();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Class realTargetClass = getRealObject(dst).getClass();
		refs.setCopyValue(src, realTargetClass, dst);
		Object res = refs.getCopyValue(src, realTargetClass);
		if (res != null && parsed.isParsed(src, realTargetClass)) {
			return res;
		}
		if (!parsed.isParsed(src, getRealObject(dst).getClass()))
			parsed.setParsed(src, getRealObject(dst).getClass());

		Class targetClass = dst.getClass();
		PropertyDescriptor[] targetPropertyDescriptors = org.apache.commons.beanutils.PropertyUtils
				.getPropertyDescriptors(targetClass);
		Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor pd : targetPropertyDescriptors) {
			map.put(pd.getName(), pd);
		}
		for (PropertyDescriptor srcProp : org.apache.commons.beanutils.PropertyUtils
				.getPropertyDescriptors(src.getClass())) {
			PropertyDescriptor dstProp = null;
			dstProp = map.get(srcProp.getName());

			if (dstProp == null) {
				continue;
			}

			copy(src, dst, srcProp, dstProp);
		}

		return dst;
	}

	private void copy(Object src, Object dst, PropertyDescriptor srcProp,
			PropertyDescriptor dstProp) throws Exception {
		if (srcProp.getName().equals("class" + ""))
			return;
		if (isSimpleValue(srcProp)) {
			copySimpleValue(src, dst, srcProp, dstProp);
		} else if (srcProp.getPropertyType().isAssignableFrom(ObjectId.class)) {
			copySimpleValue(src, dst, srcProp, dstProp);
		} else if (isObject(srcProp)) {
			copyObject(src, dst, srcProp, dstProp);
		} else if (isCollection(srcProp)) {
			copyCollection(src, dst, srcProp, dstProp);
		} else if (isMap(srcProp)) {
			copyMap(src, dst, srcProp, dstProp);
		}

	}

	private Map copyMap(Object src, Object dst, PropertyDescriptor srcProp,
			PropertyDescriptor dstProp) throws Exception {
		Object srcListValue = srcProp.getReadMethod().invoke(src);
		Object dstListValue = dstProp.getReadMethod().invoke(dst);

		if (srcListValue == null && dstListValue == null)
			return null;
		if (dstListValue == null) {
			Class dstCollectionType = dstProp.getPropertyType();
			if (Modifier.isAbstract(dstCollectionType.getModifiers())
					|| Modifier.isInterface(dstCollectionType.getModifiers())) {
				if (dstCollectionType.isAssignableFrom(srcListValue.getClass())) {
					dstListValue = srcProp.getPropertyType().newInstance();
				} else {
					Set<Class> interfaces = new HashSet<Class>(
							Arrays.asList(dstCollectionType.getInterfaces()));
					if (interfaces.contains(SortedMap.class)) {
						dstListValue = new TreeMap();
					} else if (interfaces.contains(Map.class)) {
						dstListValue = new HashMap();

					}
				}
			} else {
				dstListValue = dstCollectionType.newInstance();
			}

		}
		return copyMap((Map) srcListValue,
				(Map) dstListValue, getMapTargetClass(src.getClass(), srcProp),
				getMapTargetClass(dst.getClass(), dstProp));
	}



	private Collection copyCollection(Object src, Object dst,
			PropertyDescriptor srcProp, PropertyDescriptor dstProp)
			throws Exception {
		Object srcListValue = srcProp.getReadMethod().invoke(src);
		Object dstListValue = dstProp.getReadMethod().invoke(dst);

		if (srcListValue == null && dstListValue == null)
			return null;
		if (dstListValue == null) {
			Class dstCollectionType = dstProp.getPropertyType();
			if (Modifier.isAbstract(dstCollectionType.getModifiers())
					|| Modifier.isInterface(dstCollectionType.getModifiers())) {
				if (dstCollectionType.isAssignableFrom(srcListValue.getClass())) {
					dstListValue = srcProp.getPropertyType().newInstance();
				} else {
					Set<Class> interfaces = new HashSet<Class>(
							Arrays.asList(dstCollectionType.getInterfaces()));
					if (interfaces.contains(List.class)) {
						dstListValue = new ArrayList();
					} else if (interfaces.contains(Set.class)) {
						dstListValue = new HashSet();

					}
				}
			} else {
				dstListValue = dstCollectionType.newInstance();
			}

		}
		return copyCollection((Collection) srcListValue,
				(Collection) dstListValue, getTargetClass(src.getClass(), srcProp),
				getTargetClass(dst.getClass(), dstProp));
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
		throw new IllegalArgumentException();
	}
	private Class getTargetClass(Class clz, PropertyDescriptor prop)
			throws Exception {
		
		Field f = getField(clz, prop.getName());

		if (f.isAnnotationPresent(CollectionTargetType.class)) {
			CollectionTargetType t = f
					.getAnnotation(CollectionTargetType.class);
			return t.type();
		} else {
			
			Type type = f.getType().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType p = (ParameterizedType)type;
			    return Class.forName(p.getActualTypeArguments()[0].getTypeName());

				
			}
			
			if (prop.getReadMethod().isAnnotationPresent(CollectionTargetType.class)) {
				CollectionTargetType t = prop.getReadMethod()
						.getAnnotation(CollectionTargetType.class);
				return t.type();
			}
		}

		return null;
	}
	private Class[] getMapTargetClass(Class clz, PropertyDescriptor prop)
			throws Exception {
		
		Field f = getField(clz, prop.getName());

		if (f.isAnnotationPresent(MapTargetType.class)) {
			MapTargetType t = f
					.getAnnotation(MapTargetType.class);
			return new Class[] {t.keyType(), t.valueType()};
		} else {
			
			Type type = f.getType().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType p = (ParameterizedType)type;
			    return new Class[] {Class.forName(p.getActualTypeArguments()[0].getTypeName()), Class.forName(p.getActualTypeArguments()[1].getTypeName())};

				
			}
			
			if (prop.getReadMethod().isAnnotationPresent(MapTargetType.class)) {
				MapTargetType t = prop.getReadMethod()
						.getAnnotation(MapTargetType.class);
				return new Class[] {t.keyType(), t.valueType()};
			}
		}

		return null;
	}
	private Map copyMap(Map<Object, Object> srcs, Map<Object, Object> dsts,
			Class[] targetSrcClasses, Class[] targetDstClasses) {
		if (srcs == null || srcs.isEmpty()) {

			dsts.clear();
			return dsts;
		}
		dsts.clear();
		boolean isDstsEmptyAtStart = dsts.isEmpty();
		
		for (Entry srcEntry : srcs.entrySet()) {
			boolean isDstCreate = false;
			Object srcKey = srcEntry.getKey();
			Object srcValue = srcEntry.getValue();

			Object dstKey = null;
			Object dstValue = null;
			
			if (isSimpleValue(srcValue.getClass())) {
				dstValue = srcValue;
			} else {
				throw new UnsupportedOperationException();
			}
			if (dstValue != null) {

				dsts.put(srcKey, dstValue);
			}
		}
		return dsts;
	}


	private Collection copyCollection(Collection srcs, Collection dsts,
			Class targetSrcClass, Class targetDstClass) throws Exception {
		if (srcs == null || srcs.isEmpty()) {

			dsts.clear();
			return dsts;
		}
		boolean isDstsEmptyAtStart = dsts.isEmpty();

		for (Object srcValue : srcs) {
			boolean isDstCreate = false;
			if (srcValue == null)
				continue;
			Object dstValue = null;
			if (isSimpleValue(srcValue.getClass())) {
				dstValue = srcValue;
			} else if (srcValue.getClass().isAssignableFrom(ObjectId.class)) {
				dstValue = srcValue;
				
			} else if (isCollection(srcValue.getClass())) {
				throw new UnsupportedOperationException();
			} else {
				dstValue = findValueById(dsts, srcValue);

				if (dstValue == null) {
					isDstCreate = true;
					if (targetDstClass != null && !Modifier.isAbstract(targetDstClass
							.getModifiers())) {
						dstValue = targetDstClass.newInstance();
					} else {
						dstValue = srcValue.getClass().newInstance();
					} 
				}

				dstValue = copy(srcValue, dstValue);
			}
			if (dstValue != null) {
				// Debug.Assert(false,
				// "We supossed that the destination is a IList<> of the right type");
				removeValueFromCollection(dsts, dstValue);
				dsts.add(dstValue);
			}
		}

		if (!isDstsEmptyAtStart)
			balance(srcs, dsts);
		return dsts;
	}

	private void balance(Collection srcs, Collection dsts) throws Exception {
		List toDelete = new ArrayList();
		for (Object o : dsts) {
			Object src = findValueById(srcs, o);
			if (src == null) {
				toDelete.add(o);
			}
		}
		dsts.removeAll(toDelete);

	}

	private void removeValueFromCollection(Collection dsts, Object val)
			throws Exception {
		if (dsts.remove(val))
			return;
		try {
			Object idVal = val.getClass().getMethod("getId").invoke(val);
			for (Iterator iter = dsts.iterator(); iter.hasNext();) {
				Object object = (Object) iter.next();
				Object id = object.getClass().getMethod("getId").invoke(object);
				if (id != null && idVal != null && id.equals(idVal)) {
					iter.remove();
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private Object findValueById(Collection dsts, Object val) throws Exception {
		try {
			Object idVal = val.getClass().getMethod("getId").invoke(val);
			for (Iterator iter = dsts.iterator(); iter.hasNext();) {
				Object object = (Object) iter.next();
				Object id = object.getClass().getMethod("getId").invoke(object);
				if (id != null && idVal != null && id.equals(idVal)) {
					return object;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private void copyObject(Object src, Object dst, PropertyDescriptor srcProp,
			PropertyDescriptor dstProp) throws Exception {

		Object srcValue = srcProp.getReadMethod().invoke(src);
		Object dstValue = null;

		if (srcValue == null && eraseWithNull) {
			dstProp.getWriteMethod().invoke(dst, null);
			return;
		}
		if (srcValue == null)
			return;
		srcValue = getRealObject(srcValue);
		Object dstValueTempForType = dstProp.getReadMethod().invoke(dst);
		Class targetType = dstProp.getPropertyType();

		if (dstValueTempForType != null) {
			targetType = getRealObject(dstValueTempForType).getClass();
		} else if (targetType.isInstance(srcValue)) {
			targetType = srcValue.getClass();
		}

		dstValue = refs.getCopyValue(srcValue, targetType);

		if (dstValue == null) {

			if (Modifier.isAbstract(dstProp.getPropertyType().getModifiers())) {
				Class srcType = srcProp.getPropertyType();
				if (Modifier.isAbstract(srcType.getModifiers())) {
					srcType = srcValue.getClass();
				}
			
				dstValue = srcType.newInstance();
					
	

			} else {
				dstValue = dstProp.getReadMethod().invoke(dst);
				if (dstValue != null
						&& dstValue.hashCode() == srcValue.hashCode()) {

				} else {
					dstValue = dstProp.getPropertyType().newInstance();

				}
			}
			refs.setCopyValue(srcValue, dstProp.getPropertyType(), dstValue);
		}
		if (!parsed.isParsed(srcValue, dstProp.getPropertyType())) {
			copy(srcValue, dstValue);
		}

		dstProp.getWriteMethod().invoke(dst, dstValue);
	}

	private void copySimpleValue(Object src, Object dst,
			PropertyDescriptor srcProp, PropertyDescriptor dstProp)
			throws Exception {
		Object val = srcProp.getReadMethod().invoke(src);
		if (val != null || eraseWithNull) {
			dstProp.getWriteMethod().invoke(dst, val);
		}

	}

	private boolean isCollection(PropertyDescriptor prop) {
		Class c = prop.getPropertyType();
		return Collection.class.isAssignableFrom(c);
	}
	private boolean isMap(PropertyDescriptor prop) {
		Class c = prop.getPropertyType();
		return Map.class.isAssignableFrom(c);
	}
	private boolean isMap(Class c) {
		return Map.class.isAssignableFrom(c);
	}
	private boolean isCollection(Class c) {
		return Collection.class.isAssignableFrom(c);
	}

	private boolean isObject(PropertyDescriptor prop) {
		return !isCollection(prop) && !isMap(prop) && !isSimpleValue(prop);
	}

	private boolean isSimpleValue(PropertyDescriptor prop) {
		return isSimpleProperty(prop.getPropertyType());
	}

	private boolean isSimpleValue(Class clz) {
		return isSimpleProperty(clz);
	}

	private Object getRealObject(Object obj) {
		return objectUnproxy.get(obj);
	}

	public static boolean isSimpleProperty(Class<?> clazz) {
		return isSimpleValueType(clazz)
				|| (clazz.isArray() && isSimpleValueType(clazz
						.getComponentType()));
	}

	public static boolean isSimpleValueType(Class<?> clazz) {
		return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum()
				|| CharSequence.class.isAssignableFrom(clazz)
				|| Number.class.isAssignableFrom(clazz)
				|| Date.class.isAssignableFrom(clazz)
				|| clazz.equals(URI.class) || clazz.equals(URL.class)
				|| clazz.equals(Locale.class) || clazz.equals(Class.class);
	}

}

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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.types.ObjectId;

import com.cre8techlabs.entity.Entity;
import com.cre8techlabs.utils.io.FastByteArrayOutputStream;

public class EntityUtils {
	public static Class<?>[] findSuperClassParameterType(Class<?> classOfInterest) {
		Class<?> subClass = classOfInterest;
		ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
		
		java.lang.reflect.Type[] types = parameterizedType.getActualTypeArguments();
		Class<?>[] result = new Class[types.length];
		for (int i = 0; i < types.length; i++) {
			result[i] = (Class<?>) types[i];
		}
		return result;
	}

//	public static <S,D> RatePeriodArchived copy(S src, D dst) {
//	    // TODO Auto-generated method stub
//	    return null;
//    }
	
	public static <T extends Entity> T cloneWithNewId(T entity) throws Exception {
		
		T result = clone(entity);
		replaceId(result, new LinkedHashSet<Object>(),
				new HashMap<ObjectId, ObjectId>(), null);
		return result;

	}
	public static <T extends Entity> T cloneWithNewId(T entity, Map<ObjectId, ObjectId> oldIdMapByNewId) throws Exception {
		T result = clone(entity);
		replaceId(result, new LinkedHashSet<Object>(), oldIdMapByNewId, null);
		return result;
    }
	public static <T extends Entity> T cloneWithNewId(T entity, Map<ObjectId, ObjectId> oldIdMapByNewId, Map<ObjectId, ObjectId> newIdMapByOldId) throws Exception {
		T result = clone(entity);
		replaceId(result, new LinkedHashSet<Object>(), oldIdMapByNewId, null);
		return result;
    }
//	public static <T extends Entity> T copy(T entity, ObjectMapper mapper) throws IOException {
//		byte[] bytes = mapper.writeValueAsBytes(entity);
//		T result = (T) mapper.readValue(bytes, entity.getClass());
//
//		return result;
//
//	}

	@SuppressWarnings("unchecked")
	public static <T> T clone(T entity) {
		T obj = null;
		try {
			// Write the object out to a byte array
			FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(entity);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(bos.getInputStream());
			obj = (T) in.readObject();
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

	public static <T extends Entity> List<T> clone(List<T> entities,
			List<T> results) throws Exception {
		for (T t : entities) {
			results.add(clone(t));
		}

		return results;
	}
	private static Set<Class> immutableClass = new HashSet<Class>(Arrays.asList(
			Class[].class,
			String[].class,
			int[].class,
			Integer[].class,
			float[].class,
			Float[].class,
			double.class,
			Double[].class,
			BigDecimal.class,
			BigDecimal[].class,
			boolean[].class,
			Boolean[].class,
			
			Class.class,
			String.class,
			int.class,
			Integer.class,
			float.class,
			Float.class,
			double.class,
			Double.class,
			BigDecimal.class,
			BigDecimal.class,
			boolean.class,
			Boolean.class
			
				
			));
	public static boolean isSimpleValueType(Class<?> clazz) {
		return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum()
				|| CharSequence.class.isAssignableFrom(clazz)
				|| Number.class.isAssignableFrom(clazz)
				|| Date.class.isAssignableFrom(clazz)
				|| clazz.equals(URI.class) || clazz.equals(URL.class)
				|| clazz.equals(Locale.class) || clazz.equals(Class.class);
	}
	public static void replaceId(Object bean, Set<Object> parsed,
			Map<ObjectId, ObjectId> map, Map<ObjectId, ObjectId> reverseMap) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (bean == null || ClassUtils.isPrimitiveOrWrapper(bean.getClass()))
			return;
		if (parsed.contains(bean))
			return;
		if (bean instanceof Entity) {
			Entity entity = (Entity) bean;
			if (map.containsKey(entity.getId())) {
				entity.setId(map.get(entity.getId()));
			} else {
				ObjectId newId = new ObjectId();
				if (entity.getId() != null) {
					if (reverseMap != null && reverseMap.get(entity.getId()) != null) {
						newId = reverseMap.get(entity.getId());
					}
					map.put(entity.getId(), newId);
					entity.setId(newId);
				}
			}

		}

		parsed.add(bean);

		for (PropertyDescriptor prop : org.apache.commons.beanutils.PropertyUtils
				.getPropertyDescriptors(bean.getClass())) {
			if (
					ClassUtils.isPrimitiveOrWrapper(prop.getPropertyType())
					|| ClassUtils.isPrimitiveArray(prop.getPropertyType())
					|| ClassUtils.isPrimitiveWrapperArray(prop
							.getPropertyType()))
				continue;
			Method m = prop.getReadMethod();
			Object obj = null;
			if (m == null) {
				continue;
			}

			if (immutableClass.contains(m.getReturnType()))
				continue;

			if (isSimpleValueType(m.getReturnType()))
				continue;
			try {
				obj = m.invoke(bean);	
			} catch (Exception e) {
				continue;
			}
			if (obj instanceof Collection) {
				List<ObjectId> ids = new ArrayList<ObjectId>();
				for (Object object : (Collection) obj) {
					if (object instanceof ObjectId) {
						ids.add((ObjectId) object);
					} else {
						replaceId(object, parsed, map, reverseMap);

					}

				}
				// The condition is that this thing is executed after all the
				// parse which in here is ok will deal later with unordered
				((Collection) obj).removeAll(ids);
				for (ObjectId id : ids) {
					if (map.containsKey(id))
						((Collection) obj).add(map.get(id));
				}
			} else {
				replaceId(obj, parsed, map, reverseMap);

			}

		}
	}


	public static void replaceId(Object bean) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		replaceId(bean, new LinkedHashSet<Object>(),
				new HashMap<ObjectId, ObjectId>(), null);
    }

	public static void copy(Object src, Object dst) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(dst, src);
		
	}




}

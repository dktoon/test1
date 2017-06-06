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
package com.cre8techlabs.entity;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.util.ReflectionUtils;

public class CascadingMongoEventListener extends AbstractMongoEventListener {
  @Autowired
  private MongoOperations mongoOperations;

  @Override
  public void onBeforeConvert(final Object source) {
      ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {

          public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
              ReflectionUtils.makeAccessible(field);

		        if (field.isAnnotationPresent(DBRef.class)
		                && field.isAnnotationPresent(CascadeSave.class)) {
			        final Object fieldValue = field.get(source);
			        if (fieldValue != null) {

				        Class fieldClass = fieldValue.getClass();
				        if (Collection.class.isAssignableFrom(field.getType())) {
					        fieldClass = getParameterType(field);
				        }

				        DbRefFieldCallback callback = new DbRefFieldCallback();

				        ReflectionUtils.doWithFields(fieldClass, callback);

				        if (!callback.isIdFound()) {
					        throw new MappingException("Cannot perform cascade save on child object without id set");
				        }

				        if (Collection.class.isAssignableFrom(field.getType())) {
					        @SuppressWarnings("unchecked")
					        Collection<Object> models = (Collection<Object>) fieldValue;
					        for (Object model : models) {
						        mongoOperations.save(model);
					        }
				        } else {
					        mongoOperations.save(fieldValue);
				        }
			        }
		        }

          }

		private Class getParameterType(Field field) {
			ParameterizedType type = (ParameterizedType) field.getGenericType();
	        Class<?> clz = (Class<?>) type.getActualTypeArguments()[0];
			return clz;
		}
      });
  }

  private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {
      private boolean idFound;

      public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
          ReflectionUtils.makeAccessible(field);

          if (field.isAnnotationPresent(Id.class)) {
              idFound = true;
          }
      }

      public boolean isIdFound() {
          return idFound;
      }
  }
}
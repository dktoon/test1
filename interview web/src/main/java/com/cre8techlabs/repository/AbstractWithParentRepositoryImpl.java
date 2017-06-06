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
package com.cre8techlabs.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.cre8techlabs.entity.Entity;


public class AbstractWithParentRepositoryImpl<PARENT extends Entity, T, ID extends Serializable> 
	extends SimpleMongoRepository<T, ID>
	implements AbstractWithParentRepository<PARENT, T, ID> {
	
	

	public void init(Class<PARENT> parentClz,
			MongoEntityInformation<T, ID> metadata,
			MongoOperations mongoOperations) {
		mongoOps = mongoOperations;

		ReflectionUtils.doWithFields(AbstractWithParentRepositoryImpl.class,
			    new FieldCallback(){

	        @Override
	        public void doWith(final Field f) throws IllegalArgumentException,
	            IllegalAccessException{
	        	f.setAccessible(true);
	        	if (f.getName().equals("entityInformation"))
	        		f.set(AbstractWithParentRepositoryImpl.this, metadata);
	        	
	        	if (f.getName().equals("mongoOperations"))
	        		f.set(AbstractWithParentRepositoryImpl.this, mongoOperations);
	        }
	    },
	    new FieldFilter() {

	        @Override
	        public boolean matches(final Field f){
	            return f.getName().equals("entityInformation") || f.getName().equals("mongoOperations");
	        }
	    });
		entityClz = metadata.getJavaType();
		idClz = metadata.getIdType();
		this.parentClz = parentClz;
		if (this.getClass().isAnnotationPresent(ParentPath.class)) {
			pathToParent = this.getClass().getAnnotation(ParentPath.class).path();
		}

	}
	public AbstractWithParentRepositoryImpl(
			Class repositoryInterface,
			Class<PARENT> parentClz,
			MongoEntityInformation<T, ID> metadata,
			MongoOperations mongoOperations) {
		super(metadata, mongoOperations);
		mongoOps = mongoOperations;
		entityClz = metadata.getJavaType();
		idClz = metadata.getIdType();
		this.parentClz = parentClz;
		if (repositoryInterface.isAnnotationPresent(ParentPath.class)) {
			ParentPath pp = (ParentPath) repositoryInterface.getAnnotation(ParentPath.class);
			pathToParent = pp.path();
		}

	}



	protected Class<PARENT> parentClz;
	protected Class<T> entityClz;
	protected Class<ID> idClz;
	
	protected String pathToParent;
	

	
	public MongoOperations mongoOps;
	
	@Override
	public List<T> findAllByParentId(ID parentId) {
		Query query = new Query(where(pathToParent).is(new ObjectId(parentId.toString())));
		
		return mongoOps.find(query, entityClz);
	}

	@Override
	public List<T> findAllByParentId(ID parentId, Sort sort) {
		Query query = new Query(where(pathToParent).is(new ObjectId(parentId.toString())));
		return findAll(query.with(sort));
	}

	@Override
	public Page<T> findAllByParentId(ID parentId, Pageable pageable) {
		Long count = countByParentId(parentId);
		List<T> list = findAll(new Query(where(pathToParent).is(new ObjectId(parentId.toString()))).with(pageable));

		return new PageImpl<T>(list, pageable, count);
	}
	private List<T> findAll(Query query) {

		if (query == null) {
			return Collections.emptyList();
		}

		return mongoOps.find(query, entityClz);
	}
	public long countByParentId(ID parentId) {
		Query query = new Query(where(pathToParent).is(new ObjectId(parentId.toString())));
		return mongoOps.count(query, entityClz); 
	}
}

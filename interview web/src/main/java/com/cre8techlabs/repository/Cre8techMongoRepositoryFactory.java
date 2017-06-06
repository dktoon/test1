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

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;


public class Cre8techMongoRepositoryFactory extends MongoRepositoryFactory {

	private MongoOperations mongoOps;
	public Cre8techMongoRepositoryFactory(MongoOperations mongoOperations) {
		super(mongoOperations);
		this.mongoOps = mongoOperations;
		MongoTemplate mt = (MongoTemplate) this.mongoOps;
		MappingMongoConverter mmc = (MappingMongoConverter) mt.getConverter();
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return isQueryDslRepository2(metadata.getRepositoryInterface()) ? QueryDslMongoRepository.class
				: isWithParentRepository(metadata.getRepositoryInterface())? AbstractWithParentRepositoryImpl.class: MySimpleMongoRepository.class;
	}
	
	private static boolean isQueryDslRepository2(Class<?> repositoryInterface) {

		return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}
	private static boolean isWithParentRepository(Class<?> repositoryInterface) {

		return AbstractWithParentRepository.class.isAssignableFrom(repositoryInterface);
	}
	@Override
	protected Object getTargetRepository(RepositoryInformation metadata) {
		Class<?> repositoryInterface = metadata.getRepositoryInterface();
		MongoEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

		if (isQueryDslRepository2(repositoryInterface)) {
			return new QueryDslMongoRepository(entityInformation, mongoOps);
		} else if (isWithParentRepository(repositoryInterface)) {
			TypeInformation<?> information = ClassTypeInformation.from(repositoryInterface);
			List<TypeInformation<?>> arguments = information.getSuperTypeInformation(AbstractWithParentRepository.class).getTypeArguments();
			
			return new AbstractWithParentRepositoryImpl(repositoryInterface, arguments.get(0).getType(), entityInformation, mongoOps);
		} else {
			return new MySimpleMongoRepository(entityInformation, mongoOps);
		}
	}
}

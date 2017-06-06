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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.cre8techlabs.repository.Cre8techMongoRepositoryFactoryBean;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@Configuration
@EnableMongoRepositories(repositoryFactoryBeanClass = Cre8techMongoRepositoryFactoryBean.class,basePackages = "com.cre8techlabs.repository")
@PropertySource(value = "classpath:mongo.properties")
public class SpringMongoConfig extends AbstractMongoConfiguration {

	@Value("${host}")
	private String host;

	@Value("${port}")
	private String port;

	@Value("${database}")
	private String database;
	
	@Value("${usr}")
	private String usr;

	@Value("${password}")
	private String pwd;
	

	@Override
	protected String getDatabaseName() {
		return database;
	}

	@Override
	protected String getAuthenticationDatabaseName() {
		return database;
	}
	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}
	
	@Bean
	public DB db() throws Exception {
		return mongo().getDB(getDatabaseName());
	}
//	
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
	    return new SimpleMongoDbFactory(mongo(), getDatabaseName());
	}
//	
	
	@Bean
	@Override
	public Mongo mongo() throws Exception {
		ServerAddress serverAdress = new ServerAddress(host, Integer.parseInt(port));  
		Mongo mongo = null;
		if (usr != null && !usr.isEmpty()) {
			MongoCredential credential = MongoCredential.createCredential(usr, database, pwd.toCharArray());
			mongo = new MongoClient(serverAdress, Arrays.asList(credential));
			
		} else {
			mongo = new MongoClient(serverAdress);
		}
		
        mongo.setWriteConcern(WriteConcern.SAFE);      
        
        
        return mongo;
	}

	


	
}
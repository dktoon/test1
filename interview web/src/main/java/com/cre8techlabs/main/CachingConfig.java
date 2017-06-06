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

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.CacheConfiguration;

@Configuration
@EnableCaching
@EnableAutoConfiguration
public class CachingConfig implements CachingConfigurer {
//	@Bean
//	public CacheManager cacheManager() {
//
//		Cache cache = new ConcurrentMapCache("RatePeriods");
//
//		SimpleCacheManager manager = new SimpleCacheManager();
//		manager.setCaches(Arrays.asList(
//				cache,
//				new ConcurrentMapCache("Counties")
//				));
//
//		return manager;
//	}
	static class Item {
		String cache;
		boolean copyOnRead;
		boolean copyOnWrite;
		private int maxEntriesLocalHeap;
		private boolean eternal;
		public Item(String cache, boolean copyOnRead, boolean copyOnWrite, boolean eternal, int maxEntriesLocalHeap) {
			this.cache = cache;
			this.copyOnRead = copyOnRead;
			this.copyOnWrite = copyOnWrite;
			this.eternal = eternal;
			this.maxEntriesLocalHeap = maxEntriesLocalHeap;
		}
	}
    @Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
    	
    	net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
    	Item[] cacheFors = {
    			new Item("LoanLimit", false, false, true, 0),
    			new Item("FhaLoanLimit", false, false, true, 0),
    			new Item("State", false, false, true, 0),
    			new Item("GeoLiteBlocks", false, false, false, 1000),
    			new Item("AveragePrimeOfferRates", false, false, true, 0)
    			
    			
    	};
    	for (Item cacheFor : cacheFors) {
            CacheConfiguration cacheConfiguration = new CacheConfiguration();
            cacheConfiguration.setName(cacheFor.cache);
            cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
            cacheConfiguration.setMaxEntriesLocalHeap(cacheFor.maxEntriesLocalHeap);
            cacheConfiguration.setEternal(cacheFor.eternal);
            cacheConfiguration.copyOnRead(cacheFor.copyOnRead);
            cacheConfiguration.copyOnRead(cacheFor.copyOnWrite);
            
            config.addCache(cacheConfiguration);
		}


        return net.sf.ehcache.CacheManager.newInstance(config);
    }
    CacheManager cacheManager;
    @Bean
    @Override
    public CacheManager cacheManager() {
    	return cacheManager = cacheManager == null? new EhCacheCacheManager(ehCacheManager()): cacheManager;
    }

    @Bean
	@Override
	public CacheResolver cacheResolver() {
		return new SimpleCacheResolver(cacheManager());
	}

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new MySimpleKeyGenerator();
    }

    @Bean
	@Override
	public CacheErrorHandler errorHandler() {
		return new SimpleCacheErrorHandler();
	}
}
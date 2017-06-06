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

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cre8techlabs.utils.ISO8601;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@EnableWebMvc
@Configuration
@ComponentScan({ "com.cre8techlabs.web" })
public class JsonResponseConfig extends WebMvcConfigurerAdapter {
	
	public static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
	    @Override
	    public void serialize(ObjectId value, JsonGenerator jsonGen, SerializerProvider provider) throws IOException {
	        if (value == null) {
	        	jsonGen.writeNull();
	        } else {
	        	jsonGen.writeString(value.toString());
	        }

	    }
	}
	public static class ObjectIdDeSerializer extends JsonDeserializer<ObjectId> {
		@Override
		public ObjectId deserialize(JsonParser jsonParser, DeserializationContext ctx)
				throws IOException, JsonProcessingException {
	        ObjectCodec oc = jsonParser.getCodec();
	        String id = oc.readValue(jsonParser, String.class);
			return new ObjectId(id);
		}
	}
	public static class DateSerializer extends JsonSerializer<Date> {

		@Override
		public void serialize(Date value, JsonGenerator jsonGen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
	        if (value == null) {
	        	jsonGen.writeNull();
	        } else {
	        	jsonGen.writeString(ISO8601.fromDate(value));
	        }			
		}
		
	}
	public static class DateDeSerializer extends JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonParser jsonParser, DeserializationContext ctx)
				throws IOException, JsonProcessingException {
	        ObjectCodec oc = jsonParser.getCodec();
	        String iso8601Date = oc.readValue(jsonParser, String.class);
			try {
				return ISO8601.toCalendar(iso8601Date).getTime();
			} catch (ParseException e) {
				throw new IllegalArgumentException(iso8601Date + " is not in iso8601");
			}
		}
	}
	@Bean
	public ObjectMapper mapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.dateFormat(new ISO8601DateFormat());

        builder.serializerByType(ObjectId.class, new ObjectIdSerializer());
	    builder.deserializerByType(ObjectId.class, new ObjectIdDeSerializer());
	    ObjectMapper mapper = builder.build();
//	    mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
//	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    return mapper;
	}
	
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(mapper()));
    }

    
}

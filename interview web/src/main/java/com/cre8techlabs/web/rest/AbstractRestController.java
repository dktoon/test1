package com.cre8techlabs.web.rest;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cre8techlabs.utils.reflection.EntityUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractRestController<T> implements RestConst {
	@Autowired
	ObjectMapper mapper;
	
	protected static final String NEW = "new";
	private Class<T> clzEntity;

	protected AbstractRestController() {
		Class<?>[] clzs = EntityUtils.findSuperClassParameterType(this.getClass());
		clzEntity = (Class<T>) clzs[0];
	}
	protected abstract MongoRepository<T, String> getCrudRepository();
	protected abstract T createNew(HttpServletRequest request, String id) throws IOException;
	protected T newEntity() throws InstantiationException, IllegalAccessException {
		return clzEntity.newInstance();
	}
	@RequestMapping(method = RequestMethod.GET, headers = ACCEPT_JSON)
	public @ResponseBody HttpEntity<List<T>> listJson(HttpServletResponse response) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		List<T> result = getCrudRepository().findAll();
		
		headers.add(
				CONTENT_RANGE_HEADER,
				getContentRangeValue(0, result.size(),
						new Integer(result.size()).longValue()));
		return new HttpEntity<List<T>>(result, headers);
	}
	protected String getContentRangeValue(Integer firstResult,
			Integer resultCount, Long totalCount) {
		StringBuilder value = new StringBuilder("items " + firstResult + "-");
		if (resultCount == 0) {
			value.append("0");
		} else {
			value.append(firstResult + resultCount - 1);
		}
		value.append("/" + totalCount);
		return value.toString();
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = ACCEPT_JSON)
	public @ResponseBody T getJson(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws Exception {
		if (id.startsWith(NEW)) {
			return createNew(request, id);
		}
			
		return getCrudRepository().findOne(id);
	}
	protected T convert(JsonNode node, T entity) throws InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException {
		if (entity == null) {
			entity = newEntity();
		}
		JsonNode merged = merge(mapper.readTree(mapper.writeValueAsBytes(entity)), node);
		T obj = mapper.readValue(merged.toString(), clzEntity);
		
		return obj;
	}
	
	public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

	    Iterator<String> fieldNames = updateNode.fieldNames();

	    while (fieldNames.hasNext()) {
	        String updatedFieldName = fieldNames.next();
	        JsonNode valueToBeUpdated = mainNode.get(updatedFieldName);
	        JsonNode updatedValue = updateNode.get(updatedFieldName);

	        // If the node is an @ArrayNode
	        if (valueToBeUpdated != null && updatedValue.isArray()) {
	        	((ObjectNode) mainNode).put(updatedFieldName, updatedValue);
	            // running a loop for all elements of the updated ArrayNode
//	            for (int i = 0; i < updatedValue.size(); i++) {
//	                JsonNode updatedChildNode = updatedValue.get(i);
//	                // Create a new Node in the node that should be updated, if there was no corresponding node in it
//	                // Use-case - where the updateNode will have a new element in its Array
//	                if (valueToBeUpdated.size() <= i) {
//	                    ((ArrayNode) valueToBeUpdated).add(updatedChildNode);
//	                }
//	                // getting reference for the node to be updated
//	                JsonNode childNodeToBeUpdated = valueToBeUpdated.get(i);
//	                merge(childNodeToBeUpdated, updatedChildNode);
//	            }
	        // if the Node is an @ObjectNode
	        } else if (valueToBeUpdated != null && valueToBeUpdated.isObject()) {
	            merge(valueToBeUpdated, updatedValue);
	        } else {
	            if (mainNode instanceof ObjectNode) {
	                ((ObjectNode) mainNode).put(updatedFieldName, updatedValue);
	            }
	        }
	    }
	    return mainNode;
	}
	@RequestMapping(method = RequestMethod.POST, headers = ACCEPT_JSON)
	public T update(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody JsonNode jsonNode) throws Exception {
		String id = jsonNode.get("id").asText();
		T inDb = getCrudRepository().findOne(id);
		if (inDb == null) {
			throw new IllegalArgumentException("Entity pass in parameter doesn't exist, and need to be create first");
		}
		T entity = convert(jsonNode, inDb);
		return getCrudRepository().save(entity);
	}
	@RequestMapping(method = RequestMethod.PUT, headers = ACCEPT_JSON)
	public T create(HttpServletResponse response, @Valid @RequestBody T entity) throws Exception {
		
		return getCrudRepository().save(entity);
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = ACCEPT_JSON)
	public boolean deleteJson(HttpServletResponse response, @PathVariable("id") String id) {

		getCrudRepository().delete(id);
		return true;
	}
}

package edu.mayo.cts2.framework.plugin.service.bprdf.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.twinkql.template.TwinkqlTemplate;

@Component("rdfDao")
public class RdfDao {

	@Resource
	private TwinkqlTemplate twinkqlTemplate;
	
	public <T> List<T> selectForList(
			String namespace, 
			String selectId, 
			Map<String,Object> parameters,
			Class<T> requiredType) {
		
		return this.twinkqlTemplate.selectForList(
				namespace, 
				selectId, 
				parameters, 
				requiredType);
	}
	
	public <T> T selectForObject(
			String namespace, 
			String selectId, 
			Map<String,Object> parameters,
			Class<T> requiredType) {
		
		return this.twinkqlTemplate.selectForObject(
				namespace, 
				selectId, 
				parameters, 
				requiredType);
	}
}

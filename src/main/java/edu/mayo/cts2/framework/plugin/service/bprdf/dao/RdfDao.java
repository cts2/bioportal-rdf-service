package edu.mayo.cts2.framework.plugin.service.bprdf.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import edu.mayo.twinkql.template.TwinkqlTemplate;

@Component("rdfDao")
public class RdfDao {
	
	private static Log log = LogFactory.getLog(RdfDao.class);
	
	@Resource
	private TwinkqlTemplate twinkqlTemplate;
	
	public static void main(String[] args){
		RdfDao dao = new RdfDao();
		//dao.setApiKey("9a305fa2-40fb-4bd8-a630-8c201fca3792");
		
		String q = "SELECT ?s ?p ?o " +
		" WHERE { " +
 		"	GRAPH ?g { " +
   		"		?s a <http://www.w3.org/2002/07/owl#Ontology>; " +
   		"		?p ?o " +
   		"	} " +
		"}";		
	}
	
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
}

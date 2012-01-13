package edu.mayo.cts2.framework.plugin.service.bprdf.transform;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.EntryDescription;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.Property;
import edu.mayo.cts2.framework.model.util.ModelUtils;

@Component
public class CodeSystemTransform {
	
	public CodeSystemCatalogEntry transform(ResultSet resultSet){
		CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry();
		
		while(resultSet.hasNext()){
			QuerySolution querySolution = resultSet.next();
			
			String predicateUri = querySolution.get("predicate").toString();
			
			String localPart = this.getLocalNameFromUri(predicateUri);
			
			Property property = new Property();
			property.setPredicate(new PredicateReference());
			property.getPredicate().setName(localPart);
			property.getPredicate().setNamespace(this.getPrefix(predicateUri));
			entry.addProperty(property);
		}
	
		return entry;
	}
	
	private String getPrefix(String namespaceUri){
		return "ns-"+Integer.toString(namespaceUri.hashCode());
	}
	
	private String getLocalNameFromUri(String uri){
		String localPart = null;
		localPart = StringUtils.substringAfterLast(uri, "#");
		if(StringUtils.isBlank(localPart)){
			localPart = StringUtils.substringAfterLast(uri, ":");
		}
		if(StringUtils.isBlank(localPart)){
			localPart = StringUtils.substringAfterLast(uri, "/");
		}
		
		return localPart;
	}

	public CodeSystemCatalogEntrySummary transformSummary(QuerySolution querySolution){
		CodeSystemCatalogEntrySummary entry = new CodeSystemCatalogEntrySummary();
		
		Literal uri = querySolution.getLiteral("uri");
		Literal abbreviation = querySolution.getLiteral("abr");
		Literal label = querySolution.getLiteral("label");
		Literal description = querySolution.getLiteral("description");
		
		entry.setAbout(uri.getString());
		if(abbreviation != null){
			entry.setCodeSystemName(abbreviation.getString());
			entry.setResourceName(abbreviation.getString());
		}
		if(label != null){
			entry.setFormalName(label.getString());
		}
		if(description != null){
			entry.setResourceSynopsis(new EntryDescription());
			entry.getResourceSynopsis().setValue(ModelUtils.toTsAnyType(description.getString()));
		}
		
		return entry;
	}
}

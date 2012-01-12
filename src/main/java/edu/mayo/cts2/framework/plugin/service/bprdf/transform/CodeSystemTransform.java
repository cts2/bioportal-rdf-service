package edu.mayo.cts2.framework.plugin.service.bprdf.transform;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.EntryDescription;
import edu.mayo.cts2.framework.model.util.ModelUtils;

@Component
public class CodeSystemTransform {

	public CodeSystemCatalogEntrySummary transform(QuerySolution querySolution){
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

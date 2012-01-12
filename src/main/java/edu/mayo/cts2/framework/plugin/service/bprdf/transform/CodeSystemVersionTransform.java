package edu.mayo.cts2.framework.plugin.service.bprdf.transform;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.QuerySolution;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;

@Component
public class CodeSystemVersionTransform {

	public CodeSystemVersionCatalogEntry transform(QuerySolution querySolution){
		CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
		//
		
		
		return entry;
	}
}

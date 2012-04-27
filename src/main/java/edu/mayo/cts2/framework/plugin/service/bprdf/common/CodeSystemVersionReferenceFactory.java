package edu.mayo.cts2.framework.plugin.service.bprdf.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;

@Component
public class CodeSystemVersionReferenceFactory {
	
	@Resource
	private IdService idService;
	
	@Resource
	private UrlConstructor urlConstructor;
	
	public CodeSystemVersionReference getCodeSystemVersionReferenceFor(String id){
		CodeSystemVersionName codeSystemVersionName = 
				this.idService.getCodeSystemVersionNameForId(id);
		
		String ontologyId = this.idService.getOntologyIdForId(id);
		
		String csvName = codeSystemVersionName.getName();
		String csName = codeSystemVersionName.getAcronym();
		String csvUri = this.idService.getDocumentUriForId(id);
		String csUri = this.idService.getUriForOntologyId(ontologyId);
		
		NameAndMeaningReference versionRef = new NameAndMeaningReference();
		versionRef.setContent(csvName);
		versionRef.setUri(csvUri);
		versionRef.setHref(this.urlConstructor.createCodeSystemVersionUrl(csName, csvName));
		
		CodeSystemReference codeSystemRef = new CodeSystemReference();
		codeSystemRef.setContent(csName);
		codeSystemRef.setUri(csUri);
		codeSystemRef.setHref(this.urlConstructor.createCodeSystemUrl(csName));
		
		CodeSystemVersionReference ref = new CodeSystemVersionReference();
		ref.setCodeSystem(codeSystemRef);
		ref.setVersion(versionRef);
		
		return ref;
	}

}

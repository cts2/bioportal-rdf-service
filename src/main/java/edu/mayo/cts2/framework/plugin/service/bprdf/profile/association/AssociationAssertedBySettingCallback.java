package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
import edu.mayo.twinkql.result.callback.CallbackContext;
@Component("associationAssertedByCallback")
public class AssociationAssertedBySettingCallback implements AfterResultBinding<AssociationDirectoryEntry> {
    @Resource
    IdService idService;
	@Resource
	private UrlConstructor urlConstructor;
	
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.result.callback.AfterResultBinding#afterBinding(java.lang.Object)
	 */
	@Override
	public void afterBinding(
			AssociationDirectoryEntry bindingResult, 
			CallbackContext context) {
		String codeSystemVersion= null;
		if ((String) context.getQueryParams().get("restrictToCodeSystemVersion") != null) {
			codeSystemVersion= (String) context.getQueryParams().get("restrictToCodeSystemVersion");
		}
		if (StringUtils.isNotBlank(codeSystemVersion)) {
			CodeSystemVersionName csvn= idService.getCodeSystemVersionNameForName(codeSystemVersion);
			String codeSystemName= csvn.getAcronym();
			bindingResult.setAssertedBy(buildCodeSystemVersionReference(codeSystemName, codeSystemVersion));
					    
		}
	}
	
	/**
	 * Builds the code system version reference.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the code system version reference
	 */
	protected CodeSystemVersionReference buildCodeSystemVersionReference(String codeSystemName, String codeSystemVersionName){
		CodeSystemVersionReference ref = new CodeSystemVersionReference();
		
		
		CodeSystemReference codeSystemReference = new CodeSystemReference();
		String codeSystemPath = urlConstructor.createCodeSystemUrl(codeSystemName);

		codeSystemReference.setContent(codeSystemName);
		codeSystemReference.setHref(codeSystemPath);
		//codeSystemReference.setUri(this.getIdentityConverter().getCodeSystemAbout(codeSystemName, BioportalConstants.DEFAULT_ONTOLOGY_ABOUT));
		ref.setCodeSystem(codeSystemReference);
		
		NameAndMeaningReference version = new NameAndMeaningReference();
		version.setContent(codeSystemVersionName);
		version.setHref(urlConstructor.createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName));
			
		ref.setVersion(version);
		
		return ref;
	}


}

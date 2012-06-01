package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
import edu.mayo.twinkql.result.callback.CallbackContext;
@Component("associationEntityHrefCallback")
public class AssociationEntityHrefSettingCallback implements AfterResultBinding<AssociationDirectoryEntry> {
    @Resource
    IdService idService;
	@Resource
	private UrlConstructor urlConstructor;
	
	private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String OWL_NS = "http://www.w3.org/2002/07/owl#";
	
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
			URIAndEntityName entity;
			entity= bindingResult.getSubject();
			this.setHref(entity, codeSystemName, codeSystemVersion);
			entity= bindingResult.getTarget().getEntity();
			this.setHref(entity, codeSystemName, codeSystemVersion);
		    
		}
	}
	
	private void setHref(URIAndEntityName entity, String codeSystemName, String codeSystemVersion) {
		if(! StringUtils.equals(entity.getNamespace(), RDF_NS) && 
				! StringUtils.equals(entity.getNamespace(), OWL_NS)) {
			entity.setHref(urlConstructor.createEntityUrl(codeSystemName, codeSystemVersion, entity.getName()));
		}
	}

}

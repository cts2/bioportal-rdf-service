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
			entity.setHref(urlConstructor.createEntityUrl(codeSystemName, codeSystemVersion, entity.getName()));
			entity= bindingResult.getTarget().getEntity();
			entity.setHref(urlConstructor.createEntityUrl(codeSystemName, codeSystemVersion, entity.getName()));
		    
		}
	}

}

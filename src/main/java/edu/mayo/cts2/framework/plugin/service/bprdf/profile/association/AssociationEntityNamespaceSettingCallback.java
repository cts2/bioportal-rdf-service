package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace.NamespaceLookupService;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
@Component("associationEntityNamespaceCallback")
public class AssociationEntityNamespaceSettingCallback implements AfterResultBinding<AssociationDirectoryEntry> {
    @Resource
	NamespaceLookupService namespaceService;
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.result.callback.AfterResultBinding#afterBinding(java.lang.Object)
	 */
	@Override
	public void afterBinding(
			AssociationDirectoryEntry bindingResult, 
			Map<String,Object> callbackParams) {
		String codeSystemVersion= null;
		if (callbackParams.get("restrictToCodeSystemVersion") != null) {
			codeSystemVersion= callbackParams.get("restrictToCodeSystemVersion").toString();
		}
		if (StringUtils.isNotBlank(codeSystemVersion)) {
		    bindingResult.getSubject().setNamespace(codeSystemVersion);
		    bindingResult.getTarget().getEntity().setNamespace(codeSystemVersion);
		    
		}
//		String predicatePrefix= namespaceService.getPreferredPrefixForUri(bindingResult.getPredicate().getNamespace());
//		bindingResult.getPredicate().setNamespace(predicatePrefix);
	}

}

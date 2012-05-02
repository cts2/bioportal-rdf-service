package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier.NamespaceModifier;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
import edu.mayo.twinkql.result.callback.CallbackContext;

@Component("associationEntityNamespaceCallback")
public class AssociationEntityNamespaceSettingCallback implements AfterResultBinding<AssociationDirectoryEntry> {
    @Resource
    NamespaceModifier namespaceModifier;
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
		    bindingResult.getSubject().setNamespace(codeSystemVersion);
		    bindingResult.getTarget().getEntity().setNamespace(codeSystemVersion);
		    
		}
		try {
		//String  encodedNsURL= URLEncoder.encode(bindingResult.getPredicate().getNamespace(), "UTF-8");
		//String predicatePrefix= namespaceService.getPreferredPrefixForUri(encodedNsURL);
		String predicatePrefix= namespaceModifier.getNamespace(bindingResult.getPredicate().getNamespace());
		bindingResult.getPredicate().setNamespace(predicatePrefix);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

}

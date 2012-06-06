/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.bprdf.common.CodeSystemVersionReferenceFactory;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
import edu.mayo.twinkql.result.callback.CallbackContext;

/**
 * The Class CodeSystemHrefSettingCallback.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("entityDirectoryEntryCallback")
public class EntityDirectoryEntryCallback implements AfterResultBinding<EntityDirectoryEntry> {
	
	@Resource
	private UrlConstructor urlConstructor;
	
	@Resource
	private CodeSystemVersionReferenceFactory codeSystemVersionReferenceFactory;
	
	@Resource
	private IdService idService;
	
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.result.callback.AfterResultBinding#afterBinding(java.lang.Object)
	 */
	@Override
	public void afterBinding(
			EntityDirectoryEntry bindingResult, 
			CallbackContext context) {	
		
		String acronym = 
				(String) context.getQueryParams().get("acronym");	
		
		if(acronym == null){
			acronym = (String) context.getQueryParams().get("restrictToGraph");
		}
		
		if(acronym == null){
			acronym = (String) context.getCallbackIds().get("acronym");
		}
		
		Assert.notNull(acronym);
		
		acronym = StringUtils.upperCase(acronym);
		
		String ontologyId = this.idService.getOntologyIdForAcronym(acronym);
		String id = this.idService.getCurrentIdForOntologyId(ontologyId);
		
		//TODO: this method needs to be in the UrlConstructor
		bindingResult.setHref(
			this.urlConstructor.getServerRootWithAppName() + "/" + URIHelperInterface.ENTITY + "/" + 
					EncodingUtils.encodeScopedEntityName(bindingResult.getName()));
		
		for(DescriptionInCodeSystem description : bindingResult.getKnownEntityDescription()){
			CodeSystemVersionReference ref = this.codeSystemVersionReferenceFactory.getCodeSystemVersionReferenceFor(id);
			description.setHref(
					this.urlConstructor.createEntityUrl(
							ref.getCodeSystem().getContent(), 
							ref.getVersion().getContent(),
							bindingResult.getName()));
			
			description.setDescribingCodeSystemVersion(ref);
		}
	}

}


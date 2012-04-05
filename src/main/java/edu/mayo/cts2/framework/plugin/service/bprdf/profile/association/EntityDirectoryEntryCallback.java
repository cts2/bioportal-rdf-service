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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
import edu.mayo.twinkql.result.callback.CallbackContext;

/**
 * The Class CodeSystemHrefSettingCallback.
 * 
 * @author <a href="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</a>
 */
@Component("entityDirectoryEntryCallback")
public class EntityDirectoryEntryCallback implements
		AfterResultBinding<EntityDirectoryEntry> {


	@Resource
	private IdService idService;
	@Resource
	private UrlConstructor urlConstructor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.twinkql.result.callback.AfterResultBinding#afterBinding(java
	 * .lang.Object)
	 */
	@Override
	public void afterBinding(EntityDirectoryEntry bindingResult,
			CallbackContext context) {

		String acronym = (String) context.getQueryParams().get("restrictToGraph");
		String ontologyId = idService.getOntologyIdForAcronym(acronym);
		String codeSystemVersion_name= (String) context.getQueryParams().get("restrictToCodeSystemVersion");
		String id = idService.getCurrentIdForOntologyId(ontologyId);
		bindingResult.addKnownEntityDescription(getDescriptionInCodeSystem(ontologyId, id, codeSystemVersion_name));
		bindingResult.getName().setNamespace(codeSystemVersion_name);
	}

	protected DescriptionInCodeSystem getDescriptionInCodeSystem(String ontologyId, String id, String codeSystemVersion_name) {
		DescriptionInCodeSystem descCS= new DescriptionInCodeSystem();
		descCS.setDescribingCodeSystemVersion(getCodeSystemVersionReference(ontologyId, id));
		descCS.setDesignation(codeSystemVersion_name);
		return descCS;	
	}
	
	
	
	protected CodeSystemVersionReference getCodeSystemVersionReference(
			String ontologyId, String id) {
		CodeSystemVersionReference versionRef = new CodeSystemVersionReference();

		CodeSystemVersionName codeSystemVersionName = this.idService
				.getCodeSystemVersionNameForId(id);

		// the triple store doesn't have all of these..
		if (codeSystemVersionName == null) {
			return null;
		}

		String versionName = codeSystemVersionName.getName();
		String codeSystemName = codeSystemVersionName.getAcronym();

		NameAndMeaningReference ref = new NameAndMeaningReference();
		ref.setContent(versionName);
		ref.setHref(this.urlConstructor.createCodeSystemVersionUrl(
				codeSystemName, versionName));

		versionRef.setVersion(ref);
		versionRef.setCodeSystem(this.getCodeSystemReference(codeSystemName));
		return versionRef;
	}

	private CodeSystemReference getCodeSystemReference(String codeSystemName) {
		CodeSystemReference codeSystemReference = new CodeSystemReference();
		codeSystemReference.setContent(codeSystemName);
		codeSystemReference.setHref(this.urlConstructor
				.createCodeSystemUrl(codeSystemName));

		return codeSystemReference;
	}
}

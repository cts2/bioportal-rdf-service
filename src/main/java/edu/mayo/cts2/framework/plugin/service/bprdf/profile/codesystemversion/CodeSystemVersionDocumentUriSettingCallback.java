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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion;

import java.util.Map;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;

/**
 * The Class CodeSystemHrefSettingCallback.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("codeSystemVersionDocumentUriCallback")
public class CodeSystemVersionDocumentUriSettingCallback extends AbstractCodeSystemVersionSummaryDocumentUriSettingCallback<CodeSystemVersionCatalogEntry> {

	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.result.callback.AfterResultBinding#afterBinding(java.lang.Object)
	 */
	@Override
	public void afterBinding(
			CodeSystemVersionCatalogEntry bindingResult,
			Map<String,Object> callbackParams) {
		
		String about = (String) callbackParams.get("about");
		
		String version = (String) callbackParams.get(CodeSystemVersionConstants.VERSION_CALLBACK_PARAM);
		
		bindingResult.setDocumentURI(this.getDocumentUri(about, version));
	}
	
}
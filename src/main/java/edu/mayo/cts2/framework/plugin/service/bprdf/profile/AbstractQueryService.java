/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;

/**
 * The Class AbstractQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractQueryService implements BaseQueryService, InitializingBean {

	private Set<VariableTiedModelAttributeReference> modelAttributeReferences;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.modelAttributeReferences = new HashSet<VariableTiedModelAttributeReference>();
		this.doAddSupportedModelAttributes(this.modelAttributeReferences);
	}
	
	/**
	 * Do add supported model attributes.
	 *
	 * @param set the set
	 */
	public abstract void doAddSupportedModelAttributes(Set<VariableTiedModelAttributeReference> set);
	
	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		HashSet<MatchAlgorithmReference> returnSet = new HashSet<MatchAlgorithmReference>();
		
		returnSet.add(StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference());
		
		return returnSet;
	}

	@Override
	public Set<VariableTiedModelAttributeReference> getSupportedModelAttributes() {
		return this.modelAttributeReferences;
	}
	
	/**
	 * Find supported model attribute.
	 *
	 * @param refToLookFor the ref to look for
	 * @return the variable tied model attribute reference
	 */
	public VariableTiedModelAttributeReference findSupportedModelAttribute(ModelAttributeReference refToLookFor) {
		for(VariableTiedModelAttributeReference ref : this.modelAttributeReferences){
			if(ref.getContent().equals(refToLookFor.getContent())){
				return ref;
			}
		}
		
		return null;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		throw new UnsupportedOperationException();
	}

	

}

package edu.mayo.cts2.framework.plugin.service.bprdf.profile;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;

public abstract class AbstractQueryService implements BaseQueryService, InitializingBean {

	private Set<VariableTiedModelAttributeReference> modelAttributeReferences;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.modelAttributeReferences = new HashSet<VariableTiedModelAttributeReference>();
		this.doAddSupportedModelAttributes(this.modelAttributeReferences);
	}
	
	public abstract void doAddSupportedModelAttributes(Set<VariableTiedModelAttributeReference> set);
	
	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<VariableTiedModelAttributeReference> getSupportedModelAttributes() {
		return this.modelAttributeReferences;
	}
	
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

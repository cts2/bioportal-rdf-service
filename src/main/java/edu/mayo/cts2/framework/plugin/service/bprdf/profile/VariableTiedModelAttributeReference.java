package edu.mayo.cts2.framework.plugin.service.bprdf.profile;

import edu.mayo.cts2.framework.model.core.ModelAttributeReference;

public class VariableTiedModelAttributeReference extends ModelAttributeReference{
	
	private static final long serialVersionUID = 6302391653888781044L;
	
	private String variable;

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}
}
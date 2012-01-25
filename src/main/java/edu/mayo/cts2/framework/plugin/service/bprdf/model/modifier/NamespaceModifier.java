package edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier;

import edu.mayo.twinkql.result.callback.Modifier;

public class NamespaceModifier implements Modifier<String> {

	@Override
	public String beforeSetting(String object) {
		return this.getNamespace(object);
	}
	
	protected String getNamespace(String uri){
		String hashString = Integer.toString(uri.hashCode());
		
		return "ns"+hashString;
	}

}

package edu.mayo.cts2.framework.plugin.service.bprdf.callback.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier.UpperCaseModifier;
import edu.mayo.twinkql.result.callback.Modifier;

@Component("codeSystemAboutModifier")
public class CodeSystemAboutModifier implements Modifier<String> {

	@Resource
	private IdService idService;
	
	@Resource
	private UpperCaseModifier upperCaseModifier;
	
	@Override
	public String beforeSetting(String acronym) {
		//manually run the modifiers here. Twinkql should be able to
		//run multiple modifiers, but for now it can only do one.
		acronym = this.upperCaseModifier.beforeSetting(acronym);
		
		return idService.getUriForAcronym(acronym);
	}

}

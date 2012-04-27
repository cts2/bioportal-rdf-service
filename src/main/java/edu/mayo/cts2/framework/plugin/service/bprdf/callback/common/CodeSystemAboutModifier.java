package edu.mayo.cts2.framework.plugin.service.bprdf.callback.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.Modifier;

@Component("codeSystemAboutModifier")
public class CodeSystemAboutModifier implements Modifier<String> {

	@Resource
	private IdService idService;
	
	@Override
	public String beforeSetting(String acronym) {
		return idService.getUriForAcronym(acronym);
	}

	
}

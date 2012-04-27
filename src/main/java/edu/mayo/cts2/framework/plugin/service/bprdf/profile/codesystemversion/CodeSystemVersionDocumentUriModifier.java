package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.Modifier;

@Component("codeSystemVersionDocumentUriModifier")
public class CodeSystemVersionDocumentUriModifier implements Modifier<String> {

	@Resource
	private IdService idService;
	
	@Override
	public String beforeSetting(String id) {
		return idService.getDocumentUriForId(id);
	}

}

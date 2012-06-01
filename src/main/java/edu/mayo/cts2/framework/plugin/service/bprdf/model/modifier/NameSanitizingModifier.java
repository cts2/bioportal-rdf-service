package edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.twinkql.result.callback.Modifier;

@Component("nameSanitizingModifier")
public class NameSanitizingModifier implements Modifier<String> {

	@Override
	public String beforeSetting(String string) {
		if(StringUtils.contains(string, '/')){
			string = StringUtils.replace(string, "/", "-");
		}
		
		return string;
	}

}

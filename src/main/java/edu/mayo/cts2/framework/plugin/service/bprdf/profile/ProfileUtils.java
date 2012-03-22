package edu.mayo.cts2.framework.plugin.service.bprdf.profile;

import java.util.Map;

import edu.mayo.cts2.framework.model.command.Page;

public class ProfileUtils {
	
	private final static String LIMIT = "limit";
	private final static String OFFSET = "offset";

	private ProfileUtils(){
		super();
	}
	
	public static void setLimitOffsetParams(Map<String,Object> parameters, Page page){
		parameters.put(LIMIT, page.getMaxToReturn()+1);
		parameters.put(OFFSET, page.getStart());
	}
}

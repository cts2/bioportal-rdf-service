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
package edu.mayo.cts2.framework.plugin.service.bprdf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.twinkql.template.TwinkqlTemplate;

/**
 * The Class UriUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public final class SparqlUtils {

	private final static int PAGE_SIZE = 100;
	
	private final static String LIMIT = "limit";
	private final static String OFFSET = "offset";

	/**
	 * Instantiates a new uri utils.
	 */
	private SparqlUtils(){
		super();
	}
	
	/**
	 * Gets the namespace name tuple.
	 * The first element in the array will be the name
	 * The seconed element in the array wil be the namespace uri
	 *
	 * @param uri the uri
	 * @return the namespace name tuple
	 */
	public static <T> List<T> iterate(
			TwinkqlTemplate template, 
			String namespace, 
			String query,
			Map<String, Object> parameters,
			Page page,
			Class<T> requiredType){
		int start;
		int end;
		if(parameters == null){
			parameters = new HashMap<String,Object>();
		}
		if(page == null){
			start = 0;
			end = Integer.MAX_VALUE;
		} else {
			start = page.getStart();
			end = page.getEnd();
		}
		
		List<T> totalResults = new ArrayList<T>();
		
		int currentPos = start;
		
		while(currentPos < end){
			setLimitOffsetParams(parameters, currentPos, calculateEnd(currentPos, end));
			List<T> resultPage = 
				template.selectForList(namespace, query, parameters, requiredType);
			
			if(CollectionUtils.isEmpty(resultPage)){
				break;
			} else {
				totalResults.addAll(resultPage);
				currentPos += resultPage.size();
			}
		}
		
		return totalResults;
	}
	
	private static int calculateEnd(int currentPos, int end){
		if(currentPos + PAGE_SIZE < end){
			return currentPos + PAGE_SIZE;
		} else {
			return end;
		}
	}

	public static void setLimitOffsetParams(Map<String,Object> parameters, Page page){
		parameters.put(LIMIT, page.getMaxToReturn()+1);
		parameters.put(OFFSET, page.getStart());
	}
	
	private static void setLimitOffsetParams(Map<String,Object> parameters, int start, int end){
		parameters.put(LIMIT, end - start);
		parameters.put(OFFSET, start);
	}
	
}

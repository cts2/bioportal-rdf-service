/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.plugin.service.bprdf.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.twinkql.template.TwinkqlTemplate;

/**
 * The Class RdfDao.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("rdfDao")
public class RdfDao {

	@Resource
	private TwinkqlTemplate twinkqlTemplate;
	
	/**
	 * Select for list.
	 *
	 * @param <T> the generic type
	 * @param namespace the namespace
	 * @param selectId the select id
	 * @param parameters the parameters
	 * @param requiredType the required type
	 * @return the list
	 */
	public <T> List<T> selectForList(
			String namespace, 
			String selectId, 
			Map<String,Object> parameters,
			Class<T> requiredType) {
		
		return this.twinkqlTemplate.selectForList(
				namespace, 
				selectId, 
				parameters, 
				requiredType);
	}
	
	/**
	 * Select for object.
	 *
	 * @param <T> the generic type
	 * @param namespace the namespace
	 * @param selectId the select id
	 * @param parameters the parameters
	 * @param requiredType the required type
	 * @return the t
	 */
	public <T> T selectForObject(
			String namespace, 
			String selectId, 
			Map<String,Object> parameters,
			Class<T> requiredType) {
		
		return this.twinkqlTemplate.selectForObject(
				namespace, 
				selectId, 
				parameters, 
				requiredType);
	}
}

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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class VariableQueryBuilder.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VariableQueryBuilder {
	
	private VariableQuery variableQuery = new VariableQuery();
	
	/**
	 * Instantiates a new variable query builder.
	 */
	public VariableQueryBuilder(){
		super();
	}
	
	/**
	 * Adds the query.
	 *
	 * @param variable the variable
	 * @param text the text
	 * @return the variable query builder
	 */
	public VariableQueryBuilder addQuery(String variable, String text){
		this.variableQuery.getQueries().add(new Query(variable, text));
		
		return this;
	}
	
	/**
	 * Builds the.
	 *
	 * @return the variable query
	 */
	public VariableQuery build(){
		return this.variableQuery;
	}
	
	/**
	 * The Class VariableQuery.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class VariableQuery {
		
		private List<Query> queries = new ArrayList<Query>();

		public List<Query> getQueries() {
			return queries;
		}

		public void setQueries(List<Query> queries) {
			this.queries = queries;
		}
	}
	
	/**
	 * The Class Query.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class Query {
		
		private String var;
		private String text;

		/**
		 * Instantiates a new query.
		 *
		 * @param var the var
		 * @param text the text
		 */
		public Query(String var, String text) {
			super();
			this.var = var;
			this.text = text;
		}
		public String getVar() {
			return var;
		}
		public void setVar(String var) {
			this.var = var;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
		
	}
}

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
package edu.mayo.cts2.framework.plugin.service.bprdf.dao.id;

import org.apache.commons.lang.StringUtils;

/**
 * The Class CodeSystemName.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeSystemName {
	
	private static final String SEPARATOR = "-";

	private String acronym;
	private String ontologyId;
	
	/**
	 * Parses the.
	 *
	 * @param name the name
	 * @return the code system name
	 */
	public static CodeSystemName parse(String name) {
		String acronym = StringUtils.substringBeforeLast(name, SEPARATOR);
		String ontologyId = StringUtils.substringAfterLast(name, SEPARATOR);
		
		return new CodeSystemName(acronym, ontologyId);
	}

	/**
	 * Instantiates a new code system name.
	 *
	 * @param acronym the acronym
	 * @param ontologyId the ontology id
	 */
	public CodeSystemName(String acronym, String ontologyId) {
		super();
		this.acronym = acronym;
		this.ontologyId = ontologyId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	public String getName(){
		return this.acronym + SEPARATOR + this.ontologyId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acronym == null) ? 0 : acronym.hashCode());
		result = prime * result
				+ ((ontologyId == null) ? 0 : ontologyId.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeSystemName other = (CodeSystemName) obj;
		if (acronym == null) {
			if (other.acronym != null)
				return false;
		} else if (!acronym.equals(other.acronym))
			return false;
		if (ontologyId == null) {
			if (other.ontologyId != null)
				return false;
		} else if (!ontologyId.equals(other.ontologyId))
			return false;
		return true;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public String getOntologyId() {
		return ontologyId;
	}
	public void setOntologyId(String ontologyId) {
		this.ontologyId = ontologyId;
	}
	
	
}

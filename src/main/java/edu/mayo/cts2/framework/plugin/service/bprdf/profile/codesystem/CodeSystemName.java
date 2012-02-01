package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import org.apache.commons.lang.StringUtils;

public class CodeSystemName {
	
	private static final String SEPARATOR = "-";

	private String acronym;
	private String ontologyId;
	
	public static CodeSystemName parse(String name) {
		String[] parts = StringUtils.split(name, SEPARATOR);
		
		return new CodeSystemName(parts[0], parts[1]);
	}

	public CodeSystemName(String acronym, String ontologyId) {
		super();
		this.acronym = acronym;
		this.ontologyId = ontologyId;
	}

	@Override
	public String toString() {
		return this.acronym + SEPARATOR + this.ontologyId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acronym == null) ? 0 : acronym.hashCode());
		result = prime * result
				+ ((ontologyId == null) ? 0 : ontologyId.hashCode());
		return result;
	}
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

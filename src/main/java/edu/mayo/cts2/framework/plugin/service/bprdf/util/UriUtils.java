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

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.rdf.model.impl.Util;

/**
 * The Class UriUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public final class UriUtils {

	/**
	 * Instantiates a new uri utils.
	 */
	private UriUtils(){
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
	public static String[] getNamespaceNameTuple(String uri){
		String name;
		String namespace;
		
		int splitPoint = Util.splitNamespace(uri);
		
		if(splitPoint == uri.length()){
			name = StringUtils.substringAfterLast(uri, "/");
			namespace = StringUtils.substringBeforeLast(uri, "/") + "/";
		} else {
			namespace = uri.substring(0, splitPoint);
			name = uri.substring(splitPoint);
		}
		
		if(StringUtils.isBlank(name) || StringUtils.isBlank(namespace)){
			throw new UriParseException(uri);
		} else {
			return new String[]{name,namespace};
		}
	}

	/**
	 * The Class UriParseException.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class UriParseException extends RuntimeException {

		private static final long serialVersionUID = -1263780369875820164L;
		
		/**
		 * Instantiates a new uri parse exception.
		 *
		 * @param uri the uri
		 */
		private UriParseException(String uri){
			super("URI: " + uri + " cannot be split into a localName/namespace uri pair.");
		}	
	}
}

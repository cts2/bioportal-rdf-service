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
package edu.mayo.cts2.framework.plugin.service.bprdf.osgi;

import org.osgi.framework.BundleContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;

import edu.mayo.twinkql.template.TwinkqlTemplateFactory;

/**
 * A factory for creating OsgiTwinkqlTemplate objects.
 */
public class OsgiTwinkqlTemplateFactory extends TwinkqlTemplateFactory implements BundleContextAware {
	
	private BundleContext bundleContext;
	
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.template.TwinkqlTemplateFactory#decorateContext(org.springframework.context.annotation.AnnotationConfigApplicationContext)
	 */
	protected AnnotationConfigApplicationContext decorateContext(
			AnnotationConfigApplicationContext applicationContext) {
		if(this.bundleContext != null){
			applicationContext.setResourceLoader(
				new OsgiBundleResourcePatternResolver(this.bundleContext.getBundle()));
		}
		
		return applicationContext;
	}

	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}

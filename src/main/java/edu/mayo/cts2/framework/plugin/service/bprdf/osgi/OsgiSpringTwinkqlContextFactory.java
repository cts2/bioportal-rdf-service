package edu.mayo.cts2.framework.plugin.service.bprdf.osgi;

import org.osgi.framework.BundleContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;

import edu.mayo.twinkql.context.SpringTwinkqlContextFactory;

public class OsgiSpringTwinkqlContextFactory 
	extends SpringTwinkqlContextFactory implements BundleContextAware {

	private BundleContext bundleContext;
	
	@Override
	protected PathMatchingResourcePatternResolver createPathMatchingResourcePatternResolver() {
		if(this.bundleContext != null){
			return new OsgiBundleResourcePatternResolver(this.bundleContext.getBundle());
		} else {
			return new PathMatchingResourcePatternResolver();
		}
	}

	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	
}

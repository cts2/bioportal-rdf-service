package edu.mayo.cts2.framework.plugin.service.bprdf.osgi;

import static org.junit.Assert.*;

import org.junit.Test
import org.osgi.framework.BundleContext
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;

class OsgiSpringTwinkqlContextFactoryTest {

	@Test
	void testWithBundleContext(){
		OsgiSpringTwinkqlContextFactory f = new OsgiSpringTwinkqlContextFactory()
		f.setBundleContext([
			getBundle:{null}
		] as BundleContext)
		
		assertTrue f.createPathMatchingResourcePatternResolver() instanceof OsgiBundleResourcePatternResolver
	}
	
	@Test
	void testWithoutBundleContext(){
		OsgiSpringTwinkqlContextFactory f = new OsgiSpringTwinkqlContextFactory()
		
		assertTrue f.createPathMatchingResourcePatternResolver() instanceof PathMatchingResourcePatternResolver
	}
}

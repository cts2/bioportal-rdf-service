package edu.mayo.cts2.framework.plugin.service.bprdf.util;

import static org.junit.Assert.*;

import org.junit.Test

class UriUtilsTest {
	
	@Test
	void TestParseUriWithSlash(){
		def a = UriUtils.getNamespaceNameTuple("http://purl.bioontology.org/ontology/SNOMEDCT/285487006");
	
		assertEquals 2, a.length
		assertEquals "285487006", a[0]
		assertEquals "http://purl.bioontology.org/ontology/SNOMEDCT/", a[1]
	}
	
	@Test
	void TestParseUriWithHash(){
		def a = UriUtils.getNamespaceNameTuple("http://www.ifomis.org/bfo/1.1/span#Occurrent");
	
		assertEquals 2, a.length
		assertEquals "Occurrent", a[0]
		assertEquals "http://www.ifomis.org/bfo/1.1/span#", a[1]
	}
	
	@Test
	void TestParseUriWithHashOther(){
		def a = UriUtils.getNamespaceNameTuple("http://example.org/testing.owl#test103690005");
	
		assertEquals 2, a.length
		assertEquals "test103690005", a[0]
		assertEquals "http://example.org/testing.owl#", a[1]
	}
	
	

}

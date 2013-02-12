NCBO Bioportal RDF CTS2 Plugin
==
A CTS2 implementation based on the [NCBO BioPortal SPARQL endpoint]( http://sparql.bioontology.org/ ). 

Installation
===
Download the CTS2 Framework - ([download page]( http://informatics.mayo.edu/cts2/framework/download/ ))

You can download either a stand-alone server or a war.

Starting the Stand-alone Server
===
    java -jar cts2framework-standalone.jar

The server should start and open up a browser window.

__NOTE:__ Port 8080 will be assumed in further URLs. Change if configured differently.

Click the 'Admin Console' button or navigate to
    
    http://localhost:8080/system/console/bundles
   
__NOTE:__ Default username/password is ```admin/admin```

Getting the Plugin
===

Download
=====
[Download]( http://informatics.mayo.edu/maven/content/repositories/releases/edu/mayo/cts2/framework/bioportal-rdf-service/0.4.0/bioportal-rdf-service-0.4.0.jar) the plugin from the Maven repository.

Build From Source
=====
Ensure 'MAVEN_OPTS' envirnoment variables are set. For example:
    
    export MAVEN_OPTS="-Xmx500m -XX:MaxPermSize=500m"
    
Building/Compiling:
    
    git clone https://github.com/cts2/bioportal-rdf-service.git
    cd bioportal-rdf-service
    maven clean install -Dmaven.test.skip=true
    
__NOTE:__ ```-Dmaven.test.skip=true``` will optionally skip the tests and only assemble the plugin. To run the tests, remove this parameter. If you choose to run the tests, you must specify your Bioportal API Key as a command line parameter: ```-DapiKey=xxxxxx```


Installing the Plugin
===
In the 'Admin Console,' click the 'Install/Update...' button. In the dialog, browse to the bioportal-rdf-service plugin jar. Once selected, click 'Install Or Update.' Ensure that the 'Start Bundle' checkbox is unchecked. Next, click the 'Configuration Manager' tab: 
    http://localhost:8080/system/console/configMgr
Click the 'Bioportal RDF Service' row. Specify your Bioportal API Key and click 'Save.'

Navigate back to the 'Bundles' tab:
     
     http://localhost:8080/system/console/bundles
Find the 'NCBO Bioportal RDF CTS2 Service' bundle and click the 'start' button. This should start the service.

For a simple test:
    
    http://localhost:8080/codesystems
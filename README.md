CytoPhacts
=================

A cytoscape 3 plugin for chemical structure search and pathway links, 
based on the OpenPhacts API

Installation
=================

Step 1:
	git clone the project according to standard github procedure.
	
	
Step 2:
	retrieve dependencies with "ant retrieve". They will be placed in
	the "ivycache" subdirectory
	
Step 3: 
	From your cytoscape 3 installation, gather all api jars. 
	The following command will do it in one step:
	
	cytoscape-unix-3.2.0-SNAPSHOT$ find framework/system/org -iname "*api*.jar" -exec cp '{}' . \;
	
Step 4:
	Create a build.properties file and place it in the same directory as build.xml.
	This build.properties file should define the location of all the API jars, for example:
	
	cytoscape.dir=/home/martijn/opt/cytoscape-unix-3.2.0-SNAPSHOT


Terms of use
============

Authors: Andra Waagmeester, Martijn van Iersel

You may use this code under the terms of, at your option, either the 
Apache 2.0 license (http://www.apache.org/licenses/LICENSE-2.0.html)
or the LGPL-3.0 license (http://www.gnu.org/licenses/lgpl.html)
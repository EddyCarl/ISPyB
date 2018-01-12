# ISPyB Project [![Build Status](https://travis-ci.org/antolinos/ispyb.png)](https://travis-ci.org/antolinos/ispyb)


1. [Installing](#installing)
2. [Versioning](#versioning)
3. [Database creation and update](#database-creation-and-update)
4. [Database schema](#database-schema)
5. [Swarm](#swarm)

# Installing

1. Clone or fork the ISPyB repository and then clone it by typing:
```
git clone https://github.com/ispyb/ISPyB.git
```

2. ISPyB uses some local libraries located on /dependencies then some jars should be added to your local maven repository

```
cd dependencies
mvn install:install-file -Dfile=securityfilter.jar -DgroupId=securityfilter -DartifactId=securityfilter -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=securityaes.jar -DgroupId=securityaes -DartifactId=securityaes -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdf.jar -DgroupId=jhdf -DartifactId=jhdf -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdf5.jar -DgroupId=jhdf5 -DartifactId=jhdf5 -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdf5obj.jar -DgroupId=jhdf5obj -DartifactId=jhdf5obj -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdfobj.jar -DgroupId=jhdfobj -DartifactId=jhdfobj -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=Struts-Layout-1.2.jar -DgroupId=struts-layout -DartifactId=struts-layout -Dversion=1.2 -Dpackaging=jar
mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=ojdbc6 -DartifactId=ojdbc6 -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=ispyb-WSclient-userportal-gen-1.3.jar -DgroupId=ispyb -DartifactId=ispyb-WSclient-userportal-gen -Dversion=1.3 -Dpackaging=jar
```

3. Configure the SITE property

Copy the settings.xml present in ispyb-parent/configuration into ~/.m2/settings.xml if you are using a Linux machine or configure maven accordingly to use this setting file [https://maven.apache.org/settings.html](https://maven.apache.org/settings.html)

For example:
```
<settings>
	<proxies>
		<proxy>
			<id>esrf</id>
			<active>true</active>
			<protocol>http</protocol>
			<host>proxy.esrf.fr</host>
			<port>3128</port>
			<nonProxyHosts>localhost</nonProxyHosts>
		</proxy>
		<proxy>
			<id>esrf</id>
			<active>true</active>
			<protocol>https</protocol>
			<host>proxy.esrf.fr</host>
			<port>3128</port>
			<nonProxyHosts>localhost</nonProxyHosts>
		</proxy>
	</proxies>
	<profiles>
		<profile>
			<id>ESRF</id>
			<properties>
				<ispyb.site>ESRF</ispyb.site>
				<smis.ws.usr>******</smis.ws.usr>
				<smis.ws.pwd>******</smis.ws.pwd>
				<jboss.home>/opt/wildfly</jboss.home>
			</properties>
		</profile>
	</profiles>
	<activeProfiles>
		<activeProfile>ESRF</activeProfile>
	</activeProfiles>
</settings>
```



These properties will set the profile to be used in the ispyb-ejb pom.xml to configure ISPyB.


4. Build the project by using maven
```
mvn clean install
```

If the build has succeed a summary repost should appear:
```
[INFO] Reactor Summary:
[INFO] 
[INFO] ispyb-parent ...................................... SUCCESS [0.251s]
[INFO] ispyb-ejb3 ........................................ SUCCESS [10.243s]
[INFO] ispyb-ws .......................................... SUCCESS [1.751s]
[INFO] ispyb-ui .......................................... SUCCESS [7.212s]
[INFO] ispyb-ear ......................................... SUCCESS [5.048s]
[INFO] ispyb-bcr ......................................... SUCCESS [2.217s]
[INFO] ispyb-bcr-ear ..................................... SUCCESS [1.806s]

```

## Versioning

Use versions:set from the versions-maven plugin:
```
mvn versions:set -DnewVersion=5.0.0
```

If you are happy with the change then:
```
mvn versions:commit
```
Otherwise
```
mvn versions:revert
```


## Database creation and update
Run the creation scripts present in the module ispyb-ejb, to run the scripts you will need a user “pxadmin” with full permissions.

ispyb-ejb/db/scripts/pyconfig.sql
This corresponds to the menu options, and contains both structure and data

ispyb-ejb/db/scripts /pydb.sql
This corresponds to the ISPyB metadata and contains only the database structure.

ispyb-ejb/db/scripts/schemaStatus.sql
This corresponds to the entries present in SchemaStatus table and gives an overview of the executed update scripts.

The creation scripts are normally updated for each tag, but if you are using the trunk version you may have to run the update scripts present in :
ispyb-ejb/db/scripts/ahead

Check before the entries in SchemaStatus table to know which scripts to execute.
The scripts already run for the current tag are in :
ispyb-ejb/db/scripts/passed

### Creating an update script
The 1st line must be:
```
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_06_06_blabla.sql','ONGOING');
```
then the update script

....

and the last line must be:
```
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_06_06_blabla.sql';
```
This allows to keep the SchemaStus table uptodate and to know which scripts have been run.
You can look for examples in ispyb-ejb/db/scripts/passed/2017

### Database schema

Please do not forget to update the database schema :
 https://github.com/ispyb/ISPyB/blob/master/documentation/database/ISPyB_DataModel_5.mwb 
 
This schema can be updated using MySQLWorkbench (free tool from MySQL).


### Swarm

On this branch (wildfly-swarm), the ISByB webservices can be deployed in a Wildfly Swarm application by doing the following steps:
1. Make sure Maven 3.3.X is installed (https://wildfly-swarm.gitbooks.io/wildfly-swarm-users-guide/content/getting-started/system_requirements.html)
2. Materializing the workspace with the 3 main ISPyB projects, add the above dependencies in the ISPyB /dependencies folder
3. Run the following commands in ./ISPyB/ispyb-ejb/
```
mvn clean install
```
4. Install the resulting jar into your maven repository if it is not already there. In /ispyb-ejb/target:
```
mvn install:install-file -Dfile=ispyb-ejb3-5.4.0.jar -DgroupId=ispyb -DartifactId=ispyb-ejb3 -Dversion=5.4.0 -Dpackaging=jar
```
5. Set your datasource and security domain in the https://github.com/belkassaby/ISPyB/blob/wildfly-swarm/ispyb-ws/src/main/resources/project-default.yaml 
More information on creating a datasource for a Sarm application can be found here: https://howto.wildfly-swarm.io/create-a-datasource/
The yaml configuration method information can be found here: https://reference.wildfly-swarm.io/configuration.html
And not implemented yet but here is a link to an article on how to secure a Wildfly Swarm application: https://blog.novatec-gmbh.de/secure-web-application-wildfly-swarm/
6. run the install command on the ispyb-ws project to build the Swarm application: ```mvn clean install```
7. You will find in the target folder of ispyb-ws the following resulting file: ispyb-ws.swarm.jar. It can be executed running the following command:
```
java -jar ispyb-ws.swarm.jar
```






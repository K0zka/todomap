# Introduction #

The jetty gzip plugin is a maven plugin, that runs over your webapp at build time and creates gziped files for your static content (such as txt, javascript, css, xml files).

# Details #

Usage:
```
	<build>
		<plugins>
			<plugin>
				<groupId>org.todomap.tools</groupId>
				<artifactId>maven-jettygzip-plugin</artifactId>
				<version>0.0.1</version>
				<executions>
					<execution>
						<phase>prepare-package</phase>
						<goals><goal>process</goal></goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```
# Grunt Tasks Runner Maven Plugin

## Description

This plugin allows you to easily bind a grunt task to a given Maven phase.


## Usage

Add a plugin section to your pom.xml :

	<build>
		<plugins>
              <plugin>
                    <groupId>com.worldline.maven.plugin</groupId>
                    <artifactId>gruntTaskRunner</artifactId>
                    <version>0.1-SNAPSHOT</version>
                    <executions>
                          <execution>
                                <id>init</id>
                                <goals> <goal>init</goal> </goals>
                          </execution>
                          <execution>
                                <id>run-test</id>
                                <goals>
                                    <goal>run</goal>
                                </goals>
                                <phase>test</phase>
                                <configuration>
                                    <task>my-grunt-task</task>
                                </configuration>
                          </execution>
                    </executions>
              </plugin>
		</plugins>
	</build>

This snippet will run the grunt task "my-grunt-task" when the test phase is reached.


# Copyright

(c) 2013, Worldline By Atos

Written by :
    * Frédéric Langlade-Bellone (a546116) <frederic.langlade@worldline.net>



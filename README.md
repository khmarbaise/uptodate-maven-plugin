UpToDate-Maven-Plugin
=====================

Force the use of the most up-to-date released version of a artifact.

Basic and first idea is described in
http://jira.codehaus.org/browse/MENFORCER-190


Force using the most up-to-date version of an artifact.

Default
  checking all dependencies of the current module if they are up-to-date.


Lets assume you have the following dependency in your pom:


  <dependency>
    <groupId>org.xyz.test</groupId>
    <artifactId>the-artifact</artifact>
    <version>1.3</version>
  </dependency>

Sometime you must be sure to use always the newest version which is available.

Using the uptodate-maven-plugin will exactly check this situation.


If you need to check the parent of a parent to be always uptodate you
could use the parent goal:


 <plugins>
   <plugin>
     <groupId>com.soebes.maven.plugins</groupId>
     <artifactId>uptodate-maven-plugin</artifactId>
     <executions>
       <execution>
         <id>HIER</id>
         <goals>
           <goal>parent</goal>
         </goals>
         <phase>validate</phase>
       </execution>
     </executions>
   </plugin>
   ..
 </plugins>

1. We could check parent

2. We could check dependencies

   We could check only particular dependency.



UpToDate-Maven-Plugin
=====================

License
-------
[Apache License, Version 2.0, January 2004](http://www.apache.org/licenses/)


Overview
--------

Force the use of the most up-to-date released version of a artifact.

[The Basic idea for this plugin was the discussion about the enforcer rule](http://jira.codehaus.org/browse/MENFORCER-190).

Sometime you have the situations like using a parent pom but you yould like to
force the users in your company to use newer versions cause there had been changes
which need for further development. This plugin can help in such situations.

Parent Check
------------

The following pom snippet will show an example of a usual project which 
is using a parent:

    <parent>
      <groupId>com.soebes.maven.plugins.test.integration</groupId>
      <artifactId>parent</artifactId>
      <version>0.2</version>
    </parent>

    <groupId>org.test.parent</groupId>
    <artifactId>root</artifactId>
    <version>1.0.0-SNAPSHOT</version>

So basically no problem at all. But what happens if a new version 
of that parent is released and you want to force people to use that
new version? Simply you add the following definition to your project:

So every time you try to build this plugin will check if
the parent is the newest version. 

     <plugin>
       <groupId>com.soebes.maven.plugins</groupId>
       <artifactId>uptodate-maven-plugin</artifactId>
       <executions>
         <execution>
           <id>force-updrage</id>
           <goals>
             <goal>parent</goal>
           </goals>
           <phase>validate</phase>
         </execution>
       </executions>
     </plugin>

If it is not the newest version it will break the build.
Furtunately you can skip that breaking by using the following
property on command if you really need.

    mvn -Duptodate.skip=true ...

Dependency Check
----------------

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



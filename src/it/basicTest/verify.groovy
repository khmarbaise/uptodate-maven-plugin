import java.awt.Checkbox;
import java.io.*
import java.util.*

import org.apache.maven.model.DistributionManagement;


t = new IntegrationBase()


def getProjectVersion() {
	def pom = new XmlSlurper().parse(new File(basedir, 'pom.xml'))
   
	  def allPlugins = pom.version;
   
	  return pom.version;
}
   
def projectVersion = getProjectVersion();
   
println "Project version: ${projectVersion}"
   

def buildLogFile = new File( basedir, "build.log");

def distTargetNexus = new File (basedir, "dist/target/nexus");

def files = [ 
    "1/root-" + projectVersion + ".pom",
    "2/module-two-" + projectVersion + ".pom",
    "2/module-two-" + projectVersion + ".jar",
    "3/module-one-" + projectVersion + ".pom",
    "3/module-one-" + projectVersion + ".jar",
    "4/dist-" +projectVersion + ".pom",
]

files.each {  
   file -> check = new File(distTargetNexus, file)
   println "checking ${check}"
   if (!check.exists()) {
       throw new FileNotFoundException( "Couldn't find " + file );
   }
}

return true;

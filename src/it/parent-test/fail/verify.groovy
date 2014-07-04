def buildLogFile = new File( basedir, "build.log");

assert buildLogFile.text.contains ('[ERROR] Failed to execute goal com.soebes.maven.plugins:uptodate-maven-plugin:0.1.0-SNAPSHOT:parent (parent-check) on project root: There is a more up-to-date version ( 0.2 ) of the parent available. -> [Help 1]')


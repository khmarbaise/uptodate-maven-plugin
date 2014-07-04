def buildLogFile = new File( basedir, "build.log");
assert buildLogFile.text.contains ('[DEBUG]  Number of existing versions:1 version:0.2' )

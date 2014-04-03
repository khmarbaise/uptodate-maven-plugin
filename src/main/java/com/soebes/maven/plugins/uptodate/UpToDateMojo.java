package com.soebes.maven.plugins.uptodate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.DefaultRepositoryRequest;
import org.apache.maven.artifact.repository.RepositoryRequest;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.impl.ArtifactResolver;

/**
 * @author Karl-Heinz Marbaise <a href="mailto:khmarbaise@soebes.de">khmarbaise@soebes.de</a>
 */
@Mojo( name = "verify", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = true, threadSafe = true )
public class UpToDateMojo
    extends AbstractUpToDateMojo
{

    /**
     * The project currently being build.
     */
    @Component
    private MavenProject mavenProject;

    /**
     * The current Maven session.
     */
    @Component
    private MavenSession mavenSession;

    @Component
    protected ArtifactRepositoryFactory artifactRepositoryFactory;

    @Component( role = ArtifactRepositoryLayout.class )
    private Map<String, ArtifactRepositoryLayout> repositoryLayouts;

    @Component
    private ArtifactResolver resolver;

    @Component
    private ArtifactInstaller installer;

    /**
     * The component used to create artifacts.
     */
    @Component
    private ArtifactFactory artifactFactory;

    @Parameter( defaultValue = "${project.remoteArtifactRepositories}", readonly = true )
    private List<ArtifactRepository> remoteArtifactRepositories;

    /**
     * The component used to create artifacts.
     */
    @Component
    private ArtifactRepositoryFactory repositoryFactory;

    /**
     */
    @Parameter( property = "localRepository", required = true, readonly = true )
    private ArtifactRepository localRepository;

    public void resultArtifact( String groupId, String artifactId, String version, String type )
    {
        Artifact artifact = null;
        artifact = artifactFactory.createArtifact( groupId, artifactId, version, Artifact.SCOPE_COMPILE, type );

      RepositoryRequest repositoryRequest = DefaultRepositoryRequest.getRepositoryRequest( mavenSession, mavenProject ); 
      ArtifactResolutionRequest request = new ArtifactResolutionRequest(repositoryRequest);
      request.setArtifact( artifact );

      ArtifactResolutionResult arr = resolver.resolveArtifact( mavenSession.getRepositorySession(), request )( request );

      ArtifactResolutionResult arr = resolver.resolve( request );
      arr.isSuccess();
        
//        List<String> findVersions = localRepository.findVersions( artifact );
//        if ( findVersions.isEmpty() )
//        {
//            getLog().info( " Didn't found versions" );
//        }
//        else
//        {
//            for ( String foundVersion : findVersions )
//            {
//                getLog().info( "Found version:" + foundVersion );
//            }
//
//        }

//            RepositoryRequest repositoryRequest = 
//            ArtifactResolutionRequest request = new ArtifactResolutionRequest(repositoryRequest);
//            request.setArtifact( artifact );
//
//            resolver.resolve( request );
//            ArtifactResolutionResult arr =
//                    resolver.resolveTransitively(Collections.singleton(artifact), originatingArtifact,
//                            remoteRepositories, localRepository, artifactMetadataSource);
//
//            if (!groupId.equals(artifact.getGroupId()) || !artifactId.equals(artifact.getArtifactId())
//                    || !version.equals(artifact.getVersion())) {
//                artifact =
//                        artifactFactory.createArtifactWithClassifier(groupId, artifactId, version, type, classifier);
//                copyPoms(artifact, testRepository);
//            }
//
//            for (Artifact arrArtifact : (Set<Artifact>) arr.getArtifacts()) {
//                copyArtifact(arrArtifact, testRepository);
//            }

    }

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {

        resultArtifact( "com.soebes.smpp", "smpp", "0.6.3", "pom" );
//        ArtifactHandler artifacthandler = new DefaultArtifactHandler();
//        VersionRange vr = null; //FIXME
//        try
//        {
//            vr = VersionRange.createFromVersionSpec( "[0.6,)" );
//        }
//        catch ( InvalidVersionSpecificationException e )
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Artifact artifact =
//            new DefaultArtifact( "com.soebes.smpp", "smpp", vr, "compile", "jar", null, artifacthandler );
//        ArtifactResolutionRequest request = new ArtifactResolutionRequest();
//        request.setArtifact( artifact );
//        ArtifactResolutionResult resolve = resolver.resolve( request );
//        getLog().info( " Success: " + resolve.isSuccess() );
//        for ( Artifact item : resolve.getArtifacts() )
//        {
//            getLog().info( "Artifact:" + item.getId() );
//        }
//        MavenProject topLevelProject = mavenSession.getTopLevelProject();
//        getLog().info( "TopLevelProject:" + topLevelProject.getId() );

//        ProjectDependencyGraph projectDependencyGraph = mavenSession.getProjectDependencyGraph();
//        List<MavenProject> sortedProjects2 = projectDependencyGraph.getSortedProjects();
//        if ( sortedProjects2 != null && !sortedProjects2.isEmpty() )
//        {
//            for ( MavenProject selectedProject : sortedProjects2 )
//            {
//                getLog().info( "sortedProjects2 projects:" + selectedProject );
//            }
//        }
//
////        RepositorySystemSession repositorySession = mavenSession.getRepositorySession();
////        repositorySession.getLocalRepository().getBasedir();
//
//        //This is for things like mvn -pl a,b,c clean package 
//        List<String> selectedProjects = mavenSession.getRequest().getSelectedProjects();
//        if ( selectedProjects != null && !selectedProjects.isEmpty() )
//        {
//            for ( String selectedProject : selectedProjects )
//            {
//                getLog().info( "Selected projects:" + selectedProject );
//            }
//        }
//
//        List<MavenProject> sortedProjects = mavenSession.getProjects();
//        if ( sortedProjects != null && !sortedProjects.isEmpty() )
//        {
//            for ( MavenProject mavenProject : sortedProjects )
//            {
//                getLog().info( "Project:" + mavenProject.getId() );
//                List<ArtifactVersion> availableVersions = mavenProject.getArtifact().getAvailableVersions();
//                for ( ArtifactVersion artifactVersion : availableVersions )
//                {
//
//                    getLog().info( "  -> version: " + artifactVersion.toString() );
//                }
//            }
//
//            checkReactor( sortedProjects );
//        }
    }

    private void checkReactor( List<MavenProject> sortedProjects )
        throws MojoExecutionException
    {
        List<MavenProject> consistenceCheckResult = isReactorVersionConsistent( sortedProjects );
        if ( !consistenceCheckResult.isEmpty() )
        {
            StringBuilder sb = new StringBuilder().append( SystemUtils.LINE_SEPARATOR );
            for ( MavenProject mavenProject : consistenceCheckResult )
            {
                sb.append( " --> " );
                sb.append( mavenProject.getId() );
                sb.append( SystemUtils.LINE_SEPARATOR );
            }
            throw new MojoExecutionException( "The reactor contains different versions." + sb.toString() );
        }
    }

    private List<MavenProject> isReactorVersionConsistent( List<MavenProject> projectList )
    {
        List<MavenProject> result = new ArrayList<MavenProject>();

        if ( projectList != null && !projectList.isEmpty() )
        {
            String version = projectList.get( 0 ).getVersion();
            getLog().debug( "First version:" + version );
            for ( MavenProject mavenProject : projectList )
            {
                getLog().debug( " -> checking " + mavenProject.getId() );
                if ( !version.equals( mavenProject.getVersion() ) )
                {
                    result.add( mavenProject );
                }
            }
        }
        return result;
    }

}

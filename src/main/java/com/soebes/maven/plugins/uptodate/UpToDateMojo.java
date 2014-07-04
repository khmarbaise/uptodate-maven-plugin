package com.soebes.maven.plugins.uptodate;

import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ResolutionNode;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;

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
    @Parameter( defaultValue = "${project}" )
    private MavenProject mavenProject;

    /**
     * The current Maven session.
     */
    @Parameter( defaultValue = "${session}" )
    private MavenSession mavenSession;

    
    @Component
    private ArtifactHandler artifactHandler;
    
    @Component
    private RepositorySystem repository;

//    @Parameter( defaultValue = "${repositorySystemSession}" )
//    private RepositorySystemSession repositorySystemSession;
//
//    @Parameter( defaultValue = "${project.remoteProjectRepositories}", readonly = true )
//    private List<RemoteRepository> remoteRepositories;
//
//    @Parameter( defaultValue = "${project.remotePluginRepositories}", readonly = true )
//    private List<RemoteRepository> remotePluginRepositories;

    private ArtifactResolutionResult resolve( String groupId, String artifactId, String version, String type) throws InvalidVersionSpecificationException
    {
//        Artifact artifact = new DefaultArtifact( groupId, artifactId, version, Artifact.SCOPE_COMPILE, type, null, null );
//        Artifact artifact = repository.createProjectArtifact( groupId, artifactId, null );
////        Artifact artifact = repoSystem.createArtifact( groupId, artifactId, null, type );
        VersionRange versionRange = VersionRange.createFromVersionSpec( version);
        
        Artifact artifact = new DefaultArtifact( groupId, artifactId, versionRange, Artifact.SCOPE_COMPILE, type, null, artifactHandler );

        ArtifactResolutionRequest request = new ArtifactResolutionRequest();
        request.setArtifact( artifact );
        request.setForceUpdate( true );
        
//        request.setResolveRoot( true ).setResolveTransitively( false );
        request.setServers( mavenSession.getRequest().getServers() );
        request.setMirrors( mavenSession.getRequest().getMirrors() );
        request.setProxies( mavenSession.getRequest().getProxies() );
        request.setLocalRepository( mavenSession.getLocalRepository() );
        request.setRemoteRepositories( mavenSession.getRequest().getRemoteRepositories() );
        request.setResolveTransitively( false );
        return repository.resolve( request );
    }
    
    
    /*
    private File resolve(String artifactDescriptor) {
        String[] s = artifactDescriptor.split(":");

        String type = (s.length >= 4 ? s[3] : "jar");
        Artifact artifact = repository.createArtifact(s[0], s[1], s[2], type);

        ArtifactResolutionRequest request = new ArtifactResolutionRequest();
        request.setArtifact(artifact);
       
        request.setResolveRoot(true).setResolveTransitively(false);
        request.setServers( session.getRequest().getServers() );
        request.setMirrors( session.getRequest().getMirrors() );
        request.setProxies( session.getRequest().getProxies() );
        request.setLocalRepository(session.getLocalRepository());
        request.setRemoteRepositories(session.getRequest().getRemoteRepositories());
        repository.resolve(request);
        return artifact.getFile();
    }
*/

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        ArtifactResolutionResult f;
        try
        {
            f = resolve( "junit", "junit", "[3.8,)", "jar" );
            Set<ResolutionNode> artifactResolutionNodes = f.getArtifactResolutionNodes();
            getLog().info( "Solved: " + f );
            getLog().info(" More: " + artifactResolutionNodes.isEmpty());
            for ( ResolutionNode resolutionNode : artifactResolutionNodes )
            {
                getLog().info(" Node:" + resolutionNode.getArtifact().getId());
            }
            getLog().info( "artifacts Set:" + f.getArtifacts().isEmpty());
            for (Artifact artifact : f.getArtifacts()) {
                getLog().info("Artifact: " + artifact.getId());
            }
        }
        catch ( InvalidVersionSpecificationException e )
        {
            throw new MojoFailureException( "Version Range failed", e );
        }
    }

}

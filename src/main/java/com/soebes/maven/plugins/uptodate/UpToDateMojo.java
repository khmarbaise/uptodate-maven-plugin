package com.soebes.maven.plugins.uptodate;

import java.util.Set;

import org.apache.maven.artifact.Artifact;
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
    private RepositorySystem repoSystem;

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
        Artifact artifact = repoSystem.createProjectArtifact( groupId, artifactId, null );
////        Artifact artifact = repoSystem.createArtifact( groupId, artifactId, null, type );
        VersionRange newRange = VersionRange.createFromVersionSpec( version);
////        artifact.setGroupId( groupId );
////        artifact.setArtifactId( artifactId );
        artifact.setVersionRange( newRange );

        ArtifactResolutionRequest request = new ArtifactResolutionRequest();
        request.setArtifact( artifact );
        
        request.setResolveRoot( true ).setResolveTransitively( false );
        request.setServers( mavenSession.getRequest().getServers() );
        request.setMirrors( mavenSession.getRequest().getMirrors() );
        request.setProxies( mavenSession.getRequest().getProxies() );
        request.setLocalRepository( mavenSession.getLocalRepository() );
        request.setRemoteRepositories( mavenSession.getRequest().getRemoteRepositories() );
        return repoSystem.resolve( request );
    }

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        ArtifactResolutionResult f;
        try
        {
            f = resolve( "org.junit", "junit", "[3.8,)", "jar" );
            Set<ResolutionNode> artifactResolutionNodes = f.getArtifactResolutionNodes();
            getLog().info( "Solved: " + f );
            getLog().info(" More: " + artifactResolutionNodes.isEmpty());
            for ( ResolutionNode resolutionNode : artifactResolutionNodes )
            {
                getLog().info(" Node:" + resolutionNode.getArtifact().getId());
            }
        }
        catch ( InvalidVersionSpecificationException e )
        {
            throw new MojoFailureException( "Version Range failed", e );
        }
    }

}

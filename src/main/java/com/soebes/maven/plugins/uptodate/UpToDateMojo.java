package com.soebes.maven.plugins.uptodate;

import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ResolutionNode;
import org.apache.maven.artifact.versioning.ArtifactVersion;
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

    
    private void getNewestVersions() {
        
        ArtifactVersions versions = getHelper().lookupArtifactVersions( artifact, false );
        ArtifactVersion[] newer =
                        versions.getNewerVersions( version, segment, Boolean.TRUE.equals( allowSnapshots ) );
    }

    private ArtifactResolutionResult resolve( String groupId, String artifactId, String version, String type )
        throws InvalidVersionSpecificationException
    {
         VersionRange versionRange = VersionRange.createFromVersionSpec( "[" + version + ",)");
//        VersionRange versionRange = VersionRange.createFromVersion( version );

        Artifact artifact =
            new DefaultArtifact( groupId, artifactId, versionRange, Artifact.SCOPE_COMPILE, type, null, artifactHandler );

        ArtifactResolutionRequest request = new ArtifactResolutionRequest();
        request.setArtifact( artifact )
            .setResolveRoot( true )
            .setResolveTransitively( false )
            .setServers( mavenSession.getRequest().getServers() )
            .setMirrors( mavenSession.getRequest().getMirrors() )
            .setProxies( mavenSession.getRequest().getProxies() )
            .setLocalRepository( mavenSession.getLocalRepository() )
            .setRemoteRepositories( mavenSession.getRequest().getRemoteRepositories() );

        return repository.resolve( request );
    }

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {

        ArtifactResolutionResult f;
        try
        {
             f = resolve( "junit", "junit", "3.8", "jar" );
//            f = resolve( "org.hibernate", "hibernate", "3.5.1-Final", "pom" );

            for ( ArtifactResolutionException exceptions : f.getErrorArtifactExceptions() )
            {
                getLog().error( "Failed:", exceptions );
            }
            Set<ResolutionNode> artifactResolutionNodes = f.getArtifactResolutionNodes();
            getLog().info( "Solved: " + f );
            getLog().info( " More: " + artifactResolutionNodes.isEmpty() );
            for ( ResolutionNode resolutionNode : artifactResolutionNodes )
            {
                getLog().info( " Node:" + resolutionNode.getArtifact().getId() );
            }
            getLog().info( "artifacts Set:" + f.getArtifacts().isEmpty() );
            for ( Artifact artifact : f.getArtifacts() )
            {
                getLog().info( "Artifact: " + artifact.getId() );
            }
        }
        catch ( InvalidVersionSpecificationException e )
        {
            throw new MojoFailureException( "Version Range failed", e );
        }
    }

}

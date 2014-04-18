package com.soebes.maven.plugins.uptodate;

import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

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
    private RepositorySystem repoSystem;
    
    @Parameter(defaultValue="${repositorySystemSession}")
    private RepositorySystemSession repositorySystemSession;
    

    @Parameter(defaultValue="${project.remoteProjectRepositories}", readonly=true)
    private List<RemoteRepository> remoteRepositories;
    
    
    @Parameter(defaultValue="${project.remotePluginRepositories}", readonly=true)
    private List<RemoteRepository> remotePluginRepositories;
    
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {

        Artifact artifact = new DefaultArtifact( "com.soebes.smpp:smpp:[0,)" );

        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact( artifact );
        rangeRequest.setRepositories( remoteRepositories );

        VersionRangeResult rangeResult;
        try
        {
            rangeResult = repoSystem.resolveVersionRange( repositorySystemSession, rangeRequest );
        }
        catch ( VersionRangeResolutionException e )
        {
            // TODO Auto-generated catch block
            throw new MojoFailureException( "Failed", e );
        }

        List<Version> versions = rangeResult.getVersions();

        if (versions !=null && !versions.isEmpty()) {
            for ( Version version : versions )
            {
                getLog().info( " Item:" + version );
            }
        }
    }

}

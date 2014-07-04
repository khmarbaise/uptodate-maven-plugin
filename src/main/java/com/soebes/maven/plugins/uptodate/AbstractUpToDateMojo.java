package com.soebes.maven.plugins.uptodate;

import java.util.Iterator;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
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

public abstract class AbstractUpToDateMojo
    extends AbstractMojo
{

    @Parameter( defaultValue = "false", property = "uptodate.skip" )
    private boolean skip;

    /**
     * The project currently being build.
     */
    @Parameter( defaultValue = "${project}" )
    private MavenProject mavenProject;

    /**
     * The project's remote repositories to use for the resolution.
     */
    @Parameter( defaultValue = "${project.remoteProjectRepositories}" )
    private List<RemoteRepository> remoteRepos;

    /**
     * The current Maven session.
     */
    @Parameter( defaultValue = "${session}" )
    private MavenSession mavenSession;

    @Parameter( defaultValue = "${repositorySystemSession}" )
    private RepositorySystemSession repositorySystemSession;

    @Component
    private RepositorySystem repository;

    public boolean isSkip()
    {
        return skip;
    }

    public void setSkip( boolean skip )
    {
        this.skip = skip;
    }

    public MavenProject getMavenProject()
    {
        return mavenProject;
    }

    public List<RemoteRepository> getRemoteRepos()
    {
        return remoteRepos;
    }

    public MavenSession getMavenSession()
    {
        return mavenSession;
    }

    public RepositorySystemSession getRepositorySystemSession()
    {
        return repositorySystemSession;
    }

    public RepositorySystem getRepository()
    {
        return repository;
    }

    protected List<Version> getVersionsOfArtifact( String groupId, String artifactId, String version,
                                                   String classifier, String extension )
        throws VersionRangeResolutionException
    {
        // Create a version range from our current version..
        String versionRange = "[" + version + ",)";

        String requestExtension = ( extension == null ) ? "" : ":" + extension;
        String requestClassifier = ( classifier == null ) ? "" : ":" + classifier;

        // {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
        Artifact artifact =
            new DefaultArtifact( groupId + ":" + artifactId + requestExtension + requestClassifier + ":" + versionRange );
        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact( artifact );
        rangeRequest.setRepositories( remoteRepos );

        VersionRangeResult rangeResult;
        rangeResult = repository.resolveVersionRange( repositorySystemSession, rangeRequest );
        List<Version> versions = rangeResult.getVersions();

        return versions;
    }

    public String join( List<Version> versions )
    {
        StringBuilder sb = new StringBuilder();
        for ( Iterator<Version> iterator = versions.iterator(); iterator.hasNext(); )
        {
            Version version = iterator.next();
            sb.append( version );
            if ( iterator.hasNext() )
            {
                sb.append( "," );
            }
        }
        return sb.toString();
    }

}

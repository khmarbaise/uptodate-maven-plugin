package com.soebes.maven.plugins.uptodate;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

/**
 * Will check dependencies of your project
 * and fail the build if they are not up-to-date.
 * 
 * @author Karl-Heinz Marbaise <a href="mailto:khmarbaise@soebes.de">khmarbaise@soebes.de</a>
 */
@Mojo( name = "dependency", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = true, threadSafe = true )
public class DependencyMojo
    extends AbstractUpToDateMojo
{

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {

        if ( isSkip() )
        {
            getLog().info( " Skipping execution based on user request." );
            return;
        }

        try
        {
            List<Dependency> dependencies = getMavenProject().getDependencies();
            for ( Dependency dependency : dependencies )
            {
                String id = dependency.getGroupId() + ":" + dependency.getArtifactId() + ":" + dependency.getVersion();
                VersionRangeResult versionRangeResult =
                    getNewerVersionsOfArtifact( dependency.getGroupId(), dependency.getArtifactId(),
                                           dependency.getVersion(), dependency.getClassifier(), dependency.getType() );

                getLog().debug( "Dependency: " + id + " Number of existing versions:" + versionRangeResult.getVersions().size() + " version:" + join( versionRangeResult.getVersions() ) );

                Version newestVersion = versionRangeResult.getHighestVersion();
                if ( newestVersion.equals( dependency.getVersion() ) )
                {
                    throw new MojoExecutionException( "There is a more up-to-date version ( " + newestVersion
                        + " ) of the dependency " + id + " available." );
                }
            }
        }
        catch ( VersionRangeResolutionException e )
        {
            getLog().error( "VersionRangeRosolutionException", e );
        }

    }

}

package com.soebes.maven.plugins.uptodate;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

/**
 * Will check the parent of your project and fail the
 * build if the parent is not the newest available
 * version of the parent.
 * @author Karl-Heinz Marbaise <a href="mailto:khmarbaise@soebes.de">khmarbaise@soebes.de</a>
 */
@Mojo( name = "parent", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = true, threadSafe = true )
public class ParentMojo
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

        MavenProject parentProject = getMavenSession().getCurrentProject().getParent();

        if ( parentProject != null && getMavenProject().isExecutionRoot())
        {

            try
            {
                String id = parentProject.getGroupId() + ":" + parentProject.getArtifactId() + ":" + parentProject.getVersion();
                VersionRangeResult versionRangeResult =
                    getNewerVersionsOfArtifact( parentProject.getGroupId(), parentProject.getArtifactId(),
                                           parentProject.getVersion(), null, "pom" );
                getLog().debug( id + " existing versions:" + versionRangeResult.getVersions().size() + " version:" + join( versionRangeResult.getVersions() ) );

                Version newestVersion = versionRangeResult.getHighestVersion();
                if ( newestVersion.equals( parentProject.getVersion() ) )
                {
                    throw new MojoExecutionException( "There is a more up-to-date version ( " + newestVersion
                        + " ) of the parent available." );
                }
            }
            catch ( VersionRangeResolutionException e )
            {
                getLog().error( "VersionRangeRosolutionException", e );
            }
        }
        else
        {
            getLog().debug( "This project does not have a parent." );
        }

    }

}

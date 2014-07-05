package com.soebes.maven.plugins.uptodate;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.Version;

/**
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

//        getMavenProject().isExecutionRoot();
        if ( parentProject != null )
        {

            try
            {
                List<Version> versions =
                    getVersionsOfArtifact( parentProject.getGroupId(), parentProject.getArtifactId(),
                                           parentProject.getVersion(), null, "pom" );
                getLog().debug( " Number of existing versions:" + versions.size() + " version:" + join( versions ) );

                if ( versions.size() > 1 )
                {
                    Version newest = versions.get( versions.size() - 1 );
                    throw new MojoExecutionException( "There is a more up-to-date version ( " + newest
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

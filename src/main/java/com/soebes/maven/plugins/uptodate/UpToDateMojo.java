package com.soebes.maven.plugins.uptodate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * @author Karl-Heinz Marbaise <a href="mailto:khmarbaise@soebes.de">khmarbaise@soebes.de</a>
 */
@Mojo( name = "verify", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, threadSafe = true )
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
    private ArtifactInstaller installer;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        MavenProject topLevelProject = mavenSession.getTopLevelProject();
        getLog().info( "TopLevelProject:" + topLevelProject.getId() );
        List<MavenProject> sortedProjects = mavenSession.getProjects();

        ProjectDependencyGraph projectDependencyGraph = mavenSession.getProjectDependencyGraph();
        projectDependencyGraph.getSortedProjects();

        //This is for things like mvn -pl a,b,c clean package 
        List<String> selectedProjects = mavenSession.getRequest().getSelectedProjects();
        if ( selectedProjects != null && !selectedProjects.isEmpty() )
        {
            for ( String selectedProject : selectedProjects )
            {
                getLog().info( "Selected projects:" + selectedProject );
            }
        }
        if ( sortedProjects != null && !sortedProjects.isEmpty() )
        {
            for ( MavenProject mavenProject : sortedProjects )
            {
                getLog().info( "Project:" + mavenProject.getId() );
            }

            checkReactor( sortedProjects );
        }
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

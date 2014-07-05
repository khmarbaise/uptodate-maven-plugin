package com.soebes.maven.plugins.uptodate;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.Version;
import org.fest.util.Collections;
import org.testng.annotations.Test;

public class DependencyMojoTest
    extends TestBase
{

    @Test
    public void shouldNotFailCauseNoDependencyExist()
        throws MojoExecutionException, MojoFailureException, VersionRangeResolutionException
    {
        DependencyMojo mojo = mock( DependencyMojo.class );
        when( mojo.getLog() ).thenReturn( mock( Log.class ) );

        List<Version> versionsList = createVersionList( "1.0", "1.1" );

        when( mojo.getVersionsOfArtifact( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenReturn( versionsList );

        MavenProject project = mock( MavenProject.class );

        when( project.getDependencies() ).thenReturn( java.util.Collections.<Dependency>emptyList());

        when( mojo.getMavenProject() ).thenReturn( project );

        doCallRealMethod().when( mojo ).execute();
        mojo.execute();
    }

    @Test( expectedExceptions = MojoExecutionException.class, expectedExceptionsMessageRegExp = "There is a more up-to-date version \\( 1\\.1 \\) of the dependency com.soebes.maven.plugins.uptodate:dep-01:1.0 available." )
    public void shouldFailCauseANewerVersionOfTheDependencyExist()
        throws MojoExecutionException, MojoFailureException, VersionRangeResolutionException
    {
        DependencyMojo mojo = mock( DependencyMojo.class );
        when( mojo.getLog() ).thenReturn( mock( Log.class ) );

        List<Version> versionsList = createVersionList( "1.0", "1.1" );

        when( mojo.getVersionsOfArtifact( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenReturn( versionsList );

        MavenProject project = mock( MavenProject.class );

        Dependency dep1 = createMockDependency( "com.soebes.maven.plugins.uptodate", "dep-01", "1.0" );

        List<Dependency> dependencyList = Collections.list( dep1 );
        when( project.getDependencies() ).thenReturn( dependencyList );

        when( mojo.getMavenProject() ).thenReturn( project );

        doCallRealMethod().when( mojo ).execute();
        mojo.execute();
    }

    @Test( expectedExceptions = MojoExecutionException.class, expectedExceptionsMessageRegExp = "There is a more up-to-date version \\( 1\\.1 \\) of the dependency com.soebes.maven.plugins.uptodate:dep-02:1.0 available." )
    public void shouldFailCauseANewerVersionOfTheSecondDependencyExist()
        throws MojoExecutionException, MojoFailureException, VersionRangeResolutionException
    {
        DependencyMojo mojo = mock( DependencyMojo.class );
        when( mojo.getLog() ).thenReturn( mock( Log.class ) );

        MavenProject project = mock( MavenProject.class );

        Dependency dep1 = createMockDependency( "com.soebes.maven.plugins.uptodate", "dep-01", "1.0" );
        List<Version> versionsListDep1 = createVersionList( "1.0" );

        Dependency dep2 = createMockDependency( "com.soebes.maven.plugins.uptodate", "dep-02", "1.0" );
        List<Version> versionsListDep2 = createVersionList( "1.0", "1.1" );

        List<Dependency> dependencyList = Collections.list( dep1, dep2 );
        when( project.getDependencies() ).thenReturn( dependencyList );

        when( mojo.getVersionsOfArtifact( anyString(), anyString(), anyString(), anyString(), anyString() ) ).thenReturn( versionsListDep1,
                                                                                                                          versionsListDep2 );
        when( mojo.getMavenProject() ).thenReturn( project );

        doCallRealMethod().when( mojo ).execute();
        mojo.execute();
    }

}

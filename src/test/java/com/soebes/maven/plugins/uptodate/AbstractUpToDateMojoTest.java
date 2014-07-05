package com.soebes.maven.plugins.uptodate;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.eclipse.aether.version.Version;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbstractUpToDateMojoTest
    extends TestBase
{

    private AbstractUpToDateMojo mojo;

    @BeforeMethod
    public void beforeMethod()
    {
        mojo = mock( AbstractUpToDateMojo.class, Mockito.CALLS_REAL_METHODS );
    }

    @Test
    public void shouldResultInListJoinedByCommaOneElement()
    {
        List<Version> versionsList = createVersionList( "1.0" );

        assertThat( mojo.join( versionsList ) ).isEqualTo( "1.0" );
    }

    @Test
    public void shouldResultInListJoinedByCommaTwoElements()
    {
        List<Version> versionsList = createVersionList( "1.0", "1.1" );

        assertThat( mojo.join( versionsList ) ).isEqualTo( "1.0,1.1" );
    }

    @Test
    public void shouldResultInListJoinedByCommaThreeElements()
    {
        List<Version> versionsList = createVersionList( "1.0", "1.1", "1.2" );

        assertThat( mojo.join( versionsList ) ).isEqualTo( "1.0,1.1,1.2" );
    }

}

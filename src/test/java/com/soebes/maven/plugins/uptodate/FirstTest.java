package com.soebes.maven.plugins.uptodate;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class FirstTest
{
    private String[] listOfProjects = new String[] { "M1", "M2", "M3", "M4", "M5", "M6" };

    @Test
    public void firstTest()
    {
        List<String> resultList = new ArrayList<String>();
        int counter = listOfProjects.length;
        for ( String string : listOfProjects )
        {
            String result = String.valueOf( counter ) + "/" + string;
            resultList.add( result );
            counter--;
        }
        assertThat( resultList ).hasSize( 6 );
        assertThat( resultList ).containsExactly( "6/M1", "5/M2", "4/M3", "3/M4", "2/M5", "1/M6" );
    }

    @Test
    public void secondTest()
    {
        List<String> resultList = new ArrayList<String>();

        List<String> listOfWishedModules = Arrays.asList( "M1", "M2" );
        int counter = listOfWishedModules.size();
        for ( String string : listOfProjects )
        {
            if ( !listOfWishedModules.contains( string ) )
            {
                continue;
            }
            String result = String.valueOf( counter ) + "/" + string;
            resultList.add( result );
            counter--;
        }
        assertThat( resultList ).hasSize( 2 );
        assertThat( resultList ).containsExactly( "2/M1", "1/M2" );
    }
}

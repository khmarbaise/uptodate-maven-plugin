package com.soebes.maven.plugins.uptodate;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;

import com.jcabi.aether.Aether;

/**
 * @author Karl-Heinz Marbaise <a
 *         href="mailto:khmarbaise@soebes.de">khmarbaise@soebes.de</a>
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.VALIDATE, requiresProject = true, threadSafe = true)
public class UpToDateMojo extends AbstractUpToDateMojo {

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

    @Parameter(defaultValue = "${repositorySystemSession}")
    private RepositorySystemSession repositorySystemSession;

    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
    private List<RemoteRepository> remoteRepositories;

    @Parameter(defaultValue = "${project.remotePluginRepositories}", readonly = true)
    private List<RemoteRepository> remotePluginRepositories;

    private String localRepo;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Artifact artifact = new DefaultArtifact("junit", "junit-dep", "", "jar", "4.10");
        File repo = this.repositorySystemSession.getLocalRepository().getBasedir();
        try {
            Collection<Artifact> deps = new Aether(this.mavenProject, repo).resolve(artifact, JavaScopes.COMPILE);
        } catch (DependencyResolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

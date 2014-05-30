package com.worldline.maven.plugin.externalTasksRunner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;


@Mojo( name = "init", defaultPhase = LifecyclePhase.INITIALIZE )
public class SubTaskInitializerMojo extends AbstractMojo {
    @Parameter( defaultValue = "${os.name}", readonly = true)
    String osName;

    @Parameter( defaultValue = "npm", required = true )
    String initExecutable; // ie npm

    @Parameter( defaultValue = "install", required = true )
    String initTask; // ie install

    /** @parameter default-value="${project}" */
    private org.apache.maven.project.MavenProject mavenProject;

    public void execute() throws MojoExecutionException {
        executeCommand( initExecutable + " " + initTask );
    }

    /** Base working directory.
     *
     * @parameter default-value="${project.basedir}"
     * @required
     * @readonly
     */
	@Parameter(property = "external-tasks-maven.basedir", defaultValue = "${project-basedir}")
    protected File basedir;


    void executeCommand(String command) throws MojoExecutionException {
        try {
            if (isWindows()) {
                command = "cmd /c " + command;
            }
            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(basedir);
            executor.execute(cmdLine);
        } catch (IOException e) {
            throw new MojoExecutionException("Error during : " + command, e);
        }
    }


    private boolean isWindows() {
        return osName.startsWith("Windows");
    }
}

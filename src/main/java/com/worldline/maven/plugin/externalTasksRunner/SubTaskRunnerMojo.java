package com.worldline.maven.plugin.externalTasksRunner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/*
 * Copyright 2013 Frédéric Langlade
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
@Mojo( name = "run", defaultPhase = LifecyclePhase.INITIALIZE )
public class SubTaskRunnerMojo extends AbstractMojo {
    @Parameter( defaultValue = "${os.name}", readonly = true)
    String osName;

    @Parameter( required = false )
    String taskRunnerPath;

    @Parameter( required = true)
    String taskRunnerName;

    @Parameter( required = true )
    String task;

    @Parameter( defaultValue = " ", required = true )
    String additionalParameters;


    /** Base working directory.
     *
     * @parameter default-value="${project.basedir}"
     * @required
     * @readonly
     */
	@Parameter(property = "grunt-maven.basedir", defaultValue = "${project-basedir}")
	protected File basedir;


    private org.apache.maven.project.MavenProject mavenProject;

    public void execute() throws MojoExecutionException {
        String params = "";
        if ( System.getProperty("env") != null ) {
            params += "--env=" + System.getProperty("env");
        }
        params += " " + defaultParamsFor(taskRunnerName) + " " + additionalParameters;

        if (taskRunnerPath == null) {
            taskRunnerPath = taskRunnerName;
        }

        executeCommand( taskRunnerPath + " " + task + " " + params, task );
    }

    private String defaultParamsFor(String taskRunnerName) {
        if (taskRunnerName == "grunt") {
            return "--no-color";
        }

        return "";
    }

    void executeCommand( String command, String taskName ) throws MojoExecutionException {
        try {
            if (isWindows()) {
                command = "cmd /c " + command;
            }

            getLog().debug( "Running command : "  + command );
            CommandLine cmdLine = CommandLine.parse( command );
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(basedir);
            executor.execute( cmdLine );
        } catch (ExecuteException e) {
            getLog().error( "Task not found or failure : " + taskName );
            throw new MojoExecutionException( "Task not found or failure : " + taskName );
        } catch (IOException e) {
            throw new MojoExecutionException( "Unknown error when running grunt", e );
        }


    }


    private boolean isWindows() {
        return osName.startsWith("Windows");
    }
}

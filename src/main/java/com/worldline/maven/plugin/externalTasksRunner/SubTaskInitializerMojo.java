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

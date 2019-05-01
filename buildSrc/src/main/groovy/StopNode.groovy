/*
 * Copyright 2019 Rod MacKenzie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

class StopNode extends DefaultTask {

    private final DirectoryProperty javaHome
    private final DirectoryProperty homeDir
    private final DirectoryProperty nodeDir

    StopNode() {
        setGroup("TeamCity Nodes")
        javaHome = getProject().getObjects().directoryProperty()
        homeDir = getProject().getObjects().directoryProperty()
        nodeDir = getProject().getObjects().directoryProperty()
    }

    @InputDirectory
    DirectoryProperty getJavaHome() {
        return javaHome
    }

    @InputDirectory
    DirectoryProperty getHomeDir() {
        return homeDir
    }

    @InputDirectory
    DirectoryProperty getNodeDir() {
        return nodeDir
    }

    @TaskAction
    void start() {
        project.ant.exec(executable: "${homeDir.get()}/bin/teamcity-server.sh") {
            env key: 'JAVA_HOME', path: javaHome.get()
            env key: 'CATALINA_BASE', path: nodeDir.get()
            env key: 'TEAMCITY_LOGS_PATH', path: nodeDir.get().dir('logs')
            arg value: 'stop'
        }
    }
}

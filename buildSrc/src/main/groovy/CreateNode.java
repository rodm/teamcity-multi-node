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

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Transformer;
import org.gradle.api.file.CopySpec;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateNode extends DefaultTask {

    private final Property<Integer> portOffset;
    private final DirectoryProperty homeDir;
    private final DirectoryProperty nodeDir;

    public CreateNode() {
        setGroup("TeamCity Nodes");
        portOffset = getProject().getObjects().property(Integer.class);
        homeDir = getProject().getObjects().directoryProperty();
        nodeDir = getProject().getObjects().directoryProperty();
    }

    @Input
    Property<Integer> getPortOffset() {
        return portOffset;
    }

    @InputDirectory
    DirectoryProperty getHomeDir() {
        return homeDir;
    }

    @OutputDirectory
    DirectoryProperty getNodeDir() {
        return nodeDir;
    }

    @TaskAction
    void create() {
        Project project = getProject();
        File dir = project.mkdir(nodeDir);
        project.mkdir(new File(dir, "lib"));
        project.mkdir(new File(dir, "logs"));
        project.mkdir(new File(dir, "temp"));
        project.mkdir(new File(dir, "work"));

        Path link = dir.toPath().resolve("webapps");
        Path target = homeDir.get().getAsFile().toPath().resolve("webapps");
        try {
            if (link.toFile().exists()) {
                Files.delete(link);
            }
            Files.createSymbolicLink(link, target);
        }
        catch (IOException e) {
            throw new GradleException("Failed to link webapps directory", e);
        }

        File confDir = new File(dir, "conf");
        project.copy(new Action<CopySpec>() {
            @Override
            public void execute(CopySpec copySpec) {
                copySpec.from(new File(homeDir.get().getAsFile(), "conf"));
                copySpec.exclude("**/server.xml");
                copySpec.into(confDir);
            }
        });
        project.copy(new Action<CopySpec>() {
            @Override
            public void execute(CopySpec copySpec) {
                copySpec.from(new File(homeDir.get().getAsFile(), "conf"));
                copySpec.include("**/server.xml");
                copySpec.into(confDir);
                copySpec.filter(new Transformer<String, String>() {
                    @Override
                    public String transform(String line) {
                        if (line.contains("port=\"8111\"")) {
                            return line.replace("8111", Integer.toString(8111 + portOffset.get()));
                        }
                        if (line.contains("port=\"8105\"")) {
                            return line.replace("8105", Integer.toString(8105 + portOffset.get()));
                        }
                        if (line.contains("redirectPort=\"8543\"")) {
                            return line.replace("8543", Integer.toString(8543 + portOffset.get()));
                        }
                        return line;
                    }
                });
            }
        });
    }
}

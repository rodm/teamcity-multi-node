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

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class TeamCityNodesPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        NamedDomainObjectFactory<Node> factory = name -> new Node(name, project.getObjects());
        NamedDomainObjectContainer<Node> nodes = project.container(Node.class, factory);
        ExtensionContainer extensions = project.getExtensions();
        TeamCityNodesExtension extension = extensions.create("teamcityNodes", TeamCityNodesExtension.class, nodes);

        TaskContainer tasks = project.getTasks();
        nodes.all(node -> {
            String name = node.getName();
            String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
            TaskProvider<CreateNode> createTask = tasks.register("create" + capitalizedName, CreateNode.class, task -> {
                task.getPortOffset().set(node.getPortOffset());
                task.getHomeDir().set(extension.getHomeDir());
                task.getNodeDir().set(extension.getNodesDir().dir(name));
            });
            tasks.register("delete" + capitalizedName, DeleteNode.class, deleteNode -> {
                deleteNode.getNodeDir().set(extension.getNodesDir().dir(name));
            });
            tasks.register("start" + capitalizedName, StartNode.class, startNode -> {
                startNode.getJavaHome().set(extension.getJavaHome());
                startNode.getHomeDir().set(extension.getHomeDir());
                startNode.getNodeDir().set(extension.getNodesDir().dir(name));
                startNode.getDataDir().set(extension.getDataDir());
                startNode.getServerOptions().set(node.getServerOptions());
                startNode.dependsOn(createTask);
            });
            tasks.register("stop" + capitalizedName, StopNode.class, stopNode -> {
                stopNode.getJavaHome().set(extension.getJavaHome());
                stopNode.getHomeDir().set(extension.getHomeDir());
                stopNode.getNodeDir().set(extension.getNodesDir().dir(name));
                stopNode.dependsOn(createTask);
            });
        });
    }
}

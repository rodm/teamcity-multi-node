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
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;

import javax.inject.Inject;

public class TeamCityNodesExtension {

    private final DirectoryProperty javaHome;
    private final DirectoryProperty homeDir;
    private final DirectoryProperty nodesDir;
    private final DirectoryProperty dataDir;
    private final NamedDomainObjectContainer<Node> nodes;

    @Inject
    public TeamCityNodesExtension(NamedDomainObjectContainer<Node> nodes, ObjectFactory objects) {
        javaHome = objects.directoryProperty();
        homeDir = objects.directoryProperty();
        nodesDir = objects.directoryProperty();
        dataDir = objects.directoryProperty();
        this.nodes = nodes;
    }

    void setJavaHome(String javaHome) {
        this.javaHome.file(javaHome);
    }

    DirectoryProperty getJavaHome() {
        return javaHome;
    }

    void setHomeDir(String homeDir) {
        this.homeDir.file(homeDir);
    }

    DirectoryProperty getHomeDir() {
        return homeDir;
    }

    void setNodesDir(String nodesDir) {
        this.nodesDir.file(nodesDir);
    }

    DirectoryProperty getNodesDir() {
        return nodesDir;
    }

    void setDataDir(String dataDir) {
        this.dataDir.file(dataDir);
    }

    DirectoryProperty getDataDir() {
        return dataDir;
    }

    void nodes(Action<? super NamedDomainObjectContainer<Node>> action) {
        action.execute(nodes);
    }
}

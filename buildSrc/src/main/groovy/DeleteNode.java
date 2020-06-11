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

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.Destroys;
import org.gradle.api.tasks.TaskAction;

public class DeleteNode extends DefaultTask {

    private final DirectoryProperty nodeDir;

    public DeleteNode() {
        setGroup("TeamCity Nodes");
        nodeDir = getProject().getObjects().directoryProperty();
    }

    @Destroys
    DirectoryProperty getNodeDir() {
        return nodeDir;
    }

    @TaskAction
    void delete() {
        getProject().delete(nodeDir.get());
    }
}

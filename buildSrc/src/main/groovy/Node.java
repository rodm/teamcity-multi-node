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

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public class Node {
    private final String name;
    private final Property<Integer> portOffset;
    private final Property<String> serverOptions;

    public Node(String name, ObjectFactory objectFactory) {
        this.name = name;
        this.portOffset = objectFactory.property(Integer.class);
        this.serverOptions = objectFactory.property(String.class);
        this.serverOptions.set("");
    }

    public String getName() {
        return name;
    }

    public void setPortOffset(int portOffset) {
        this.portOffset.set(portOffset);
    }

    public Property<Integer> getPortOffset() {
        return portOffset;
    }

    public void setServerOptions(String serverOptions) {
        this.serverOptions.set(serverOptions);
    }

    public Property<String> getServerOptions() {
        return serverOptions;
    }
}

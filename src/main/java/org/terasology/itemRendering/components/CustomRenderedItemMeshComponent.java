/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.itemRendering.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.mesh.Mesh;

/**
 * Add this to an entity that already has RenderItemComponent to use this mesh and material for rendering instead of the default block or item mesh.
 */
public class CustomRenderedItemMeshComponent implements Component {
    @Replicate
    public Mesh mesh;
    @Replicate
    public Material material;

    public CustomRenderedItemMeshComponent() {
    }

    public CustomRenderedItemMeshComponent(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }
}

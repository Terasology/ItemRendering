// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.rendering.assets.material.Material;
import org.terasology.engine.rendering.assets.mesh.Mesh;

/**
 * Add this to an entity that already has RenderItemComponent to use this mesh and material for rendering instead of the
 * default block or item mesh.
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

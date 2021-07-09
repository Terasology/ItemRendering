// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import org.terasology.engine.network.Replicate;
import org.terasology.engine.rendering.assets.material.Material;
import org.terasology.engine.rendering.assets.mesh.Mesh;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Add this to an entity that already has RenderItemComponent to use this mesh and material for rendering instead of the default block or item mesh.
 */
public class CustomRenderedItemMeshComponent implements Component<CustomRenderedItemMeshComponent> {
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

    @Override
    public void copy(CustomRenderedItemMeshComponent other) {
        this.mesh = other.mesh;
        this.material = other.material;
    }
}

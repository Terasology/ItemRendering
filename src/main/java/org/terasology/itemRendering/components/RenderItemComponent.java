// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import org.joml.Vector3f;
import org.terasology.engine.math.Pitch;
import org.terasology.engine.math.Roll;
import org.terasology.engine.math.Yaw;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Add this to an entity that will be rendered in the world.  It will attach its location to its owner entity's location.
 */
public class RenderItemComponent implements Component<RenderItemComponent> {
    /**
     * Translation relative to its owner entity's location.
     */
    @Replicate
    public Vector3f translate = new Vector3f(0, 0, 0);
    /**
     * Scaling factor for rendering the item.
     */
    @Replicate
    public float size = 0.3f;
    @Replicate
    public Yaw yaw = Yaw.NONE;
    @Replicate
    public Pitch pitch = Pitch.NONE;
    @Replicate
    public Roll roll = Roll.NONE;

    public RenderItemComponent() {
    }

    /**
     * Create a {@link RenderItemComponent} with specific relative translation.
     *
     * @param translate translation relative to its owner entity's location
     */
    public RenderItemComponent(Vector3f translate) {
        this.translate = translate;
    }

    /**
     * Create a {@link RenderItemComponent} with specific relative translation and scaling factor.
     *
     * @param translate translation to its owner entity's location
     * @param size      scaling factor
     */
    public RenderItemComponent(Vector3f translate, float size) {
        this.translate = translate;
        this.size = size;
    }

    @Override
    public void copyFrom(RenderItemComponent other) {
        this.translate = new Vector3f(other.translate);
        this.size = other.size;
        this.yaw = other.yaw;
        this.pitch = other.pitch;
        this.roll = other.roll;
    }
}

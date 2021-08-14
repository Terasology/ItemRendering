// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.itemRendering.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Add this to an entity and it will bounce up and down. Requires a LocationComponent to work.
 */
public class AnimateBounceComponent implements Component<AnimateBounceComponent> {

    /**
     * The period length of one bounce in game time.
     */
    @Replicate
    public float period = 10f;

    /**
     * The max. height of a bounce in blocks.
     */
    @Replicate
    public float maxHeight = 1f;

    public AnimateBounceComponent() {
    }

    public AnimateBounceComponent(float period, float maxHeight) {
        this.period = period;
        this.maxHeight = maxHeight;
    }

    @Override
    public void copyFrom(AnimateBounceComponent other) {
        this.period = other.period;
        this.maxHeight = other.maxHeight;
    }
}

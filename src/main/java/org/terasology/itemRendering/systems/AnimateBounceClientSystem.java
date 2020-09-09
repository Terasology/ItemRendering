// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.itemRendering.components.AnimateBounceComponent;

/**
 * Continuously animates all entities with an {@link AnimateBounceComponent}.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class AnimateBounceClientSystem extends AnimatedOffsetSystem<AnimateBounceComponent> {

    public AnimateBounceClientSystem() {
        super(AnimateBounceComponent.class);
    }

    @Override
    protected float computeOffset(AnimateBounceComponent bounceComp, float gameTime) {
        float modTime = gameTime / bounceComp.period;
        float dt = modTime % 1f;    // dt goes from 0 to 1
        if (dt > 0.5f) {
            dt = 1f - dt;           // dt now goes from 0 to 0.5 and back
        }
        // squaring its position is equivalent to constant (downward) acceleration
        float relPos = dt * dt;     // relPos is between 0 and 0.25

        // multiply with 4 to get a range from 0 to 1
        return bounceComp.maxHeight * (1 - relPos * 4f);
    }
}

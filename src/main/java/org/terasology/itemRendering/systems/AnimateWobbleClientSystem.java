// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.itemRendering.components.AnimateWobbleComponent;

/**
 * Continuously animates all entities with an {@link AnimateWobbleComponent}.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class AnimateWobbleClientSystem extends AnimatedOffsetSystem<AnimateWobbleComponent> {

    public AnimateWobbleClientSystem() {
        super(AnimateWobbleComponent.class);
    }

    @Override
    protected float computeOffset(AnimateWobbleComponent wobbleComp, float gameTime) {
        float modTime = gameTime * (float) Math.PI * 2f / wobbleComp.period;

        float relPos = (float) -Math.cos(modTime) * 0.5f + 0.5f;
        return wobbleComp.maxHeight * relPos;
    }
}

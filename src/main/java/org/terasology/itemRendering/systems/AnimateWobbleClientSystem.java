/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.itemRendering.systems;

import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
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

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

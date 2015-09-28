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

import java.util.HashMap;
import java.util.Map;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.itemRendering.components.AnimateBounceComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.registry.In;

/**
 * Continuously animates all entities an {@link AnimateBounceComponent}.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class AnimateBounceClientSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    private final Map<EntityRef, Float> offsets = new HashMap<>();

    private static final Float ZERO = Float.valueOf(0);

    @In
    private Time time;

    @In
    private EntityManager entityManager;

    @Override
    public void update(float delta) {
        float gameTime = time.getGameTime();
        for (EntityRef entity : entityManager.getEntitiesWith(AnimateBounceComponent.class, LocationComponent.class)) {

            AnimateBounceComponent bounceComp= entity.getComponent(AnimateBounceComponent.class);
            LocationComponent locComp= entity.getComponent(LocationComponent.class);

            float prevOff = offsets.getOrDefault(entity, ZERO);
            float newOff = computeOffset(bounceComp, gameTime);
            float y = locComp.getLocalPosition().getY();

            locComp.getLocalPosition().setY(y - prevOff + newOff);

            offsets.put(entity, newOff);
            entity.saveComponent(locComp);
        }
    }

    private float computeOffset(AnimateBounceComponent bounceComp, float gameTime) {
        float modTime = gameTime / bounceComp.period;
        float dt = modTime % 1f; // dt moves from 0 to 0.5 and back
        if (dt > 0.5f) {
            dt = 1f - dt;
        }
        // squaring is position is equivalent to constant (downward) acceleration
        float relPos = dt * dt;  // relPos is between 0 and 0.25

        // multiply with 4 to get a range from 0 to 1
        return bounceComp.maxHeight * (1 - relPos * 4f);
    }
}

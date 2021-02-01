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

import org.joml.Vector3fc;
import org.terasology.engine.Time;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.registry.In;

/**
 * Continuously animates all entities based on a given component type.
 */
public abstract class AnimatedOffsetSystem<T extends Component> extends BaseComponentSystem implements UpdateSubscriberSystem {

    private final Map<EntityRef, Float> offsets = new HashMap<>();

    private static final Float ZERO = Float.valueOf(0);

    @In
    private Time time;

    @In
    private EntityManager entityManager;

    private Class<T> componentType;

    public AnimatedOffsetSystem(Class<T> componentType) {
        this.componentType = componentType;
    }

    @Override
    public void update(float delta) {
        float gameTime = time.getGameTime();
        for (EntityRef entity : entityManager.getEntitiesWith(componentType, LocationComponent.class)) {

            T bounceComp = entity.getComponent(componentType);
            LocationComponent locComp= entity.getComponent(LocationComponent.class);

            float prevOff = offsets.getOrDefault(entity, ZERO);
            float newOff = computeOffset(bounceComp, gameTime);
            Vector3fc localPos = locComp.getLocalPosition();

            locComp.setLocalPosition(localPos.x(), localPos.y() - prevOff + newOff, localPos.z());

            offsets.put(entity, newOff);
            entity.saveComponent(locComp);
        }
    }

    protected abstract float computeOffset(T comp, float gameTime);
}

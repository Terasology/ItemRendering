// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.joml.Vector3fc;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.HashMap;
import java.util.Map;

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

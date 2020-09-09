// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.itemRendering.components.AnimateRotationComponent;
import org.terasology.math.geom.Quat4f;

/**
 * Continuously rotates all entities in a location with an AnimateRotationComponent.
 * <p/>
 * Using synchronization will allow all rotated objects at the same speed to always be in the same rotation as each
 * other.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class AnimateRotationClientSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    static final float RADIAN_VALUE = 2f * (float) Math.PI;

    @In
    Time time;
    @In
    EntityManager entityManager;

    @Override
    public void update(float delta) {
        float gameTime = time.getGameTime();
        for (EntityRef entity : entityManager.getEntitiesWith(AnimateRotationComponent.class,
                LocationComponent.class)) {
            AnimateRotationComponent animateRotationComponent = entity.getComponent(AnimateRotationComponent.class);
            LocationComponent locationComponent = entity.getComponent(LocationComponent.class);

            if (animateRotationComponent.isSynchronized) {
                float yaw = animateRotationComponent.yawSpeed != 0 ?
                        ((gameTime / animateRotationComponent.yawSpeed) % 1f) * RADIAN_VALUE : 0;
                float pitch = animateRotationComponent.pitchSpeed != 0 ?
                        ((gameTime / animateRotationComponent.pitchSpeed) % 1f) * RADIAN_VALUE : 0;
                float roll = animateRotationComponent.rollSpeed != 0 ?
                        ((gameTime / animateRotationComponent.rollSpeed) % 1f) * RADIAN_VALUE : 0;
                Quat4f rotationDirection = new Quat4f(yaw, pitch, roll);
                locationComponent.setLocalRotation(rotationDirection);
            } else {
                Quat4f rotationAmount = new Quat4f(animateRotationComponent.yawSpeed * delta * RADIAN_VALUE,
                        animateRotationComponent.pitchSpeed * delta * RADIAN_VALUE,
                        animateRotationComponent.rollSpeed * delta * RADIAN_VALUE);
                Quat4f currentRotation = locationComponent.getLocalRotation();
                currentRotation.mul(rotationAmount);
                currentRotation.normalize();
                locationComponent.setLocalRotation(currentRotation);
            }
            entity.saveComponent(locationComponent);
        }
    }
}

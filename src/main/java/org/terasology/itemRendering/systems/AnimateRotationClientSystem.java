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

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.itemRendering.components.AnimateRotationComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.registry.In;

/**
 * Continuously rotates all entities in a location with an AnimateRotationComponent.
 * <p/>
 * Using synchronization will allow all rotated objects at the same speed to always be in the same rotation as each other.
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
        for (EntityRef entity : entityManager.getEntitiesWith(AnimateRotationComponent.class, LocationComponent.class)) {
            AnimateRotationComponent animateRotationComponent = entity.getComponent(AnimateRotationComponent.class);
            LocationComponent locationComponent = entity.getComponent(LocationComponent.class);

            if (animateRotationComponent.isSynchronized) {
                float yaw = animateRotationComponent.yawSpeed != 0 ? ((time.getGameTime() / animateRotationComponent.yawSpeed) % 1f) * RADIAN_VALUE : 0;
                float pitch = animateRotationComponent.pitchSpeed != 0 ? ((time.getGameTime() / animateRotationComponent.pitchSpeed) % 1f) * RADIAN_VALUE : 0;
                float roll = animateRotationComponent.rollSpeed != 0 ? ((time.getGameTime() / animateRotationComponent.rollSpeed) % 1f) * RADIAN_VALUE : 0;
                Quat4f rotationDirection = new Quat4f(yaw, pitch, roll);
                locationComponent.setLocalRotation(rotationDirection);
            } else {
                Quat4f rotationAmount = new Quat4f(animateRotationComponent.yawSpeed * delta * RADIAN_VALUE, animateRotationComponent.pitchSpeed * delta * RADIAN_VALUE, animateRotationComponent.rollSpeed * delta * RADIAN_VALUE);
                Quat4f currentRotation = locationComponent.getLocalRotation();
                currentRotation.mul(rotationAmount);
                currentRotation.normalize();
                locationComponent.setLocalRotation(currentRotation);
            }
            entity.saveComponent(locationComponent);
        }
    }
}

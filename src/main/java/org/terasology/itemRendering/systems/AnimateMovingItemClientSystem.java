// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.itemRendering.components.AnimatedMovingItemComponent;
import org.terasology.itemRendering.components.RenderItemComponent;
import org.terasology.math.geom.Vector3f;

/**
 * This will take any entity in a location with AnimatedMovingItemComponent and RenderItemComponent and move it between
 * the entry and exit sides in the block space for a location. These entities will move from start towards the center,
 * then from the center to the exit.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class AnimateMovingItemClientSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    @In
    Time time;
    @In
    EntityManager entityManager;

    @Override
    public void update(float delta) {
        for (EntityRef entity : entityManager.getEntitiesWith(AnimatedMovingItemComponent.class,
                LocationComponent.class, RenderItemComponent.class)) {
            AnimatedMovingItemComponent animatedMovingItemComponent =
                    entity.getComponent(AnimatedMovingItemComponent.class);
            LocationComponent locationComponent = entity.getComponent(LocationComponent.class);
            RenderItemComponent renderItemComponent = entity.getComponent(RenderItemComponent.class);

            updateItemLocation(locationComponent, animatedMovingItemComponent, renderItemComponent);
            entity.saveComponent(locationComponent);
        }
    }

    @ReceiveEvent
    public void onUpdateMovingItem(OnChangedComponent event,
                                   EntityRef entityRef,
                                   AnimatedMovingItemComponent animatedMovingItemComponent,
                                   RenderItemComponent renderItemComponent) {
        LocationComponent locationComponent = entityRef.getComponent(LocationComponent.class);
        if (locationComponent != null) {
            updateItemLocation(locationComponent, animatedMovingItemComponent, renderItemComponent);
            entityRef.saveComponent(locationComponent);
        }
    }


    private void updateItemLocation(LocationComponent locationComponent,
                                    AnimatedMovingItemComponent animatedMovingItemComponent,
                                    RenderItemComponent renderItemComponent) {
        float percentToTarget = 1.0f - (float) (animatedMovingItemComponent.arrivalTime - time.getGameTimeInMs())
                / (float) (animatedMovingItemComponent.arrivalTime - animatedMovingItemComponent.startTime);
        if (percentToTarget < 0f) {
            percentToTarget = 0f;
        }
        if (percentToTarget > 1f) {
            percentToTarget = 1f;
        }

        Vector3f relativePosition;
        if (percentToTarget > 0.5f) {
            // 0 - 50%
            relativePosition = animatedMovingItemComponent.exitSide.toDirection().getVector3f();
            percentToTarget -= 0.5f;
        } else {
            // 50% - 0
            relativePosition = animatedMovingItemComponent.entranceSide.toDirection().getVector3f();
            percentToTarget = 0.5f - percentToTarget;
        }

        relativePosition.x *= percentToTarget;
        relativePosition.y *= percentToTarget;
        relativePosition.z *= percentToTarget;

        relativePosition.add(renderItemComponent.translate);

        locationComponent.setLocalPosition(relativePosition);

    }

    @ReceiveEvent
    public void onRemoveMovingItem(BeforeDeactivateComponent event, EntityRef entityRef,
                                   RenderItemComponent renderItemTransform) {
        entityRef.removeComponent(AnimatedMovingItemComponent.class);
    }
}

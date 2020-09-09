// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;
import org.terasology.itemRendering.components.RenderEntitiesComponent;

import java.util.Map;

@RegisterSystem(RegisterMode.CLIENT)
public class RenderEntitiesClientSystem extends RenderOwnedEntityClientSystemBase {

    @In
    WorldProvider worldProvider;
    @In
    EntityManager entityManager;

    @ReceiveEvent
    public void changeEntityRendering(OnChangedComponent event,
                                      EntityRef parentEntity,
                                      RenderEntitiesComponent renderEntitiesComponent) {
        refreshRenderedItems(parentEntity);
    }

    @ReceiveEvent
    public void addEntityRendering(OnActivatedComponent event,
                                   EntityRef parentEntity,
                                   RenderEntitiesComponent renderEntitiesComponent) {
        refreshRenderedItems(parentEntity);
    }

    @ReceiveEvent
    public void removeEntityRendering(BeforeDeactivateComponent event,
                                      EntityRef parentEntity,
                                      RenderEntitiesComponent renderEntitiesComponent) {
        destroyExistingRenderedEntities(renderEntitiesComponent);
    }

    private void refreshRenderedItems(EntityRef parentEntity) {
        RenderEntitiesComponent renderEntitiesComponent = parentEntity.getComponent(RenderEntitiesComponent.class);

        // remove all existing rendered entities
        destroyExistingRenderedEntities(renderEntitiesComponent);

        for (Map.Entry<String, RenderOwnedEntityDetails> item : renderEntitiesComponent.entities.entrySet()) {
            EntityRef newEntity = entityManager.create(item.getKey());
            addRenderingComponents(parentEntity, newEntity, item.getValue());
        }
    }

    private void destroyExistingRenderedEntities(RenderEntitiesComponent renderEntitiesComponent) {
        for (EntityRef entity : renderEntitiesComponent.ownedEntities) {
            entity.destroy();
        }
        renderEntitiesComponent.ownedEntities.clear();
    }
}

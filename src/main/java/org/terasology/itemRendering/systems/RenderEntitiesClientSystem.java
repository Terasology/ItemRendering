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

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.itemRendering.components.RenderEntitiesComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;

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

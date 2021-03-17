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

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.itemRendering.components.RenderInventorySlotsComponent;
import org.terasology.module.inventory.components.InventoryComponent;
import org.terasology.module.inventory.systems.InventoryManager;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;

import java.util.List;

@RegisterSystem(RegisterMode.CLIENT)
public class RenderSlotsClientSystem extends RenderOwnedEntityClientSystemBase {

    @In
    InventoryManager inventoryManager;
    @In
    WorldProvider worldProvider;

    @ReceiveEvent
    public void addRemoveItemRendering(OnChangedComponent event,
                                       EntityRef inventoryEntity,
                                       InventoryComponent inventoryComponent) {
        refreshRenderedItems(inventoryEntity);
    }

    @ReceiveEvent
    public void initExistingItemRendering(OnActivatedComponent event,
                                          EntityRef inventoryEntity,
                                          InventoryComponent inventoryComponent) {
        refreshRenderedItems(inventoryEntity);
    }

    private void refreshRenderedItems(EntityRef inventoryEntity) {
        RenderInventorySlotsComponent renderInventorySlotsComponent = inventoryEntity.getComponent(RenderInventorySlotsComponent.class);

        List<Integer> slots = Lists.newArrayList();
        if (renderInventorySlotsComponent != null) {
            slots = renderInventorySlotsComponent.slots;
        }

        // ensure all non rendered inventory slots have been reset
        for (int slot = 0; slot < inventoryManager.getNumSlots(inventoryEntity); slot++) {
            EntityRef item = inventoryManager.getItemInSlot(inventoryEntity, slot);
            if (!slots.contains(slot) && item.exists()) {
                removeRenderingComponents(item);
            }
        }

        for (int slot : slots) {
            EntityRef item = inventoryManager.getItemInSlot(inventoryEntity, slot);
            if (item.exists()) {
                addRenderingComponents(inventoryEntity, item, renderInventorySlotsComponent);
            }
        }
    }
}

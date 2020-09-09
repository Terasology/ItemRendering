// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;
import org.terasology.inventory.logic.InventoryComponent;
import org.terasology.inventory.logic.InventoryManager;
import org.terasology.itemRendering.components.RenderInventorySlotsComponent;

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
        RenderInventorySlotsComponent renderInventorySlotsComponent =
                inventoryEntity.getComponent(RenderInventorySlotsComponent.class);

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

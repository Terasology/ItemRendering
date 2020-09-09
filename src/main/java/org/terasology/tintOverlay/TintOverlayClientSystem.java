// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.tintOverlay;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.engine.utilities.Assets;

/**
 * Overrides the default icon on an item with a runtime generated icon.  This is a client system because the
 * itemComponent.icon is only sent to clients on intitial component creation.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class TintOverlayClientSystem extends BaseComponentSystem {
    @ReceiveEvent
    public void overlayIconAdded(OnActivatedComponent event, EntityRef entity, TintOverlayIconComponent overlayIcon) {
        updateItemComponentIcon(entity, overlayIcon);
    }

    @ReceiveEvent
    public void overlayIconChanged(OnChangedComponent event, EntityRef entity, TintOverlayIconComponent overlayIcon) {
        updateItemComponentIcon(entity, overlayIcon);
    }

    void updateItemComponentIcon(EntityRef entity, TintOverlayIconComponent overlayIcon) {
        ItemComponent itemComponent = entity.getComponent(ItemComponent.class);
        if (itemComponent != null) {
            itemComponent.icon =
                    Assets.getTexture(TintOverlayTextureProducer.getTintOverlayUri(overlayIcon.texture)).get();
            entity.saveComponent(itemComponent);
        }
    }
}

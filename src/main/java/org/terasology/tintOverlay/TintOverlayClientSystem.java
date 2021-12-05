/*
 * Copyright 2014 MovingBlocks
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
package org.terasology.tintOverlay;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.engine.utilities.Assets;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;

/**
 * Overrides the default icon on an item with a runtime generated icon.  This is a client system because the itemComponent.icon is only sent to clients on intitial component creation.
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
            itemComponent.icon = Assets.getTexture(TintOverlayTextureProducer.getTintOverlayUri(overlayIcon.texture)).get();
            entity.saveComponent(itemComponent);
        }
    }
}

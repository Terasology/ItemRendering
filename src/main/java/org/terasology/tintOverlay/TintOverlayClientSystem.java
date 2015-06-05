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

import org.terasology.asset.Assets;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.ItemComponent;

/**
 * Overrides the default icon on an item with a runtime generated icon.
 */
@RegisterSystem
public class TintOverlayClientSystem extends BaseComponentSystem {
    @ReceiveEvent
    public void overlayIconAdded(OnActivatedComponent event, EntityRef entity, TintOverlayIconComponent overlayIcon, ItemComponent itemComponent) {
        itemComponent.icon = Assets.getTexture(TintOverlayTextureProducer.getTintOverlayUri(overlayIcon.texture)).get();
    }

    @ReceiveEvent
    public void overlayIconChanged(OnChangedComponent event, EntityRef entity, TintOverlayIconComponent overlayIcon, ItemComponent itemComponent) {
        itemComponent.icon = Assets.getTexture(TintOverlayTextureProducer.getTintOverlayUri(overlayIcon.texture)).get();
    }
}

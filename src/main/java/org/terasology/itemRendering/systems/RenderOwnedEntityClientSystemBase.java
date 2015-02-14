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

import org.terasology.RotationUtils;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.itemRendering.components.RenderItemComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Pitch;
import org.terasology.math.Rotation;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.items.BlockItemComponent;

public abstract class RenderOwnedEntityClientSystemBase extends BaseComponentSystem {

    protected void removeRenderingComponents(EntityRef item) {
        item.removeComponent(RenderItemComponent.class);
    }

    protected void addRenderingComponents(EntityRef owningEntity, EntityRef item, RenderOwnedEntityDetails renderOwnedEntityDetails) {
        if (item.exists()) {
            // this is inherently evil,  but multiplayer acts strangely
            item.setOwner(owningEntity);

            RenderItemComponent renderItemTransform = createRenderItemComponent(owningEntity, item, renderOwnedEntityDetails);

            if (item.hasComponent(RenderItemComponent.class)) {
                item.saveComponent(renderItemTransform);
            } else {
                item.addComponent(renderItemTransform);
            }
        }
    }

    public RenderItemComponent createRenderItemComponent(EntityRef owningEntity, EntityRef item, RenderOwnedEntityDetails renderOwnedEntityDetails) {
        RenderItemComponent renderItem = new RenderItemComponent();

        boolean isBlockItem = item.hasComponent(BlockItemComponent.class);

        if (renderOwnedEntityDetails.itemsAreFlat && !isBlockItem) {
            // make it flat
            renderItem.pitch = Pitch.CLOCKWISE_90;
        }

        renderItem.translate = new Vector3f(renderOwnedEntityDetails.translate);
        if (renderOwnedEntityDetails.verticalAlignmentBottom) {
            if (!isBlockItem && renderOwnedEntityDetails.itemsAreFlat) {
                // shift items up half their thickness
                renderItem.translate.y += 0.125f * 0.25f;
            } else {
                renderItem.translate.y += renderOwnedEntityDetails.blockSize * 0.5f;
            }
        }

        if (isBlockItem) {
            renderItem.size = renderOwnedEntityDetails.blockSize;
        } else {
            renderItem.size = renderOwnedEntityDetails.itemSize;
        }

        if (renderOwnedEntityDetails.rotateWithBlock) {
            LocationComponent parentLocationComponent = owningEntity.getComponent(LocationComponent.class);
            WorldProvider worldProvider = CoreRegistry.get(WorldProvider.class);
            if (worldProvider.isBlockRelevant(parentLocationComponent.getWorldPosition())) {
                Block block = worldProvider.getBlock(parentLocationComponent.getWorldPosition());
                Side direction = block.getDirection();
                Rotation blockRotation = RotationUtils.getRotation(direction);
                renderItem.yaw = blockRotation.getYaw();
                renderItem.pitch = blockRotation.getPitch();
                renderItem.roll = blockRotation.getRoll();

                renderItem.translate = RotationUtils.rotateVector3f(renderItem.translate, direction.toDirection());
            }
        }

        return renderItem;
    }
}

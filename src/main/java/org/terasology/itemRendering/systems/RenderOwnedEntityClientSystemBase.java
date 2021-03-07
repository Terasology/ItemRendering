// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.joml.Vector3f;
import org.terasology.RotationUtils;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.math.Side;
import org.terasology.itemRendering.components.RenderItemComponent;
import org.terasology.engine.math.Pitch;
import org.terasology.engine.math.Rotation;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.engine.world.block.family.BlockFamily;
import org.terasology.engine.world.block.family.SideDefinedBlockFamily;
import org.terasology.engine.world.block.items.BlockItemComponent;

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
            BlockComponent blockComponent = owningEntity.getComponent(BlockComponent.class);
            if (blockComponent != null) {
                Side direction = getSideDefinedDirection(blockComponent.getBlock());
                Rotation blockRotation = RotationUtils.getRotation(direction);
                renderItem.yaw = blockRotation.getYaw();
                renderItem.pitch = blockRotation.getPitch();
                renderItem.roll = blockRotation.getRoll();

                renderItem.translate = RotationUtils.rotateVector3f(renderItem.translate, direction.toDirection());
            }
        }

        return renderItem;
    }

    public Side getSideDefinedDirection(Block block) {
        Side blockDirection = Side.FRONT;
        BlockFamily blockFamily = block.getBlockFamily();
        if (blockFamily instanceof SideDefinedBlockFamily) {
            blockDirection = ((SideDefinedBlockFamily) blockFamily).getSide(block);
        }
        return blockDirection;
    }
}

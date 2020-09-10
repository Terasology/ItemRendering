// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.systems;

import org.terasology.math.geom.Vector3f;
import org.terasology.nui.reflection.MappedContainer;

/**
 * Add this to a block that you want items displayed from an inventory category. Also add the RenderItemComponent to
 * adjust the location of the item,  otherwise it will be in the center of the containing block.
 */
@MappedContainer
public class RenderOwnedEntityDetails {
    public Vector3f translate = new Vector3f();
    public float blockSize = 0.3f;
    public float itemSize = 0.3f;
    public boolean itemsAreFlat;
    public boolean verticalAlignmentBottom;
    public boolean rotateWithBlock;

    public void setRenderDetails(RenderOwnedEntityDetails other) {
        translate = other.translate;
        blockSize = other.blockSize;
        itemSize = other.itemSize;
        itemsAreFlat = other.itemsAreFlat;
        verticalAlignmentBottom = other.verticalAlignmentBottom;
        rotateWithBlock = other.rotateWithBlock;
    }
}

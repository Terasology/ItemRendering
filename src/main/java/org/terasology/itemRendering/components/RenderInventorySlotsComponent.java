// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.itemRendering.systems.RenderOwnedEntityDetails;

import java.util.List;

/**
 * Add this to a block that you want items displayed from an inventory category. Also add the RenderItemComponent to
 * adjust the location of the item,  otherwise it will be in the center of the containing block.
 */
public class RenderInventorySlotsComponent extends RenderOwnedEntityDetails implements Component {
    @Replicate
    public List<Integer> slots = Lists.newArrayList();
}

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
package org.terasology.itemRendering.components;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.Component;
import org.terasology.itemRendering.systems.RenderOwnedEntityDetails;
import org.terasology.engine.network.Replicate;

import java.util.List;

/**
 * Add this to a block that you want items displayed from an inventory category.
 * Also add the RenderItemComponent to adjust the location of the item,  otherwise it will be in the center of the containing block.
 */
public class RenderInventorySlotsComponent extends RenderOwnedEntityDetails implements Component {
    @Replicate
    public List<Integer> slots = Lists.newArrayList();
}

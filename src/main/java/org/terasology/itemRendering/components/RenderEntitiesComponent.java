// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.Owns;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.itemRendering.systems.RenderOwnedEntityDetails;

import java.util.List;
import java.util.Map;

public class RenderEntitiesComponent extends RenderOwnedEntityDetails implements Component {
    public Map<String, RenderOwnedEntityDetails> entities = Maps.newHashMap();

    @Owns
    public List<EntityRef> ownedEntities = Lists.newArrayList();
}

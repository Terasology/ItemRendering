// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.engine.entitySystem.Owns;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.itemRendering.systems.RenderOwnedEntityDetails;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class RenderEntitiesComponent extends RenderOwnedEntityDetails implements Component<RenderEntitiesComponent> {
    public Map<String, RenderOwnedEntityDetails> entities = Maps.newHashMap();

    @Owns
    public List<EntityRef> ownedEntities = Lists.newArrayList();

    @Override
    public void copy(RenderEntitiesComponent other) {
        this.entities.clear();
        for (Map.Entry<String, RenderOwnedEntityDetails> entry : other.entities.entrySet()) {
            RenderOwnedEntityDetails value = entry.getValue();
            if (value instanceof Component && value instanceof RenderOwnedEntityDetails) {
                Class cls = value.getClass();
                RenderOwnedEntityDetails newValue =
                        copyRenderOwnedEntityDetails(
                                cls,
                                (RenderOwnedEntityDetails & Component) value);
                this.entities.put(entry.getKey(),
                        newValue);
            } else {
                throw new RuntimeException("Cannot copy non-component RenderOwnedDetails");
            }
        }
        this.ownedEntities = Lists.newArrayList(other.ownedEntities);
    }

    private <T extends RenderOwnedEntityDetails & Component<T>> RenderOwnedEntityDetails copyRenderOwnedEntityDetails(Class<T> clazz, T other) {
        try {
            T obj = clazz.getConstructor().newInstance();
            obj.copy(other);
            return obj;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create empty " + clazz.getName() + " for copying", e);
        }
    }
}

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

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.math.Rotation;
import org.terasology.itemRendering.components.CustomRenderedItemMeshComponent;
import org.terasology.itemRendering.components.RenderItemComponent;
import org.terasology.engine.logic.location.Location;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.logic.MeshComponent;
import org.terasology.engine.utilities.random.FastRandom;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.BlockComponent;

/**
 * This will add a location and mesh to an entity in the world for any entities that get a RenderItemComponent, causing them to be rendered in the world.
 * <p/>
 * The location is a relative location based on the entity's owner.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class RenderItemClientSystem extends BaseComponentSystem {

    Random rand;

    @In
    WorldProvider worldProvider;

    @Override
    public void initialise() {
        rand = new FastRandom();
    }

    @ReceiveEvent
    public void onChangedItemDisplay(OnChangedComponent event, EntityRef entity, RenderItemComponent itemDisplay) {
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if (location != null) {
            updateLocation(entity, itemDisplay, location);
        }
    }

    private void updateLocation(EntityRef entity, RenderItemComponent itemDisplay, LocationComponent location) {
        Rotation rotation = Rotation.rotate(itemDisplay.yaw, itemDisplay.pitch, itemDisplay.roll);
        if (entity.hasComponent(LocationComponent.class)) {
            entity.saveComponent(location);
        } else {
            entity.addComponent(location);
        }
        Location.attachChild(entity.getOwner(), entity, itemDisplay.translate, rotation.orientation(), itemDisplay.size);
    }

    @ReceiveEvent
    public void onAddedItemDisplay(OnActivatedComponent event, EntityRef entity, RenderItemComponent itemDisplay) {
        LocationComponent locationComponent = entity.getOwner().getComponent(LocationComponent.class);

        if (locationComponent == null && entity.getOwner().hasComponent(BlockComponent.class)) {
            // sometimes blocks lose their location component
            BlockComponent blockComponent = entity.getOwner().getComponent(BlockComponent.class);
            locationComponent = new LocationComponent(new Vector3f(blockComponent.getPosition(new Vector3i())));
            entity.getOwner().addComponent(locationComponent);
        }

        if (locationComponent != null) {
            if (entity.hasComponent(CustomRenderedItemMeshComponent.class)) {
                addCustomItemRendering(entity);
            }

            updateLocation(entity, itemDisplay, new LocationComponent());
        }
    }

    private void addCustomItemRendering(EntityRef entity) {
        CustomRenderedItemMeshComponent customRenderedItemMeshComponent = entity.getComponent(CustomRenderedItemMeshComponent.class);
        MeshComponent meshComponent = new MeshComponent();
        meshComponent.mesh = customRenderedItemMeshComponent.mesh;
        meshComponent.material = customRenderedItemMeshComponent.material;
        entity.addOrSaveComponent(meshComponent);
    }

    @ReceiveEvent
    public void onRemoveItemDisplay(BeforeDeactivateComponent event, EntityRef entity, RenderItemComponent itemDisplay) {
        Location.removeChild(entity.getOwner(), entity);
        entity.removeComponent(LocationComponent.class);
    }
}

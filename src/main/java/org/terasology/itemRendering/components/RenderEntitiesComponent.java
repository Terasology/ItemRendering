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

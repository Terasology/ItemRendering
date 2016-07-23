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

import org.terasology.entitySystem.Component;
import org.terasology.math.Pitch;
import org.terasology.math.Roll;
import org.terasology.math.Yaw;
import org.terasology.math.geom.Vector3f;

/**
 * Add this to an entity that will be rendered in the world.  It will attach its location to its owner entity's location.
 */
public class RenderItemComponent implements Component {
    /**
     * Translation relative to its owner entity's location.
     */
    public Vector3f translate = new Vector3f(0, 0, 0);
    /**
     * Scaling factor for rendering the item.
     */
    public float size = 0.3f;
    public Yaw yaw = Yaw.NONE;
    public Pitch pitch = Pitch.NONE;
    public Roll roll = Roll.NONE;

    public RenderItemComponent() {
    }

    /**
     * Create a {@link RenderItemComponent} with specific relative translation.
     *
     * @param translate translation relative to its owner entity's location
     */
    public RenderItemComponent(Vector3f translate) {
        this.translate = translate;
    }

    /**
     * Create a {@link RenderItemComponent} with specific relative translation and scaling factor.
     *
     * @param translate translation to its owner entity's location
     * @param size      scaling factor
     */
    public RenderItemComponent(Vector3f translate, float size) {
        this.translate = translate;
        this.size = size;
    }
}

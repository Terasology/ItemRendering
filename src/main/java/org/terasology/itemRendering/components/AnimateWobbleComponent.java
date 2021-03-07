/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.itemRendering.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

/**
 * Add this to an entity and it will wobble up and down. Requires a LocationComponent to work.
 */
public class AnimateWobbleComponent implements Component {

    /**
     * The period length of one bounce in game time.
     */
    @Replicate
    public float period = 10f;

    /**
     * The max. height of a bounce in blocks.
     */
    @Replicate
    public float maxHeight = 10f;
}

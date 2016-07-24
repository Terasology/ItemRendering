/*
 * Copyright 2014 MovingBlocks
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

import org.terasology.entitySystem.Component;
import org.terasology.math.Side;
import org.terasology.network.Replicate;

/**
 * Add this to an entity that will move between the entrance side of a block to the exit side of a block, passing through the center of the block
 */
public class AnimatedMovingItemComponent implements Component {
    @Replicate
    public Side entranceSide;
    @Replicate
    public Side exitSide;

    /**
     * The point in time the item starts moving (in game time)
     */
    @Replicate
    public long startTime;

    /**
     * The point in time the item reaches the exit side (in game time)
     */
    @Replicate
    public long arrivalTime;

    public AnimatedMovingItemComponent() {
    }

    /**
     * @param entranceSide the block side the item enters
     * @param exitSide     the block side the item exits
     * @param startTime    the point in time the item starts moving (in game time)
     * @param arrivalTime  the point in time the item reaches the exit side (in game time)
     */
    public AnimatedMovingItemComponent(Side entranceSide, Side exitSide, long startTime, long arrivalTime) {
        this.entranceSide = entranceSide;
        this.exitSide = exitSide;
        this.startTime = startTime;
        this.arrivalTime = arrivalTime;
    }
}

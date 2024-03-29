// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Add this to an entity that will move between the entrance side of a block to the exit side of a block, passing through the center of the block
 */
public class AnimatedMovingItemComponent implements Component<AnimatedMovingItemComponent> {
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

    @Override
    public void copyFrom(AnimatedMovingItemComponent other) {
        this.entranceSide = other.entranceSide;
        this.exitSide = other.exitSide;
        this.startTime = other.startTime;
        this.arrivalTime = other.arrivalTime;
    }
}

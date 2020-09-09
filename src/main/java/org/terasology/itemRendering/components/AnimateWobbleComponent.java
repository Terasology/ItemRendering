// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

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

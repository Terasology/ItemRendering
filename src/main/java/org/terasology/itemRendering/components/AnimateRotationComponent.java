/*
 * Copyright 2014 MovingBlocks
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

import org.joml.Vector3f;
import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

/**
 * Add this to an entity that will continuously rotate.
 * <p>
 * Speed is in rotations per second.
 */
public class AnimateRotationComponent implements Component {
    @Replicate
    public float yawSpeed;
    @Replicate
    public float pitchSpeed;
    @Replicate
    public float rollSpeed;

    /**
     * Synchronize rotation components with each other, so that everything rotating at the same speed will rotate synchronously.
     */
    @Replicate
    public boolean isSynchronized;

    public AnimateRotationComponent() {
    }

    public AnimateRotationComponent(float yawSpeed, float pitchSpeed, float rollSpeed) {
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
        this.rollSpeed = rollSpeed;
    }

    public AnimateRotationComponent(float yawSpeed, float pitchSpeed, float rollSpeed, boolean isSynchronized) {
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
        this.rollSpeed = rollSpeed;
        this.isSynchronized = isSynchronized;
    }

    /**
     * Creates a new {@link AnimateRotationComponent} from a rotation vector.
     *
     * @param rotation the rotation speed values as (yaw, pitch, roll)
     */
    public AnimateRotationComponent(Vector3f rotation) {
        this.yawSpeed = rotation.x;
        this.pitchSpeed = rotation.y;
        this.rollSpeed = rotation.z;
    }

    /**
     * Creates a new {@link AnimateRotationComponent} from a rotation vector.
     *
     * @param rotation       the rotation speed values as (yaw, pitch, roll)
     * @param isSynchronized synchronize rotations of same speed
     */
    public AnimateRotationComponent(Vector3f rotation, boolean isSynchronized) {
        this.yawSpeed = rotation.x;
        this.pitchSpeed = rotation.y;
        this.rollSpeed = rotation.z;
        this.isSynchronized = isSynchronized;
    }

    /**
     * The rotation speeds as vector denoting (yaw, pitch, roll)
     *
     * @return vector with rotation speeds for (yaw, pitch, roll)
     */
    public Vector3f getRotation() {
        return new Vector3f(yawSpeed, pitchSpeed, rollSpeed);
    }

    /**
     * Set the rotation speeds for (yaw, pitch, roll)
     *
     * @param rotation the new rotation speeds given as (yaw, pitch, roll)
     */
    public void setRotation(Vector3f rotation) {
        this.yawSpeed = rotation.x;
        this.pitchSpeed = rotation.y;
        this.rollSpeed = rotation.z;
    }
}

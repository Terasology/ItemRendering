// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.components;

import org.joml.Vector3f;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Add this to an entity that will continuously rotate.
 * <p>
 * Speed is in rotations per second.
 */
public class AnimateRotationComponent implements Component<AnimateRotationComponent> {
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

    @Override
    public void copy(AnimateRotationComponent other) {
        this.yawSpeed = other.yawSpeed;
        this.pitchSpeed = other.pitchSpeed;
        this.rollSpeed = other.rollSpeed;
        this.isSynchronized = other.isSynchronized;
    }
}

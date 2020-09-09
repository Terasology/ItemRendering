// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology;

import org.terasology.engine.math.Direction;
import org.terasology.engine.math.Pitch;
import org.terasology.engine.math.Rotation;
import org.terasology.engine.math.Side;
import org.terasology.engine.math.Yaw;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;

public abstract class RotationUtils {

    public static Vector3f rotateVector3f(Vector3f input, Direction direction) {
        switch (direction) {
            case BACKWARD:
                return new Vector3f(input.x, input.y, input.z * -1);
            case DOWN:
                return new Vector3f(input.x, input.z * -1, input.y);
            case LEFT:
                return new Vector3f(input.z, input.y, input.x);
            case RIGHT:
                return new Vector3f(input.z * -1, input.y, input.x);
            case UP:
                return new Vector3f(input.x, input.z, input.y * -1);
            default:
                return new Vector3f(input.x, input.y, input.z);
        }
    }

    public static Vector3i rotateVector3i(Direction direction, Vector3i input) {
        switch (direction) {
            case BACKWARD:
                return new Vector3i(input.x, input.y, input.z * -1);
            case DOWN:
                return new Vector3i(input.x, input.z * -1, input.y);
            case LEFT:
                return new Vector3i(input.z, input.y, input.x);
            case RIGHT:
                return new Vector3i(input.z * -1, input.y, input.x);
            case UP:
                return new Vector3i(input.x, input.z, input.y * -1);
            default:
                return new Vector3i(input.x, input.y, input.z);
        }
    }

    public static Rotation getRotation(Side side) {
        Pitch pitch = Pitch.NONE;
        Yaw yaw = Yaw.NONE;

        if (side == Side.BACK) {
            pitch = Pitch.CLOCKWISE_180;
        } else if (side == Side.TOP) {
            pitch = Pitch.CLOCKWISE_90;
        } else if (side == Side.BOTTOM) {
            pitch = Pitch.CLOCKWISE_270;
        } else if (side == Side.LEFT) {
            yaw = Yaw.CLOCKWISE_90;
        } else if (side == Side.RIGHT) {
            yaw = Yaw.CLOCKWISE_270;
        }

        return Rotation.rotate(yaw, pitch);
    }
}

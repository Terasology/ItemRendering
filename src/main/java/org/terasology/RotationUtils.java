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
package org.terasology;

import org.terasology.math.Direction;
import org.terasology.math.Pitch;
import org.terasology.math.Rotation;
import org.terasology.math.Side;
import org.terasology.math.Yaw;
import org.terasology.math.geom.Vector3f;

public class RotationUtils {

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
                return new Vector3f(input.x, input.z, input.z * -1);
            default:
                return new Vector3f(input.x, input.y, input.z);
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
            pitch = pitch.CLOCKWISE_270;
        } else if (side == Side.LEFT) {
            yaw = Yaw.CLOCKWISE_90;
        } else if (side == Side.RIGHT) {
            yaw = Yaw.CLOCKWISE_270;
        }

        return Rotation.rotate(yaw, pitch);
    }
}

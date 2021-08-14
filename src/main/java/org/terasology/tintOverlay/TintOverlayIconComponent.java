// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.tintOverlay;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.utilities.Assets;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.Map;
import java.util.Set;

/**
 * A map of a texture and its tint parameters (although at this point it does more than just tint).
 */
public class TintOverlayIconComponent implements Component<TintOverlayIconComponent> {
    public Map<String, TintParameter> texture = Maps.newLinkedHashMap();

    public TintParameter getTintParameterForIcon(String iconUri) {
        for (Map.Entry<String, TintParameter> overlayItem : texture.entrySet()) {
            Set<ResourceUrn> toolItemIcon = Assets.resolveAssetUri(overlayItem.getKey(), Texture.class);
            Set<ResourceUrn> inputItemIcon = Assets.resolveAssetUri(iconUri, Texture.class);
            if (Iterables.getFirst(toolItemIcon, null).equals(Iterables.getFirst(inputItemIcon, null))) {
                return overlayItem.getValue();
            }
        }

        return null;
    }

    @Override
    public void copyFrom(TintOverlayIconComponent other) {
        this.texture.clear();
        for (Map.Entry<String, TintParameter> entry : other.texture.entrySet()) {
            TintParameter value = entry.getValue();
            TintParameter newValue = new TintParameter(
                    value.hue,
                    value.brightnessScale,
                    value.saturationScale,
                    value.shiftX,
                    value.shiftY
            );
            newValue.invisible = value.invisible;
            this.texture.put(entry.getKey(), newValue);
        }
    }

    @MappedContainer
    public static class TintParameter {
        private static final String DELIMITER = "~";

        /**
         * Change the hue to this value in between 0 and 360
         */
        public Integer hue;
        /**
         * Scale the brightness value so that texture is maintained after the hue replacement
         */
        public float brightnessScale = 1.0f;
        /**
         * Scale the saturation value so that texture is maintained after the hue replacement
         */
        public float saturationScale = 1.0f;
        /**
         * positive values shift right, negative values shift left
         */
        public int shiftX;
        /**
         * positive values shift down, negative values shift up
         */
        public int shiftY;

        public boolean invisible;

        public TintParameter(Integer hue, float brightnessScale, float saturationScale, int shiftX, int shiftY) {
            this.hue = hue;
            this.brightnessScale = brightnessScale;
            this.saturationScale = saturationScale;
            this.shiftX = shiftX;
            this.shiftY = shiftY;
        }

        public TintParameter() {
        }

        TintParameter(String delimitedString) {
            String[] split = delimitedString.split(DELIMITER);
            if (split.length == 5) {
                if (!split[0].isEmpty()) {
                    hue = Integer.parseInt(split[0]);
                }
                brightnessScale = Float.parseFloat(split[1]);
                saturationScale = Float.parseFloat(split[2]);
                shiftX = Integer.parseInt(split[3]);
                shiftY = Integer.parseInt(split[4]);
            }
        }

        String toDelimitedString() {
            return Joiner.on(DELIMITER).join(new String[]{
                    hue == null ? "" : String.valueOf(hue),
                    String.valueOf(brightnessScale),
                    String.valueOf(saturationScale),
                    String.valueOf(shiftX),
                    String.valueOf(shiftY)
            });
        }

        /**
         * @return A float hue in between 0-1 based on the 360 different hues
         */
        float getScaledHue() {

            return (float) hue / 360f;
        }
    }
}

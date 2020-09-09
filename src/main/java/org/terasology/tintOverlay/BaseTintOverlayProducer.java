// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.tintOverlay;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.gestalt.naming.Name;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Layers images on top of each other. Tinting, brightning, saturating, and shifting. Uses a goofy uri syntax to carry
 * all the parameters.
 */
public abstract class BaseTintOverlayProducer {
    private static final Name MODULENAME = new Name("ItemRendering");
    private static final Name TINTOVERLAY_RESOURCE_NAME = new Name("tintOverlayTexture");
    private static final Logger logger = LoggerFactory.getLogger(BaseTintOverlayProducer.class);

    /**
     * @return ItemRendering:tintOverlayTexture#tintparameters1>basetextureuri1,tintparameters2>basetextureuri2
     */
    public static String getTintOverlayUri(Map<String, TintOverlayIconComponent.TintParameter> hueTextures) {

        StringBuilder sb = new StringBuilder();
        sb.append(MODULENAME + ":" + TINTOVERLAY_RESOURCE_NAME + "#");

        List<String> parameters = Lists.newArrayList();
        for (Map.Entry<String, TintOverlayIconComponent.TintParameter> entry : hueTextures.entrySet()) {
            if (!entry.getValue().invisible) {
                parameters.add(entry.getValue().toDelimitedString() + ">" + entry.getKey());
            }
        }
        sb.append(escape(Joiner.on(",").join(parameters)));
        return sb.toString();

    }

    static String escape(String text) {
        return text.replace("#", "%23");
    }

    static String unEscape(String text) {
        return text.replace("%23", "#");
    }

    public Set<ResourceUrn> getAvailableAssetUrns() {
        return Collections.emptySet();
    }

    public Set<Name> getModulesProviding(Name resourceName) {
        if (TINTOVERLAY_RESOURCE_NAME.equals(resourceName)) {
            return ImmutableSet.of(MODULENAME);
        }
        return Collections.emptySet();
    }

    public ResourceUrn redirect(ResourceUrn urn) {
        return urn;
    }

    protected BufferedImage createImage(ResourceUrn urn) {
        if (MODULENAME.equals(urn.getModuleName()) && TINTOVERLAY_RESOURCE_NAME.equals(urn.getResourceName())) {
            Name fragmentName = urn.getFragmentName();
            if (!fragmentName.isEmpty()) {
                String[] parameterValues = unEscape(fragmentName.toString()).split(",");
                BufferedImage resultImage = null;
                for (String hueTexture : parameterValues) {

                    String[] hueTextureSplit = hueTexture.split(">", 2);
                    String resourceUri = hueTextureSplit[hueTextureSplit.length - 1];
                    TintOverlayIconComponent.TintParameter tintParameter =
                            new TintOverlayIconComponent.TintParameter(hueTextureSplit[0]);

                    BufferedImage resourceImage = getResourceImage(resourceUri);

                    if (resourceImage == null) {
                        logger.error("Image: " + resourceUri + " not found");
                        continue;
                    }

                    if (resultImage == null) {
                        resultImage = new BufferedImage(resourceImage.getHeight(), resourceImage.getWidth(),
                                BufferedImage.TYPE_INT_ARGB);
                    }

                    if (resultImage.getHeight() != resourceImage.getHeight()) {
                        logger.error("Heights are not the same for " + resourceUri + ". Skipping this image");
                        continue;
                    }

                    float[] hsv = new float[3];
                    for (int y = 0; y < resultImage.getHeight(); y++) {
                        for (int x = 0; x < resultImage.getWidth(); x++) {
                            int targetX = x + tintParameter.shiftX;
                            int targetY = y + tintParameter.shiftY;
                            if (validCoord(targetX, resultImage.getWidth())
                                    && validCoord(targetY, resultImage.getHeight())) {
                                int argb = resourceImage.getRGB(x, y);
                                int a = (argb >> 24) & 0xFF;
                                if (a > 0) {
                                    if (tintParameter.hue == null) {
                                        // we are not hueing
                                        resultImage.setRGB(targetX, targetY, argb);
                                    } else {
                                        Color.RGBtoHSB((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, hsv);

                                        int resultRgb = Color.HSBtoRGB(tintParameter.getScaledHue(),
                                                hsv[1] * tintParameter.saturationScale,
                                                hsv[2] * tintParameter.brightnessScale);
                                        int resultArgb = (a << 24) | (resultRgb & 0x00FFFFFF);
                                        resultImage.setRGB(targetX, targetY, resultArgb);
                                    }
                                }
                            }
                        }
                    }
                }

                return resultImage;
            }
        }
        return null;
    }

    protected abstract BufferedImage getResourceImage(String resourceUri);

    private boolean validCoord(int coord, int maxCoord) {
        return coord >= 0 && coord <= maxCoord;
    }
}

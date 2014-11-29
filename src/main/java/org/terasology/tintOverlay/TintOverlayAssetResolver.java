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
package org.terasology.tintOverlay;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.asset.AssetFactory;
import org.terasology.asset.AssetResolver;
import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.naming.Name;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureData;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.assets.texture.TextureUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Layers images on top of each other. Tinting, brightning, saturating, and shifting.
 * Uses a goofy uri syntax to carry all the parameters.
 */
public class TintOverlayAssetResolver implements AssetResolver<Texture, TextureData> {
    private static final Name MODULENAME = new Name("texturefunction");
    private static final Logger logger = LoggerFactory.getLogger(TintOverlayAssetResolver.class);

    /**
     * @return texturefunction.tintoverlay=tintparameters1>basetextureuri1,tintparameters2>basetextureuri2
     */
    public static String getTintOverlayUri(Map<String, TintOverlayIconComponent.TintParameter> hueTextures) {

        StringBuilder sb = new StringBuilder();
        sb.append(MODULENAME + ":tintoverlay=");

        List<String> parameters = Lists.newArrayList();
        for (Map.Entry<String, TintOverlayIconComponent.TintParameter> entry : hueTextures.entrySet()) {
            if (!entry.getValue().invisible) {
                parameters.add(entry.getValue().toDelimitedString() + ">" + entry.getKey());
            }
        }
        sb.append(Joiner.on(",").join(parameters));
        return sb.toString();

    }

    @Override
    public AssetUri resolve(Name partialUri) {
        String[] parts = partialUri.toString().split("\\=", 2);
        if (parts.length > 1) {
            AssetUri uri = Assets.resolveAssetUri(AssetType.TEXTURE, parts[0]);
            if (uri != null) {
                return new AssetUri(AssetType.TEXTURE, uri.getModuleName(), partialUri);
            }
        }
        return null;
    }

    @Override
    public Texture resolve(AssetUri uri, AssetFactory<TextureData, Texture> factory) {
        final String assetName = uri.getAssetName().toString().toLowerCase();
        if (!MODULENAME.equals(uri.getModuleName())
                || !assetName.startsWith("tintoverlay=")) {
            return null;
        }
        String[] split = assetName.split("=", 2);

        String[] parameterValues = split[1].split(",");

        BufferedImage resultImage = null;
        for (String hueTexture : parameterValues) {

            String[] hueTextureSplit = hueTexture.split(">", 2);
            String textureResourceUri = hueTextureSplit[1];
            TintOverlayIconComponent.TintParameter tintParameter = new TintOverlayIconComponent.TintParameter(hueTextureSplit[0]);

            TextureRegion resourceTextureRegion = Assets.getTextureRegion(textureResourceUri);
            if (resourceTextureRegion == null) {
                logger.error("Texture: " + textureResourceUri + " not found");
                continue;
            }
            BufferedImage resourceImage = TextureUtil.convertToImage(resourceTextureRegion);
            if (resultImage == null) {
                resultImage = new BufferedImage(resourceImage.getHeight(), resourceImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            }

            if (resultImage.getHeight() != resourceImage.getHeight()) {
                logger.error("Heights are not the same for " + textureResourceUri + ". Skipping this texture");
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

                                int resultRgb = Color.HSBtoRGB(tintParameter.getScaledHue(), hsv[1] * tintParameter.saturationScale, hsv[2] * tintParameter.brightnessScale);
                                int resultArgb = (a << 24) | (resultRgb & 0x00FFFFFF);
                                resultImage.setRGB(targetX, targetY, resultArgb);
                            }
                        }
                    }
                }
            }
        }

        if (resultImage == null) {
            return null;
        } else {
            final ByteBuffer byteBuffer = TextureUtil.convertToByteBuffer(resultImage);
            return factory.buildAsset(uri, new TextureData(resultImage.getWidth(), resultImage.getHeight(), new ByteBuffer[]{byteBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST));
        }
    }

    private boolean validCoord(int coord, int maxCoord) {
        return coord >= 0 && coord <= maxCoord;
    }
}

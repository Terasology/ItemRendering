// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.tintOverlay;

import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureData;
import org.terasology.engine.rendering.assets.texture.TextureRegionAsset;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.utilities.Assets;
import org.terasology.gestalt.assets.AssetDataProducer;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.gestalt.assets.module.annotations.RegisterAssetDataProducer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * Layers images on top of each other. Tinting, brightning, saturating, and shifting. Uses a goofy uri syntax to carry
 * all the parameters.
 */
@RegisterAssetDataProducer
public class TintOverlayTextureProducer extends BaseTintOverlayProducer implements AssetDataProducer<TextureData> {

    @Override
    public Optional<TextureData> getAssetData(ResourceUrn urn) throws IOException {
        BufferedImage resultImage = createImage(urn);
        if (resultImage != null) {
            final ByteBuffer byteBuffer = TextureUtil.convertToByteBuffer(resultImage);
            return Optional.of(new TextureData(
                    resultImage.getWidth(),
                    resultImage.getHeight(),
                    new ByteBuffer[]{byteBuffer},
                    Texture.WrapMode.REPEAT,
                    Texture.FilterMode.NEAREST));
        }

        return Optional.empty();
    }

    @Override
    protected BufferedImage getResourceImage(String resourceUri) {
        Optional<TextureRegionAsset> resourceTextureRegion = Assets.getTextureRegion(resourceUri);
        if (!resourceTextureRegion.isPresent()) {
            return null;
        }
        return TextureUtil.convertToImage(resourceTextureRegion.get());
    }
}

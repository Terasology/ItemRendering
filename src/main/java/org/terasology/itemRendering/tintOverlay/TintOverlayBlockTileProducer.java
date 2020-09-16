// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.itemRendering.tintOverlay;

import org.terasology.engine.rendering.assets.texture.TextureRegionAsset;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.tiles.BlockTile;
import org.terasology.engine.world.block.tiles.TileData;
import org.terasology.gestalt.assets.AssetDataProducer;
import org.terasology.gestalt.assets.ResourceUrn;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

/**
 * Layers images on top of each other. Tinting, brightning, saturating, and shifting. Uses a goofy uri syntax to carry
 * all the parameters.
 * <p>
 * Warning: this does not yet work because: TileData does not yet have @API, runtime generated TileData assets do not
 * get added to the blocktile texture.
 */
//@RegisterAssetDataProducer
public class TintOverlayBlockTileProducer extends BaseTintOverlayProducer implements AssetDataProducer<TileData> {

    @Override
    public Optional<TileData> getAssetData(ResourceUrn urn) throws IOException {
        BufferedImage[] resultImage = new BufferedImage[1];
        resultImage[0] = createImage(urn);
        if (resultImage[0] != null) {
            return Optional.of(new TileData(resultImage, false));
        }

        return Optional.empty();
    }

    @Override
    protected BufferedImage getResourceImage(String resourceUri) {
        // attempt to get an existing block tile
        Optional<BlockTile> resourceBlockTile = Assets.get(resourceUri, BlockTile.class);
        if (resourceBlockTile.isPresent()) {
            return resourceBlockTile.get().getImage(0);
        }

        // try and get it as a normal texture
        Optional<TextureRegionAsset> resourceTextureRegion = Assets.getTextureRegion(resourceUri);
        if (resourceTextureRegion.isPresent()) {
            return TextureUtil.convertToImage(resourceTextureRegion.get());
        }

        return null;
    }
}

package com.nations.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class AssetsManager {
    private TextureAtlas mMainTileset;
    private float mTileWidth;
    private float mTileHeight;
    private final HashMap<String, Sprite> mSprites = new HashMap<String, Sprite>();

    private static AssetsManager sAssetsManager;
    public static AssetsManager get() {
        if(sAssetsManager == null) {
            sAssetsManager = new AssetsManager();
        }
        return sAssetsManager;
    }
    private AssetsManager() {
        mMainTileset = new TextureAtlas("tileset.txt");

        Array<TextureAtlas.AtlasRegion> regions = mMainTileset.getRegions();
        for(TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = mMainTileset.createSprite(region.name);
            mSprites.put(region.name, sprite);
        }
        mTileWidth = mSprites.get("grass").getWidth()-3;
        mTileHeight = 28f;
    };
    public void dispose() {
        mMainTileset.dispose();
    }
    public Sprite getSprite(Terrain terrain) {
        return mSprites.get(terrain.getType().toString().toLowerCase());
    }
    public Sprite getRiver(String type) {
        String sType = "011011";
        int sBits = 4;
        for(int i = 0; i < type.length(); ++i) {
            if(type.charAt(i) != sType.charAt(i) && sBits > 2) {
                sType = sType.substring(0, i) + "0" + sType.substring(i + 1, sType.length());
                sBits--;
            }
            else continue;
        }
        if(mSprites.get("river"+sType) == null)
            return mSprites.get("sea");
        return mSprites.get("river" + sType);
    }

    public float getTileWidth() {
        return mTileWidth;
    }

    public float getTileHeight() {
        return mTileHeight;
    }
}

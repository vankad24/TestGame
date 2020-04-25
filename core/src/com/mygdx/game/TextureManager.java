package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureManager {
    AssetManager manager;


    public TextureManager() {
        manager = new AssetManager();
        manager.load("myAtlas.atlas",TextureAtlas.class);
        manager.finishLoading();
    }

    public AssetManager getManager() { return manager; }

    public void dispose () {
        manager.dispose();
    }
}

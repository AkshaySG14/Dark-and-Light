package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class RedLighterHead extends Head {

    public RedLighterHead(GameScreen screen, RedLighter owner, TextureAtlas atlas) {
        super(screen, owner, atlas);

        setRegion(headAtlas.findRegion("RedLighterHead1"));
        setSize(getRegionWidth(), getRegionHeight());
    }

    protected void update() {

    }

    public void changeColor(boolean colored) {
        if (colored)
            setRegion(headAtlas.findRegion("RedLighterHead2"));
        else
            setRegion(headAtlas.findRegion("RedLighterHead1"));
    }
}

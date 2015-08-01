package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class OrangeLighterHead extends Head {

    public OrangeLighterHead(GameScreen screen, OrangeLighter owner, TextureAtlas atlas) {
        super(screen, owner, atlas);

        setRegion(headAtlas.findRegion("OrangeLighterHead1"));
        setSize(getRegionWidth(), getRegionHeight());
    }

    protected void update() {
    }

    public void changeColor(boolean colored) {
        if (colored)
            setRegion(headAtlas.findRegion("OrangeLighterHead2"));
        else
            setRegion(headAtlas.findRegion("OrangeLighterHead1"));
    }
}

package com.inoculates.dal.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class YellowLighterHead extends Head {

    public YellowLighterHead(GameScreen screen, YellowLighter owner, TextureAtlas atlas) {
        super(screen, owner, atlas);

        setRegion(headAtlas.findRegion("YellowLighterHead1"));
        setSize(getRegionWidth(), getRegionHeight());
    }

    protected void update() {

    }

    public void changeColor(boolean colored) {
        if (colored)
            setRegion(headAtlas.findRegion("YellowLighterHead2"));
        else
            setRegion(headAtlas.findRegion("YellowLighterHead1"));
    }
}

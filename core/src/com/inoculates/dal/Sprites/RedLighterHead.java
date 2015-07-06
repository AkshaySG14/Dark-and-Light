package com.inoculates.dal.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class RedLighterHead extends Sprite {
    private GameScreen screen;
    private TextureAtlas enemyAtlas;
    private RedLighter owner;

    public RedLighterHead(GameScreen screen, RedLighter owner, TextureAtlas atlas) {
        this.screen = screen;
        this.enemyAtlas = atlas;
        this.owner = owner;

        setRegion(enemyAtlas.findRegion("RedLighterHead1"));
        setSize(8, 7);
    }

    public void draw(SpriteBatch batch) {
        setPosition(owner.getX() + owner.getWidth() / 2 - getWidth() / 2,
                owner.getY() + owner.getHeight() / 2 - getHeight() / 2);

        super.draw(batch);
    }

    public void changeColor(boolean colored) {
        if (colored)
            setRegion(enemyAtlas.findRegion("RedLighterHead2"));
        else
            setRegion(enemyAtlas.findRegion("RedLighterHead1"));
    }

    public void setRotation(float angle) {
        setOriginCenter();
        super.setRotation(angle);
    }
}

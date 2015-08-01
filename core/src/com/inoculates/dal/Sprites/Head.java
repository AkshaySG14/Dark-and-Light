package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public abstract class Head extends Sprite {
    protected GameScreen screen;
    protected TextureAtlas headAtlas;
    protected Sprite owner;

    // Initializes the head.
    public Head(GameScreen screen, Sprite owner, TextureAtlas atlas) {
        this.screen = screen;
        this.headAtlas = atlas;
        this.owner = owner;
    }

    public void draw(SpriteBatch batch) {
        // Continuously sets the position of the head to the center of the owner body.
        setPosition(owner.getX() + owner.getWidth() / 2 - getWidth() / 2,
                owner.getY() + owner.getHeight() / 2 - getHeight() / 2);
        update();

        super.draw(batch);
    }

    // Sets the axis of rotation to be the center of the head.
    public void setRotation(float angle) {
        setOriginCenter();
        super.setRotation(angle);
    }

    abstract void update();

    // Changes the region of the head when the light is on.
    public abstract void changeColor(boolean colored);
}

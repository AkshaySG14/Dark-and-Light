package com.inoculates.dal.UI;

// Created by akshaysubramaniam on 4/7/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// This class simply serves as a way to iterate through all enemies, and as an identifier.
public abstract class UI extends Sprite {
    protected GameScreen screen;
    protected World world;
    protected TextureAtlas UIAtlas;
    protected float xPos, yPos;

    public UI(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        this.screen = screen;
        this.world = world;
        this.UIAtlas = atlas;
        xPos = x;
        yPos = y;
    }

    public void draw(SpriteBatch batch) {
        // Continually sets the position of the UI element to the offset in the camera. This ensures that the UI
        // element is always in view of the player.
        update();
        super.draw(batch);
    }

    abstract void update();

    abstract void updateTime(float deltaTime);
}

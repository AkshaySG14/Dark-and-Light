package com.inoculates.dal.UI;

// Created by akshaysubramaniam on 4/7/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// This class simply serves as a way to iterate through all enemies, and as an identifier.
public class HealthBar extends UI {
    public HealthBar(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        super(screen, world, atlas, x, y);
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    protected void update() {

    }

    protected void updateTime(float deltaTime) {

    }
}

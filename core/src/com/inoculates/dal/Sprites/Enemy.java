package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 4/7/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// This class simply serves as a way to iterate through all enemies, and as an identifier.
public abstract class Enemy extends Sprite {
    protected GameScreen screen;
    protected World world;
    protected TextureAtlas enemyAtlas;

    public Enemy(GameScreen screen, World world, TextureAtlas atlas) {
        this.screen = screen;
        this.world = world;
        this.enemyAtlas = atlas;
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    abstract void update();

    abstract void updateTime(float deltaTime);
}

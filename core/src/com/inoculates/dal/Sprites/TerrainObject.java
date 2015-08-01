package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 12/7/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

public abstract class TerrainObject extends Sprite {
    protected GameScreen screen;
    protected World world;
    protected TextureAtlas terrainAtlas;
    protected TerrainObject link;
    protected Body tBody;

    // Initializes the terrain object without a link.
    public TerrainObject(GameScreen screen, World world, TextureAtlas atlas) {
        this.screen = screen;
        this.world = world;
        this.terrainAtlas = atlas;
    }

    // Initializes the terrain object with link.
    public TerrainObject(GameScreen screen, World world, TextureAtlas atlas, TerrainObject link) {
        this.screen = screen;
        this.world = world;
        this.terrainAtlas = atlas;
        this.link = link;
    }

    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
    }

    // Optional, overridable method.
    protected void update() {

    }

    public abstract void activate();
}

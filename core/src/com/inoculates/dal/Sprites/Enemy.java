package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 4/7/15.

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// This class simply serves as a way to iterate through all enemies, and as an identifier.
public abstract class Enemy extends Character {
    protected TextureAtlas enemyAtlas;
    protected Body eBody;
    // The angle that is used to swivel the head in a 90 degree arc.
    protected float rotateAngle = 0;
    // Direction the head is swivelling.
    private int rotateDir = 1;

    public Enemy(GameScreen screen, World world, TextureAtlas atlas) {
        super(screen, world);
        this.screen = screen;
        this.world = world;
        this.enemyAtlas = atlas;
        layer = (TiledMapTileLayer) screen.getMap().getLayers().get("Objects");
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public float getBX() {
        return eBody.getPosition().x;
    }

    public float getBY() {
        return eBody.getPosition().y;
    }

    protected boolean isMoving() {
        return eBody.getLinearVelocity().x != 0 || eBody.getLinearVelocity().y != 0;
    }

    // Constantly rotates the head of the enemy.
    protected void rotateHead() {
        // If the rotation of the head is 45 degrees away from the rotate angle, makes the head rotate the other way.
        // This makes the head swivel constantly in a 90 degree arc.
        if (Math.abs(rotateAngle - head.getRotation()) > 45)
            rotateDir *= -1;
        // Sets rotation of the head in accordance with the rotation direction.
        head.setRotation(head.getRotation() + rotateDir);
    }

    abstract void update();

    abstract void updateTime(float deltaTime);
}

package com.inoculates.dal.UI;

// Created by akshaysubramaniam on 4/7/15.

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// This is the health bar UI element, that serves as the OUTLINE for the health of the player.
public class Health extends UI {

    public Health(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        super(screen, world, atlas, x, y);
        setRegion(UIAtlas.findRegion("Health"));
        setSize(getRegionWidth(), getRegionHeight());
    }

    protected void update() {
        setPosition(screen.camera.position.x - screen.camera.viewportWidth / 2 + xPos, screen.camera.position.y -
                screen.camera.viewportHeight / 2 + yPos);
    }

    protected void updateTime(float deltaTime) {

    }
}

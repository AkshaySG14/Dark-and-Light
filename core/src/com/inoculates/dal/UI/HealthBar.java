package com.inoculates.dal.UI;

// Created by akshaysubramaniam on 4/7/15.

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// This is the health UI element, that serves as the red bar that indicates to the player what his/her health is.
public class HealthBar extends UI {
    private Health healthUI;

    public HealthBar(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        super(screen, world, atlas, x, y);
        healthUI = new Health(screen, world, atlas, x + 1, y + 1);
        screen.UIs.add(healthUI);
        setRegion(UIAtlas.findRegion("HealthBar"));
        setSize(getRegionWidth(), getRegionHeight());
    }

    public void updateHealth(int health) {
        healthUI.setSize(health, healthUI.getHeight());
    }

    protected void update() {
        setPosition(screen.camera.position.x - screen.camera.viewportWidth / 2 + xPos, screen.camera.position.y -
        screen.camera.viewportHeight / 2 + yPos);
    }

    protected void updateTime(float deltaTime) {

    }
}

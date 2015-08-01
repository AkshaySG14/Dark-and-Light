package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.inoculates.dal.WorldHandlers.GameScreen;

// Created by akshaysubramaniam on 4/7/15.
public class RedLighterStatic extends RedLighter {
    private int direction, rotateDir = 1;
    private float turnTime = 0;
    private float lightTime = 0;

    public RedLighterStatic(GameScreen screen, World world, float x, float y, TextureAtlas atlas, int direction, float
                            startingAngle) {
        super(screen, world, atlas, x, y);
        this.direction = direction;
        startDirection(startingAngle);
    }

    // Sets the starting direction.
    private void startDirection(float angle) {
        eBody.setTransform(eBody.getPosition(), MathUtils.degreesToRadians * angle);
        setRotation(angle);
        head.setRotation(angle);
    }

    protected void tryMove() {
        // Every one second, the RedLighter will switch its light on/off. This is accomplished by periodically setting
        // light time to zero. This only occurs if the RedLighter is facing in one direction.
        if (lightTime > 1 && direction == 0) {
            switchLight();
            lightTime = 0;
        }

        // Changes the direction in accordance with the time that has passed, which is captured via turnTime.
        changeDirection();
    }

    // Changes the direction of the head depending on the direction the box is initially facing, and its type.
    private void changeDirection() {
        switch (direction) {
            case 0:
                // Exits the loop to prevent the head from rotating.
                return;
            case 1:
                // Spins head in a 90 degree arc
                // If the head is too far to the right, spins the head the other way.
                if (head.getRotation() < getRotation() - 90)
                    rotateDir = 1;
                else if (head.getRotation() > getRotation())
                    rotateDir = -1;
            case 2:
                // Spins head in a 180 degree arc.
                // If the head is too far to the left respective of the box's angle, starts turning head in a clockwise
                // direction.
                if (head.getRotation() < getRotation() - 90)
                    rotateDir = 1;
                // Same but for the right and in a counter-clockwise direction.
                else if (head.getRotation() > getRotation() + 90)
                    rotateDir = -1;
                break;
            case 3:
                // Spins head in a 270 degree arc.
                if (head.getRotation() < getRotation() - 90)
                    rotateDir = 1;
                else if (head.getRotation() > getRotation() + 180)
                    rotateDir = -1;
            case 4:
                // Spins in a 360 degree arc, therefore requires no change of direction.
                break;
        }
        // Sets rotation of the head in accordance with the rotation direction.
        head.setRotation(head.getRotation() + rotateDir);
    }

    protected void updateTime(float deltaTime) {
        lightTime += deltaTime;
        turnTime += deltaTime;
    }
}

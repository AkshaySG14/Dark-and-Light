package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.inoculates.dal.WorldHandlers.GameScreen;

// Created by akshaysubramaniam on 4/7/15.
public class RedLighterDynamic extends RedLighter {
    private float moveTime = 0;

    public RedLighterDynamic(GameScreen screen, World world, float x, float y, TextureAtlas atlas, float
            startingAngle) {
        super(screen, world, atlas, x, y);
        startDirection(startingAngle);
    }

    // Sets the starting direction.
    private void startDirection(float angle) {
        eBody.setTransform(eBody.getPosition(), MathUtils.degreesToRadians * angle);
        setRotation(angle - 90);
        head.setRotation(angle - 90);
        rotateAngle = angle;
        // Sets initial movement based on the direction the Red Lighter is facing.
        eBody.setLinearVelocity((float) (Math.cos(angle * MathUtils.degreesToRadians) * 25),
                (float) (Math.sin(angle * MathUtils.degreesToRadians) * 25));
    }

    // Changes the velocity of the Red Lighter to the opposite direction every 2.5 seconds.
    protected void tryMove() {
        if (moveTime > 2.5f) {
            changeDirection();
            moveTime = 0;
        }
        rotateHead();
    }

    // Changes the direction of the entire Red Lighter to the opposite one.
    private void changeDirection() {
        // Gets the negative of the velocity so that it may be used later. Afterwards, set velocity to zero so that
        // the Red Lighter turns in place.
        final float velX = eBody.getLinearVelocity().x * -1;
        final float velY = eBody.getLinearVelocity().y * -1;
        rotate(velX, velY);
        eBody.setLinearVelocity(0, 0);
    }

    // Swivels the RedLighter 180 degrees.
    private void rotate(final float velX, final float velY) {
        float deltaTime = 0;
        // Captures the initial rotation.
        final float initialRotation = getRotation();
        // Switches light off.
        switchLight();
        // Quickly rotates the sprite by offsetting a minor rotation by time.
        for (float angle = getRotation(); angle <= getRotation() + 180; angle ++) {
            final float newAngle = angle;
            Timer timer = new Timer();
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    setRotation(newAngle);
                    head.setRotation(newAngle);
                    // If done turning, allows for movement again and turns light on.
                    if (newAngle == initialRotation + 180) {
                        eBody.setLinearVelocity(velX, velY);
                        switchLight();
                        rotateAngle = newAngle;
                    }
                }
            }, deltaTime);
            timer.start();
            deltaTime += 0.005f;
        }
    }

    protected void updateTime(float deltaTime) {
        // If the Red Lighter is moving, moveTime is incremented.
        if (eBody.getLinearVelocity().x != 0 || eBody.getLinearVelocity().y != 0)
            moveTime += deltaTime;
    }
}

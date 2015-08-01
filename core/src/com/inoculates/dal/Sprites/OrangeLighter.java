package com.inoculates.dal.Sprites;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.inoculates.dal.WorldHandlers.GameScreen;

// Created by akshaysubramaniam on 4/7/15.
public class OrangeLighter extends Enemy {
    private float moveTime = 0;

    public OrangeLighter(GameScreen screen, World world, TextureAtlas atlas, float x, float y, float startingAngle) {
        super(screen, world, atlas);

        BodyDef eDef = new BodyDef();
        eDef.type = BodyDef.BodyType.KinematicBody;
        eBody = world.createBody(eDef);

        // Creates the circular shape and its radius.
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(0, 0);
        vertices[1] = new Vector2(8, 16);
        vertices[2] = new Vector2(16, 0);
        shape.set(vertices);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1;
        fDef.friction = 0.5f;
        fDef.restitution = 0.2f;
        Fixture fixture = eBody.createFixture(fDef);
        shape.dispose();

        setRegion(enemyAtlas.findRegion("OrangeLighter2"));
        setSize(getRegionWidth(), getRegionHeight());
        eBody.setTransform(x, y, 0);

        head = new OrangeLighterHead(screen, this, atlas);

        // Creates the flashlight and sets it to the main rayhandler.
        fLight = new ConeLight(screen.lHandler.rHandler, 300, Color.ORANGE, 75, getX(), getY(), head.getRotation(), 20);
        fLight.setSoftnessLength(100);
        fLight.setActive(false);

        startDirection(startingAngle);
        switchLight();
    }

    // Sets the starting direction.
    private void startDirection(float angle) {
        eBody.setTransform(eBody.getPosition(), MathUtils.degreesToRadians * angle);
        // Sets initial movement based on the direction the Orange Lighter is facing.
        eBody.setLinearVelocity((float) Math.cos(angle) * 25, (float) Math.sin(angle) * 25);
        eBody.setAngularVelocity(-5);
    }

    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
        head.draw(batch);
    }

    // Changes the velocity of the Orange Lighter to the opposite direction every 3 seconds.
    protected void update() {
        updateTime(Gdx.graphics.getDeltaTime());
        // Sets sprite around the physical body position.
        setPosition(eBody.getPosition().x - getWidth() / 2, eBody.getPosition().y - getHeight() / 2);
        // Sets origin of the sprite to center so that it can rotate around the center.
        setOriginCenter();
        // Sets rotation of sprite itself and head to the body's.
        setRotation(MathUtils.radiansToDegrees * eBody.getAngle());
        head.setRotation(getRotation());

        // If the player is in lighting mode, set position of cone light.
        if (lighting) {
            fLight.setDirection(head.getRotation() + 90);
            fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);
        }
        //Moves in opposite direction every three seconds.
        if (moveTime > 3) {
            changeDirection();
            moveTime = 0;
        }

        // If player is inside flashlight, makes him/her lose health.
        if (checkLightCollision(screen.player.getBX(), screen.player.getBY()))
            screen.player.loseHealth();
    }

    // Changes the direction of the entire Orange Lighter to the opposite one.
    private void changeDirection() {
        // Gets the negative of the velocity so that it may be used later. After one second, the orange lighter moves
        // once again.
        final float velX = eBody.getLinearVelocity().x * -1;
        final float velY = eBody.getLinearVelocity().y * -1;
        // Sets frame to dormant region.
        setRegion(enemyAtlas.findRegion("OrangeLighter1"));
        // Immobilizes the sprite and turns the light off.
        eBody.setLinearVelocity(0, 0);
        switchLight();

        // After one second, makes the body head the other way.
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                // Sets opposite angular and linear velocity.
                eBody.setLinearVelocity(velX, velY);
                eBody.setAngularVelocity(eBody.getAngularVelocity() * -1);
                // Sets region to active region once again.
                setRegion(enemyAtlas.findRegion("OrangeLighter2"));
                // Switches off light.
                switchLight();
            }
        }, 1);
        timer.start();

    }

    protected void updateTime(float deltaTime) {
        // If the Orange Lighter is moving, moveTime is incremented.
        if (eBody.getLinearVelocity().x != 0 || eBody.getLinearVelocity().y != 0)
            moveTime += deltaTime;
    }
}

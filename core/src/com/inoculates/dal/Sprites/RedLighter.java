package com.inoculates.dal.Sprites;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

abstract class RedLighter extends Enemy {
    // Initializes the body of the enemy. This is largely the same as the player.
    public RedLighter(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        super(screen, world, atlas);
        BodyDef eDef = new BodyDef();
        eDef.type = BodyDef.BodyType.KinematicBody;
        eBody = world.createBody(eDef);

        // Creates the box shape and its dimensions.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8, 8);

        // Defines the shape.
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;

        fDef.density = 1;
        fDef.friction = 0.5f;

        Fixture fixture = eBody.createFixture(fDef);
        shape.dispose();

        // Sets the sprite based on whether the Red Lighter is static or not.
        if (this instanceof RedLighterStatic)
            setRegion(enemyAtlas.findRegion("RedLighter1"));
        else
            setRegion(enemyAtlas.findRegion("RedLighter2"));

        setSize(getRegionWidth(), getRegionHeight());
        eBody.setTransform(x, y, 0);

        head = new RedLighterHead(screen, this, atlas);

        // Creates the flashlight and sets it to the main rayhandler. Also turns it on.
        fLight = new ConeLight(screen.lHandler.rHandler, 300, Color.RED, 100, getX(), getY(), head.getRotation(), 20);
        fLight.setSoftnessLength(100);
        switchLight();
    }

    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
        head.draw(batch);
    }

    protected void update() {
        updateMovement();
        setPosition(eBody.getPosition().x - getWidth() / 2, eBody.getPosition().y - getHeight() / 2);
        setOriginCenter();

        if (lighting) {
            fLight.setDirection(head.getRotation() + 90);
            fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);
        }

        // If player is inside flashlight, makes him/her lose health.
        if (checkLightCollision(screen.player.getBX(), screen.player.getBY()))
            screen.player.loseHealth();
    }

    private void updateMovement() {
        tryMove();
        updateTime(Gdx.graphics.getDeltaTime());
    }

    // This is the move method that is determined by the subclasses of RedLighter.
    abstract void tryMove();
}

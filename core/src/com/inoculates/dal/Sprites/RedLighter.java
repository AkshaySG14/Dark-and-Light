package com.inoculates.dal.Sprites;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

abstract class RedLighter extends Enemy {
    protected Body eBody;
    protected boolean stun = false, lighting = false;
    protected RedLighterHead head;
    private ConeLight fLight;

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

        setRegion(enemyAtlas.findRegion("RedLighter1"));
        setSize(16, 16);
        eBody.setTransform(x, y, 0);

        head = new RedLighterHead(screen, this, atlas);

        // Creates the flashlight and sets it to the main rayhandler.
        fLight = new ConeLight(screen.lHandler.rHandler, 300, Color.RED, 75, getX(), getY(), head.getRotation(), 20);
        fLight.setActive(false);
        fLight.setSoftnessLength(100);
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
        setRotation(MathUtils.radiansToDegrees * eBody.getAngle());

        if (lighting) {
            fLight.setDirection(head.getRotation() + 90);
            fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);
        }
    }

    private void updateMovement() {
        tryMove();
        changeDirection();
        updateTime(Gdx.graphics.getDeltaTime());

        if (eBody.getLinearVelocity().x != 0 || eBody.getLinearVelocity().y != 0)
            // Sets frame to active frame.
            setRegion(enemyAtlas.findRegion("RedLighter2"));
        else
            // Sets frame to inert frame and stops the player from rotating.
            setRegion(enemyAtlas.findRegion("RedLighter1"));
    }

    // Changes the direction of the box in accordance with its direction if it's moving.
    private void changeDirection() {
        if (eBody.getLinearVelocity().x != 0 && eBody.getLinearVelocity().y != 0)
            eBody.setTransform(eBody.getPosition(), eBody.getLinearVelocity().angle());
    }

    public void switchLight() {
        fLight.setDirection(head.getRotation() + 90);
        fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);

        lighting = !lighting;
        head.changeColor(lighting);
        fLight.setActive(lighting);
    }

    // This is the move method that is determined by the subclasses of RedLighter.
    abstract void tryMove();
}

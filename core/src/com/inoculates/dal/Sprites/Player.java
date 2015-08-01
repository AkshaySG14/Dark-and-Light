package com.inoculates.dal.Sprites;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.inoculates.dal.UI.HealthBar;
import com.inoculates.dal.WorldHandlers.GameScreen;

import java.awt.*;

//Created by akshaysubramaniam on 25/6/15.

public class Player extends Character {
    private Body pBody;
    private int health = 58, healthCounter = 0;
    private TextureAtlas playerAtlas;
    private HealthBar bar;
    private boolean frozen = false;

    // Initializes the body of the player.
    public Player(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        super(screen, world);
        this.playerAtlas = atlas;
        createUI();
        // Creates the definition for the body of the player, and sets it to dynamic.
        BodyDef pDef = new BodyDef();
        pDef.type = BodyDef.BodyType.DynamicBody;
        // Sets the body's definition to pDef to apply the preceding lines to the body.
        pBody = world.createBody(pDef);

        // Creates the circular shape and its radius.
        CircleShape shape = new CircleShape();
        // Sets the radius to half the sprite's width. Note that it is 0.1 off the actual radius that should be used.
        // This is to allow the player to squeeze in between blocks.
        shape.setRadius(7.9f);

        // Defines the shape.
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        // Sets density and friction for physics-related purposes.
        fDef.density = 1;
        fDef.friction = 0.5f;
        // Makes the ball bounce a little.
        fDef.restitution = 0.2f;
        // Sets the fixture definitions that were just created to the body.
        Fixture fixture = pBody.createFixture(fDef);
        // Gets rid of unnecessary data.
        shape.dispose();

        // Sets the frame of the sprite to the player frame.
        setRegion(playerAtlas.findRegion("Player1"));
        // Sets sizing of the sprite.
        setSize(getRegionWidth(), getRegionHeight());
        // Sets the position of the sprite to the given x and y position.
        pBody.setTransform(x, y, 0);
        pBody.setUserData(this);

        // Creates the player head.
        head = new PlayerHead(screen, this, atlas);

        // Creates the flashlight and sets it to the main rayhandler.
        fLight = new ConeLight(screen.lHandler.rHandler, 200, Color.GREEN, 100, getX(), getY(), head.getRotation(), 30);
        fLight.setActive(false);
        fLight.setSoftnessLength(100);
    }

    public void draw(SpriteBatch batch) {
        // If dead, returns to prevent any interaction with the player character.
        if (frozen)
            return;
        // Updates movement of the player.
        update();
        // Sets sprite around the physical body position.
        setPosition(pBody.getPosition().x - getWidth() / 2, pBody.getPosition().y - getHeight() / 2);
        // Sets origin of the sprite to center so that it can rotate around the center.
        setOriginCenter();
        setRotation(MathUtils.radiansToDegrees * pBody.getAngle());
        super.draw(batch);
        head.draw(batch);

        // If the player is in lighting mode, rotate cone light.
        if (lighting) {
            fLight.setDirection(head.getRotation() + 90);
            fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);
        }
        // Regenerates health, if able to.
        regenHealth();
    }

    private void update() {
        tryMove();
        checkBoxLight();
    }

    private void tryMove() {
        // Sets the player's linear velocity depending on the input of the player.
        if (Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S))
            pBody.setLinearVelocity(pBody.getLinearVelocity().x, 50);
        if (Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W))
            pBody.setLinearVelocity(pBody.getLinearVelocity().x, -50);
        if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A))
            pBody.setLinearVelocity(50, pBody.getLinearVelocity().y);
        else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D))
            pBody.setLinearVelocity(-50, pBody.getLinearVelocity().y);

        if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S))
            pBody.setLinearVelocity(pBody.getLinearVelocity().x, 0);
        if (!Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A))
            pBody.setLinearVelocity(0, pBody.getLinearVelocity().y);

        if (pBody.getLinearVelocity().x != 0 || pBody.getLinearVelocity().y != 0) {
            // Sets frame to active frame.
            setRegion(playerAtlas.findRegion("Player2"));
            // Rotates the player when moving. Axis of rotation is based on the direction the player is going.
            if (pBody.getLinearVelocity().x > 0 || (pBody.getLinearVelocity().y > 0 && pBody.getLinearVelocity().x == 0))
                pBody.setAngularVelocity(-5);
            else
                pBody.setAngularVelocity(5);
        }
        else {
            // Sets frame to inert frame and stops the player from rotating.
            setRegion(playerAtlas.findRegion("Player1"));
            pBody.setAngularVelocity(0);
        }

        // If the player is in the green end square, the player wins the game.
        if (isCellEnd(getBX(), getBY()))
            screen.winScreen();
    }

    // Regenerates health of the player if no damage was taken for five seconds.
    private void regenHealth() {
        // If under a light, does not allow regeneration.
        if (lit || health == 58)
            return;

        // Increases health.
        health++;
        bar.updateHealth(health);
    }

    // Reduces the health of the player and updates the UI accordingly.
    public void loseHealth() {
        // Health is zero, no need to calculate anything.
        if (health == 0)
            return;

        // This counter is here to ensure that the player's health does not go down too fast. It takes ten calls of
        // this method to effect any change in health.
        healthCounter ++;

        if (healthCounter > 2) {
            // Informs the program that the player should not be regenerating, removes HP, updates the UI, and resets
            // the player counter.
            lit = true;
            health --;
            bar.updateHealth(health);
            healthCounter = 0;

            // If after three seconds the player's health has not been reduced, allows the player to regenerate again.
            final int oldHealth = health;
            Timer timer = new Timer();
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    checkUnlit(oldHealth);
                }
            }, 5);
            timer.start();
        }

        if (health == 0)
            die();
    }

    // Fills in the box if it is within the player's flashlight.
    private void checkBoxLight() {
        for (TerrainObject tObject : screen.tObjects)
            if (tObject instanceof LightBox) {
                LightBox box = (LightBox) tObject;
                if (checkLightCollision(box.getX() + box.getWidth() / 2, box.getY() + box.getHeight() / 2))
                    box.colorIn();
            }
    }

    private void checkUnlit(final int oldHealth) {
        lit = health != oldHealth;
    }

    private void createUI() {
        // Creates the UI of the game.
        bar = new HealthBar(screen, world, screen.spriteAtlases.get(1), 5, 135);
        screen.UIs.add(bar);
    }

    // Brings up the lose screen.
    private void die() {
        screen.loseScreen();
        frozen = true;
    }

    //Gets the body position's x and y.
    public float getBX() {
        return pBody.getPosition().x;
    }

    public float getBY() {
        return pBody.getPosition().y;
    }
}

package com.inoculates.dal.Sprites;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class Player extends Sprite {
    private Body pBody;
    private boolean stun = false, lighting = false;
    private GameScreen screen;
    private World world;
    private TextureAtlas playerAtlas;
    private PlayerHead head;
    private ConeLight fLight;

    // Initializes the body of the player.
    public Player(GameScreen screen, World world, float x, float y, TextureAtlas atlas) {
        this.screen = screen;
        this.world = world;
        this.playerAtlas = atlas;
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
        setSize(16, 16);
        // Sets the position of the sprite to the given x and y position.
        pBody.setTransform(x, y, 0);

        // Creates the player head.
        head = new PlayerHead(screen, this, atlas);

        // Creates the flashlight and sets it to the main rayhandler.
        fLight = new ConeLight(screen.lHandler.rHandler, 200, Color.GREEN, 100, getX(), getY(), head.getRotation(), 30);
        fLight.setActive(false);
        fLight.setSoftnessLength(100);
    }

    public void draw(SpriteBatch batch) {
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
    }

    private void update() {
        tryMove();
    }

    private void tryMove() {
        if (stun)
            return;

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
    }

    // Switches on/off the light and the color of the player head correspondingly.
    public void switchLight() {
        // Resets the position of the flashlight so it draws at the correct position immediately.
        fLight.setDirection(head.getRotation() + 90);
        fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);

        // Switches on/off the flashlight.
        lighting = !lighting;
        // Changes the color of the head.
        head.changeColor(lighting);
        // Sets the light to active, so that it is rendered.
        fLight.setActive(lighting);
    }
}

package com.inoculates.dal.Sprites;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class YellowLighter extends Enemy {
    private float moveTime = 0, spawnX, spawnY;
    protected boolean following = false, returning = false;
    private Timer unfollowTimer = new Timer();

    // The Yellow Lighter is an enemy that can move around freely, and will pursue the player if it sees him/her. The
    // class itself is very similar to the player class.
    public YellowLighter(GameScreen screen, World world, TextureAtlas atlas, float x, float y) {
        super(screen, world, atlas);

        BodyDef eDef = new BodyDef();
        eDef.type = BodyDef.BodyType.DynamicBody;
        eBody = world.createBody(eDef);

        // Creates the circular shape and its radius.
        CircleShape shape = new CircleShape();
        shape.setRadius(7.9f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1;
        fDef.friction = 0.5f;
        fDef.restitution = 0.2f;
        Fixture fixture = eBody.createFixture(fDef);
        shape.dispose();

        setRegion(enemyAtlas.findRegion("YellowLighter1"));
        setSize(getRegionWidth(), getRegionHeight());
        eBody.setTransform(x, y, 0);
        spawnX = eBody.getPosition().x;
        spawnY = eBody.getPosition().y;
        eBody.setUserData(this);

        head = new YellowLighterHead(screen, this, atlas);

        // Creates the flashlight and sets it to the main rayhandler.
        fLight = new ConeLight(screen.lHandler.rHandler, 300, Color.YELLOW, 85, getX(), getY(), head.getRotation(), 20);
        fLight.setSoftnessLength(100);
        fLight.setActive(false);
    }

    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
        head.draw(batch);
    }

    protected void update() {
        updateTime(Gdx.graphics.getDeltaTime());

        // Sets sprite around the physical body position.
        setPosition(eBody.getPosition().x - getWidth() / 2, eBody.getPosition().y - getHeight() / 2);
        // Sets origin of the sprite to center so that it can rotate around the center.
        setOriginCenter();
        setRotation(MathUtils.radiansToDegrees * eBody.getAngle());

        // If the player is in lighting mode, set position of cone light.
        if (lighting) {
            fLight.setDirection(head.getRotation() + 90);
            fLight.setPosition(head.getX() + head.getWidth() / 2, head.getY() + head.getHeight() / 2);

            // If player is inside flashlight, makes him/her lose health. Also begins following the player.
            if (checkLightCollision(screen.player.getBX(), screen.player.getBY()) && !returning) {
                screen.player.loseHealth();
                following = true;
                // Clears timer so that it is effectively reset.
                unfollowTimer.clear();
            }
            // Otherwise checks if it's necessary to unfollow the player.
            else
                checkUnfollow();
        }

        if (moveTime > 2) {
            // Starts normal movement.
            if (!following && !returning)
                checkMove();
        }

        // Follows the player, attempting to continuously shine light on him.
        if (following)
            follow();
        // Returns to the spawn point, deactivating its light.
        if (returning)
            returnSpawn();

        if (!following && !returning && isMoving())
            rotateHead();

        // Ensures that the yellow lighter is moving in the correct direction at all times (or not moving if immobile).
        if (lighting && !following)
            eBody.setLinearVelocity((float) Math.cos(rotateAngle) * 25, (float) Math.sin(rotateAngle) * 25);

        tryMove();
    }

    private void tryMove() {
        if (eBody.getLinearVelocity().x != 0 || eBody.getLinearVelocity().y != 0) {
            setRegion(enemyAtlas.findRegion("YellowLighter2"));
            if (eBody.getLinearVelocity().x > 0 || (eBody.getLinearVelocity().y > 0 && eBody.getLinearVelocity().x == 0))
                eBody.setAngularVelocity(-5);
            else
                eBody.setAngularVelocity(5);
        }
        else {
            // Sets frame to inert frame and stops the player from rotating.
            setRegion(enemyAtlas.findRegion("YellowLighter1"));
            eBody.setAngularVelocity(0);
        }
    }

    // Checks whether or not the Yellow Lighter should move.
    private void checkMove() {
        // If the yellow lighter is currently out of bounds, moves the yellow lighter in the opposite velocity that
        // got it here.
        if (screen.bHandler.isOutOfBounds(getBX(), getBY()) && !returning) {
            switchLight();
            eBody.setLinearVelocity((float) Math.cos(head.getRotation()) * 25, (float) Math.sin(head.getRotation()) * 25);
            moveTime = 0;
            returning = true;
            return;
        }

        // Randomly selects a direction to move.
        int random = (int) (Math.random() * 5);
        // If zero, remain still and turn off light.
        if (random == 0) {
            eBody.setLinearVelocity(0, 0);
            // Switches light off if on.
            if (lighting)
                switchLight();
        }
        // Else sets the direction accordingly.
        else {
            // Switches light on if off.
            if (!lighting)
                switchLight();

            float angle = (float) Math.random() * 360;
            eBody.setLinearVelocity((float) Math.cos(angle) * 25, (float) Math.sin(angle) * 25);
            head.setRotation(angle);
            rotateAngle = angle;
        }
        moveTime = 0;
    }

    // Constantly acquires an angle and uses it to set the velocity.
    public void follow() {
        // If the yellow lighter is currently out of bounds, moves the yellow lighter in the opposite velocity that
        // got it here.
        if (screen.bHandler.isOutOfBounds(getBX(), getBY())) {
            following = false;
            checkMove();
            return;
        }

        // If for whatever reason the light is not on while following, turns it on.
        if (!lighting)
            switchLight();

        // Gets angle between the Yellow Lighter and the player.
        float angle = (float) Math.atan2(screen.player.getBY() - getBY(),
                screen.player.getBX() - getBX());
        // Sets the velocity based on the cosine and sine of the angle.
        eBody.setLinearVelocity((float) Math.cos(angle) * 25, (float) Math.sin(angle) * 25);
        head.setRotation(angle * MathUtils.radiansToDegrees - 90);
    }

    private void checkUnfollow() {
        unfollowTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!checkLightCollision(screen.player.getBX(), screen.player.getBY()))
                    following = false;
            }
        }, 2);
        unfollowTimer.start();
    }

    private void returnSpawn() {
        // Gets angle between the Yellow Lighter and the spawn point.
        float angle = (float) Math.atan2(spawnY - getBY(),
                spawnX - getBX());
        // Sets the velocity based on the cosine and sine of the angle.
        eBody.setLinearVelocity((float) Math.cos(angle) * 25, (float) Math.sin(angle) * 25);
        head.setRotation(angle * MathUtils.radiansToDegrees - 90);

        // If the yellow lighter is near enough to the spawn point, stops returning.
        if (getDistance(getBX(), getBY(), spawnX, spawnY) < 0.25f) {
            eBody.setLinearVelocity(0, 0);
            head.setRotation(0);
            setRotation(0);
            returning = false;
        }
    }

    // Increments the time based on Gdx.graphics.getDeltaTime().
    protected void updateTime(float deltaTime) {
        if (!returning && !following)
            moveTime += deltaTime;
    }

    public boolean getReturning() {
        return returning;
    }

}

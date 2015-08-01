package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class Gate extends TerrainObject {
    // Initial angle used later for swinging the gate open.
    float startAngle;
    // This sprite is used as a gate that swing open and shut. Look at the block class for a sufficient explanation on what
    // occurs in this constructor.
    public Gate(GameScreen screen, World world, TextureAtlas atlas, float x, float y, float angle) {
        super(screen, world, atlas);
        BodyDef tDef = new BodyDef();
        tDef.type = BodyDef.BodyType.StaticBody;
        tDef.position.set(x - 8, y - 2);
        tBody = world.createBody(tDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8, 2);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        Fixture fixture = tBody.createFixture(fDef);
        shape.dispose();

        // Acquires values for subtracting from the placement of the gate to ensure proper placement. Essentially,
        // ensures that the gate is always at the bottom left edge of the tile, as opposed to the center of it.
        // This is only necessary if the gate is not 90 or 270 degrees.
        float displacementX = (float) Math.cos(MathUtils.degreesToRadians * (angle - 90)) * 8;
        float displacementY = (float) Math.sin(MathUtils.degreesToRadians * (angle - 90)) * 2;
        // Sets x and y value with displacement.
        tBody.setTransform(x - displacementX, y - displacementY, 0);
        setRegion(terrainAtlas.findRegion("Gate"));
        setSize(getRegionWidth(), getRegionHeight());
        setPosition(tBody.getPosition().x - getWidth() / 2, tBody.getPosition().y - getHeight() / 2);
        tBody.setTransform(tBody.getPosition(), angle * MathUtils.degreesToRadians);
        // Sets rotation in accordance with what has been given.
        setOriginCenter();
        setRotation(MathUtils.radiansToDegrees * tBody.getAngle());
        startAngle = tBody.getAngle() * MathUtils.radiansToDegrees;
    }

    // Swings the gate open.
    public void activate() {
        Timer timer = new Timer();
        for (float i = 0; i <= 1; i += 0.01f) {
            final float offset = i;
            // Every 0.01 increases the angle of the gate by one degree. This gradually opens the gate up for the
            // player. Also sets the rotation of the sprite accordingly.
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    // The two displacement values ensure that the gate moves half a tile to the bottom right. This is to
                    // move the gate out of any potential collision paths. Note that the first value in the if statement
                    // is half a tile's width/height divided by 100 (the amount of times the loop iterates), while the
                    // second is slightly larger. This is to account for the width or height of the gate, depending
                    // on the angle of the gate.
                    float displacementX;
                    float displacementY;

                    if (startAngle == 90 || startAngle == 270) {
                        displacementX = 0.08f;
                        displacementY = 0.1f;
                    }
                    else {
                        displacementX = 0.1f;
                        displacementY = 0.08f;
                    }

                    // Sets the position of the body based on the displacement, as well as the angle of the body, which
                    // is based on the amount the start angle has been offset by in the loop.
                    tBody.setTransform(tBody.getPosition().x + displacementX, tBody.getPosition().y - displacementY,
                            (startAngle - offset * 90) * MathUtils.degreesToRadians);
                    // Updates the position and angle of the SPRITE.
                    setPosition(tBody.getPosition().x - getWidth() / 2, tBody.getPosition().y - getHeight() / 2);
                    setRotation(MathUtils.radiansToDegrees * tBody.getAngle());
                }
            }, i);
        }
        timer.start();
    }
}

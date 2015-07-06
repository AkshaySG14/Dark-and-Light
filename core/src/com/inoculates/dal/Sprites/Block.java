package com.inoculates.dal.Sprites;

import box2dLight.Light;
import com.badlogic.gdx.physics.box2d.*;

//Created by akshaysubramaniam on 25/6/15.

// This is just a normal block that prevents passage for the player. It also shapes much of the levels. Note that the
// block has no sprite. This is because the tile actually serves as the sprite
public class Block {
    // Initializes the block.
    public Block(World world, float x, float y) {
        // Creates the definition for the body of the block, and sets it to static.
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        // Sets the body's definition to bBody to apply the preceding lines to the body.
        Body bBody = world.createBody(bDef);

        // Creates the box shape and sets its width and height to 16.
        PolygonShape shape = new PolygonShape();
        // Sets the radius to half the sprite's width.
        shape.setAsBox(8, 8);

        // Defines the shape.
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        // Sets density for physics-related purposes.
        // Sets the fixture definitions that were just created to the body.
        Fixture fixture = bBody.createFixture(fDef);
        // Gets rid of unnecessary data.
        shape.dispose();

        // Sets the physical body position to the tile.
        bBody.setTransform(x, y, 0);
    }
}

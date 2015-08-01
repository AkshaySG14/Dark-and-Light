package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class LightBox extends TerrainObject {
    float color = 0;
    boolean lit = false;

    // Initializes a light box that serves as a switch. Look at the block class for a sufficient explanation on what occurs
    // in this constructor.
    public LightBox(GameScreen screen, World world, TextureAtlas atlas, TerrainObject link, float x, float y) {
        super(screen, world, atlas, link);
        BodyDef tDef = new BodyDef();
        tDef.type = BodyDef.BodyType.StaticBody;
        tBody = world.createBody(tDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8, 8);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        Fixture fixture = tBody.createFixture(fDef);
        shape.dispose();

        tBody.setTransform(x, y, 0);
        setRegion(terrainAtlas.findRegion("LightBox"));
        setSize(getRegionWidth(), getRegionHeight());
        setPosition(tBody.getPosition().x - getWidth() / 2, tBody.getPosition().y - getHeight() / 2);
        color = getColor().b;
        setAlpha(0.5f);
    }

    // Incrementally fills in the color of the box, to indicate how close it is to activating its link.
    public void colorIn() {
        // Box already lit and has activated the link.
        if (lit)
            return;
        // Otherwise color in by increasing the color float and then setting that as the blue pixel value.
        color += 2;
        setColor(getColor().r, getColor().g, color, getColor().a);
        setAlpha(0.5f + color / 510f);
        // If fully yellow, activates link and sets itself to lit. Additionally, sets the color to be green.
        if (color == 255) {
            link.activate();
            lit = true;
            setColor(Color.GREEN);
        }
    }

    // Does not activate.
    public void activate() {

    }
}

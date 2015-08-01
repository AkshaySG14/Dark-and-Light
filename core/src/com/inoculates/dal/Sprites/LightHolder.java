package com.inoculates.dal.Sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class LightHolder extends TerrainObject {
    // This sprite is used as a graphical indicator for point lights. Look at the block class for a sufficient explanation
    // on what occurs in this constructor.
    public LightHolder(GameScreen screen, World world, TextureAtlas atlas, Color color, float x, float y) {
        super(screen, world, atlas);
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
        setRegion(terrainAtlas.findRegion("LightHolder"));
        setSize(getRegionWidth(), getRegionHeight());
        setPosition(tBody.getPosition().x - getWidth() / 2, tBody.getPosition().y - getHeight() / 2);
        setColor(color);
    }

    // Does not activate.
    public void activate() {

    }
}

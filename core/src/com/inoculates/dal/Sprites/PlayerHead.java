package com.inoculates.dal.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class PlayerHead extends Head {

    // Initializes the head of the player.
    public PlayerHead(GameScreen screen, Player owner, TextureAtlas atlas) {
        super(screen, owner, atlas);
        // Sets the frame of the sprite to the player head frame.
        setRegion(headAtlas.findRegion("PlayerHead1"));
        // Sets sizing of the sprite.
        setSize(getRegionWidth(), getRegionHeight());
    }

    protected void update() {
        // Sets the rotation to the angle between the head center and the mouse. The subtraction by 90 serves a
        // correction, as value of 0 yielded would turn the face to the right, when it actuality an arc tangent that
        // yields 0 would mean that the head should be looking up.
        setOriginCenter();
        Vector2 mPos = screen.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        setRotation(MathUtils.radiansToDegrees * (float) Math.atan2(mPos.y - getY() - getHeight() / 2,
                mPos.x - getX() - getWidth() / 2) - 90);
    }

    public void changeColor(boolean colored) {
        if (colored)
            setRegion(headAtlas.findRegion("PlayerHead2"));
        else
            setRegion(headAtlas.findRegion("PlayerHead1"));
    }
}

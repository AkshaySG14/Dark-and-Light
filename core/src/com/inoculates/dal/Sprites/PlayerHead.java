package com.inoculates.dal.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.inoculates.dal.WorldHandlers.GameScreen;

//Created by akshaysubramaniam on 25/6/15.

public class PlayerHead extends Sprite {
    private GameScreen screen;
    private TextureAtlas playerAtlas;
    private Player owner;

    // Initializes the head of the player.
    public PlayerHead(GameScreen screen, Player owner, TextureAtlas atlas) {
        this.screen = screen;
        this.playerAtlas = atlas;
        this.owner = owner;

        // Sets the frame of the sprite to the player head frame.
        setRegion(playerAtlas.findRegion("PlayerHead1"));
        // Sets sizing of the sprite.
        setSize(8, 7);
    }

    public void draw(SpriteBatch batch) {
        // Continuously sets the position of the head to the center of the player body.
        setPosition(owner.getX() + owner.getWidth() / 2 - getWidth() / 2,
                owner.getY() + owner.getHeight() / 2 - getHeight() / 2);

        // Sets the rotation to the angle between the head center and the mouse. The subtraction by 90 serves a
        // correction, as value of 0 yielded would turn the face to the right, when it actuality an arc tangent that
        // yields 0 would mean that the head should be looking up.
        setOriginCenter();
        Vector2 mPos = screen.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        setRotation(MathUtils.radiansToDegrees * (float) Math.atan2(mPos.y - getY() - getHeight() / 2,
                mPos.x - getX() - getWidth() / 2) - 90);

        super.draw(batch);
    }

    public void changeColor(boolean colored) {
        if (colored)
            setRegion(playerAtlas.findRegion("PlayerHead2"));
        else
            setRegion(playerAtlas.findRegion("PlayerHead1"));
    }
}

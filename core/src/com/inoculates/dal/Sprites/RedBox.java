package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 28/7/15.

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.inoculates.dal.WorldHandlers.GameScreen;

public class RedBox extends Sprite {
    private GameScreen screen;
    private BitmapFont giveUp;
    private ShaderProgram fontShader;

    // This is the red box that displays the give up text.
    public RedBox(GameScreen screen, TextureAtlas atlas) {
        setRegion(atlas.findRegion("RedRectangle"));
        setSize(50, 25);
        this.screen = screen;

        Texture texture = new Texture(Gdx.files.internal("Fonts/screenfont.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture);
        // Creates the give up text.
        giveUp = new BitmapFont(Gdx.files.internal("Fonts/screenfont.fnt"), region, false);
        giveUp.setUseIntegerPositions(false);
        giveUp.setColor(Color.BLACK);
        giveUp.getData().setScale(0.25f);

        fontShader = new ShaderProgram(Gdx.files.internal("Shaders/font.vert"),
                Gdx.files.internal("Shaders/font.frag"));
    }

    // Draws the give up text and itself in the proper positions.
    public void draw(SpriteBatch batch) {
        setPosition(screen.camera.position.x - getWidth() / 2,
                screen.camera.position.y - 50);
        super.draw(batch);

        batch.setShader(fontShader);
        giveUp.draw(batch, "Give Up?", getX() + 7, getY() + 14);
        batch.setShader(null);
    }
}

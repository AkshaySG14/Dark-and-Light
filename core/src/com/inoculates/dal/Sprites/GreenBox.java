package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 28/7/15.

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.inoculates.dal.WorldHandlers.GameScreen;

public class GreenBox extends Sprite {
    private GameScreen screen;
    private BitmapFont tryAgain;
    private ShaderProgram fontShader;

    // This is the green box, largely the same as the red box.
    public GreenBox(GameScreen screen, TextureAtlas atlas) {
        setRegion(atlas.findRegion("GreenRectangle"));
        setSize(50, 25);
        this.screen = screen;

        Texture texture = new Texture(Gdx.files.internal("Fonts/screenfont.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture);

        tryAgain = new BitmapFont(Gdx.files.internal("Fonts/screenfont.fnt"), region, false);
        tryAgain.setUseIntegerPositions(false);
        tryAgain.setColor(Color.BLACK);
        tryAgain.getData().setScale(0.25f);

        fontShader = new ShaderProgram(Gdx.files.internal("Shaders/font.vert"),
                Gdx.files.internal("Shaders/font.frag"));
    }

    public void draw(SpriteBatch batch) {
        setPosition(screen.camera.position.x - getWidth() / 2,
                screen.camera.position.y + 50 - getHeight());
        super.draw(batch);

        batch.setShader(fontShader);
        tryAgain.draw(batch, "Try Again?", getX() + 3, getY() + 14);
        batch.setShader(null);
    }
}

package com.inoculates.dal.Sprites;

// Created by akshaysubramaniam on 28/7/15.

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Timer;
import com.inoculates.dal.WorldHandlers.GameScreen;

public class BlackScreen extends Sprite {
    private boolean displaying = false, lost = false;
    private GameScreen screen;
    private BitmapFont font;
    private GreenBox gBox;
    private RedBox rBox;
    private ShaderProgram fontShader;

    // This is the black screen that displays when the player either beats the level or loses.
    public BlackScreen(GameScreen screen, TextureAtlas atlas) {
        setRegion(atlas.findRegion("BlackScreen"));
        setSize(200, 150);
        this.screen = screen;
        this.lost = lost;

        // Creates the BitmapFont and sets its color to white so that it may be seen on the black screen.
        Texture texture = new Texture(Gdx.files.internal("Fonts/screenfont.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture);
        font = new BitmapFont(Gdx.files.internal("Fonts/screenfont.fnt"), region, false);
        font.setUseIntegerPositions(false);
        font.setColor(Color.WHITE);

        // Creates the red and green boxes that contain the give up and try again options respectively.
        gBox = new GreenBox(screen, atlas);
        rBox = new RedBox(screen, atlas);
        //Shader for the BitmapFonts.
        fontShader = new ShaderProgram(Gdx.files.internal("Shaders/font.vert"),
                Gdx.files.internal("Shaders/font.frag"));
        // Sets the scale so that the text is legible.
        font.getData().setScale(0.75f, 0.75f);
    }

    public void setDisplaying(boolean display) {
        displaying = display;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    // Draws the black screen only if set on display mode (when the player has died or won).
    public void draw(SpriteBatch batch) {
        if (displaying) {
            // Sets the position to the center of the camera, then draws.
            setPosition(screen.camera.position.x - screen.camera.viewportWidth / 2,
                    screen.camera.position.y - screen.camera.viewportHeight / 2);
            super.draw(batch);
            // Sets the shader of the text.
            batch.setShader(fontShader);
            // Sets the text depending on whether the user has lost or won.
            if (lost)
                font.draw(batch, "You Lost...", screen.camera.position.x - 70, screen.camera.position.y + 5);
            else
                font.draw(batch, "You Won!", screen.camera.position.x - 50, screen.camera.position.y + 5);
            // Sets the batch's shader back to null. This is to avoid other objects being drawn by the batch using
            // the font shader.
            batch.setShader(null);
            // Draws the green and red boxes.
            gBox.draw(batch);
            rBox.draw(batch);
        }
    }

    // This method ascertains whether the user has clicked on the green box or the red box and acts accordingly.
    public void checkClick(float x, float y) {
        if (x > gBox.getX() && x < gBox.getX() + gBox.getWidth() && y > gBox.getY() && y < gBox.getY() + gBox.getHeight())
            tryAgain();
        if (x > rBox.getX() && x < rBox.getX() + rBox.getWidth() && y > rBox.getY() && y < rBox.getY() + rBox.getHeight())
            giveUp();
    }

    // This method ascertains whether the user is hovering over the green or red box and highlights the box accordingly.
    public void checkHover(float x, float y) {
        if (x > gBox.getX() && x < gBox.getX() + gBox.getWidth() && y > gBox.getY() && y < gBox.getY() + gBox.getHeight())
            hoverGreen();
        else if (x > rBox.getX() && x < rBox.getX() + rBox.getWidth() && y > rBox.getY() && y < rBox.getY() + rBox.getHeight())
            hoverRed();
        // Otherwise clears any hovering that is occurring.
        else
            clearHover();
    }

    // Darkens the box to inform the user that something has occurred, and then restarts the game after 0.5 seconds.
    private void tryAgain() {
        gBox.setAlpha(0.25f);
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                gBox.setAlpha(1);
                screen.restart();
            }
        }, 0.5f);
        timer.start();
    }

    // Darkens the box to inform the user that something has occurred, and then ends the game after 0.5 seconds.
    private void giveUp() {
        rBox.setAlpha(0.25f);
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                screen.end();
            }
        }, 0.5f);
        timer.start();
    }

    // These two methods darken the two boxes slightly to show the user that he/she is mousing over one of the two
    // buttons.
    private void hoverGreen() {
        gBox.setAlpha(0.5f);
    }

    private void hoverRed() {
        rBox.setAlpha(0.5f);
    }

    // Sets the alpha to normal if the user is not hovering over something.
    private void clearHover() {
        gBox.setAlpha(1);
        rBox.setAlpha(1);
    }
}

package com.inoculates.dal.InputProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.inoculates.dal.WorldHandlers.GameScreen;

// Created by akshaysubramaniam on 25/6/15.

public class PlayerInput implements InputProcessor {
    GameScreen screen;
    boolean frozen = false;

    public PlayerInput(GameScreen screen) {
        this.screen = screen;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        // Turns the player light on/off.
        // If frozen returns to prevent any controller use.
        if (button == Input.Buttons.LEFT && !frozen)
            screen.player.switchLight();
        // If at the end game screen check for any according inputs.
        else if (frozen)
            screen.checkClick(x, y);

        return true;
    }

    public boolean touchDragged(int x, int y, int z) {
        return true;
    }

    public boolean keyTyped(char x) {
        return true;
    }

    public boolean scrolled(int x) {
        return true;
    }

    // Interprets the users input in the form of keys pressed and translates it to actions.
    public boolean keyDown(int x) {
        return true;
    }

    public boolean mouseMoved(int x, int y) {
        //if (frozen)
            screen.checkHover(x, y);
        return true;
    }

    public boolean keyUp(int x) {
        return true;
    }

    public boolean touchUp(int x, int y, int z, int a) {
        return true;
    }

    public void freeze() {
        frozen = true;
    }

    public void unFreeze() {
        frozen = false;
    }
}

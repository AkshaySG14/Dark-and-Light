package com.inoculates.dal.WorldHandlers;

import com.badlogic.gdx.Game;


public class DarkAndLight extends Game {

	@Override
	public void create () {
        setScreen(new GameScreen());
	}
}

package com.gudu.flappybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gudu.flappybird.screens.GameScreen;
import com.gudu.flappybird.screens.GetReadyScreen;

public class Application extends Game {
	public TextureAtlas textureAtlas;
	public AssetManager assets;
	public GetReadyScreen getReadyScreen;
	public GameScreen gameScreen;

	@Override
	public void create () {
		assets = new AssetManager();
		assets.load("assets.atlas", TextureAtlas.class);
		assets.load("sounds/sfx_wing.wav", Sound.class);
		assets.load("sounds/sfx_hit.wav", Sound.class);
		assets.load("sounds/sfx_point.wav", Sound.class);
		assets.finishLoading();
		textureAtlas = assets.get("assets.atlas");

		getReadyScreen = new GetReadyScreen(this);
		gameScreen = new GameScreen(this);

		setScreen(getReadyScreen);
	}

	@Override
	public void render () {
		super.render();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	@Override
	public void dispose () {
		super.dispose();
		textureAtlas.dispose();
		assets.dispose();
		getReadyScreen.dispose();
		gameScreen.dispose();
	}
}

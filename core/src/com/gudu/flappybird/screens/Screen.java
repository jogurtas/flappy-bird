package com.gudu.flappybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gudu.flappybird.Application;

public abstract class Screen implements com.badlogic.gdx.Screen {

    protected final Application app;

    public Screen(Application app) {
        this.app = app;
    }

//    @Override
//    public void show() {
//
//    }

    protected abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//    }
}

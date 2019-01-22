package com.gudu.flappybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gudu.flappybird.Application;
import com.gudu.flappybird.actors.Background;
import com.gudu.flappybird.actors.GetReadyText;

import static com.gudu.flappybird.utils.Constants.V_HEIGHT;
import static com.gudu.flappybird.utils.Constants.V_WIDTH;

public class GetReadyScreen extends Screen {
    private Viewport viewport;
    private Stage stage;

    public GetReadyScreen(Application app) {
        super(app);
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(V_WIDTH, V_HEIGHT, camera);
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Background bg = new Background(app.textureAtlas.createSprite("background_day"));
        bg.setSpeed(1);
        stage.addActor(bg);

        Background ground = new Background(app.textureAtlas.createSprite("ground"));
        ground.setSpeed(5);
        stage.addActor(ground);

        Array<TextureAtlas.AtlasRegion> frames = app.textureAtlas.findRegions("bird_orange");
        GetReadyText text = new GetReadyText(app.textureAtlas.createSprite("label_flappy_bird"), app.textureAtlas.findRegion("instructions"), frames);
        stage.addActor(text);
    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            app.setScreen(app.gameScreen);

        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

package com.gudu.flappybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gudu.flappybird.Application;
import com.gudu.flappybird.actors.*;
import com.gudu.flappybird.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.gudu.flappybird.utils.Constants.V_HEIGHT;
import static com.gudu.flappybird.utils.Constants.V_WIDTH;

public class GameScreen extends Screen {
    private OrthographicCamera hudCamera;
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;

    // physics
    private World world;
    private Box2DDebugRenderer b2dr;
    private float accumulator;

    // graphics
    private Background bg;
    private List<Pipe> pipes;
    private List<Ground> ground;
    private Bird bird;
    private GameOverText gameOverText;
    private BitmapFont font24;
    private GlyphLayout fontSize;

    private long pipeMovementTimer;
    private int score;

    // sounds
    private Sound sfxFlap, sfxDie, sfxScore;
    private boolean playedDieSfx;

    public GameScreen(Application app) {
        super(app);
        OrthographicCamera camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new StretchViewport(V_WIDTH, V_HEIGHT, camera);
        stage = new Stage(viewport);
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        world = new World(new Vector2(0, -50), true);
        b2dr = new Box2DDebugRenderer();

        // background
        bg = new Background(app.textureAtlas.createSprite("background_day"));
        stage.addActor(bg);

        // pipes
        pipes = new ArrayList<Pipe>();
        for (int i = 0; i < 4; i++) {
            Pipe p = new Pipe(world, app.textureAtlas.createSprite("pipe_green_top"), app.textureAtlas.createSprite("pipe_green_bottom"));
            pipes.add(p);
            stage.addActor(p);
        }
        pipeMovementTimer = TimeUtils.nanoTime();

        // ground
        ground = new ArrayList<Ground>();
        ground.add(new Ground(world, app.textureAtlas.createSprite("ground"), 0f, 6f));
        ground.add(new Ground(world, app.textureAtlas.createSprite("ground"), V_WIDTH, 6f));
        stage.addActor(ground.get(0));
        stage.addActor(ground.get(1));

        // bird
        Array<TextureAtlas.AtlasRegion> frames = app.textureAtlas.findRegions("bird_orange");
        bird = new Bird(world, frames);
        stage.addActor(bird);

        // game over text
        gameOverText = new GameOverText(app.textureAtlas.createSprite("label_game_over"));

        initFont(); // score font

        // sounds
        sfxFlap = app.assets.get("sounds/sfx_wing.wav");
        sfxDie = app.assets.get("sounds/sfx_hit.wav");
        sfxScore = app.assets.get("sounds/sfx_point.wav");

        // reset vars
        score = 0;
        pipeMovementTimer = 0;
        playedDieSfx = false;
        accumulator = 0;
    }

    @Override
    protected void update(float delta) {
        stepping(delta);
        stage.act(delta);

        if (!bird.isAlive()) {
            stage.addActor(gameOverText);
            stopMovement();
            if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                app.setScreen(app.getReadyScreen);
            }

            if (!playedDieSfx) {
                sfxDie.play();
                playedDieSfx = true;
            }
            return;
        }
        else if (bird.getBirdY() > V_HEIGHT) {
            bird.setAlive(false);
            return;
        }

        movePipes();
        updateScore();

        // update text size
        fontSize.setText(font24, Integer.toString(score));

        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            bird.flap(20);
            sfxFlap.play();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
//        b2dr.render(world, stage.getCamera().combined);

        batch.begin();
        font24.draw(batch, Integer.toString(score), Gdx.graphics.getWidth() / 2f - fontSize.width / 2f, Gdx.graphics.getHeight() / 1.1f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        batch.setProjectionMatrix(hudCamera.combined);
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
        batch.dispose();
        font24.dispose();
        b2dr.dispose();
        world.dispose();
        sfxScore.dispose();
        sfxDie.dispose();
        sfxFlap.dispose();
    }

    private void stepping(float delta) {
        float timeStep = 1 / 60f;
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep) {
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }
    }

    private void movePipes() {
        if (TimeUtils.timeSinceNanos(pipeMovementTimer) > 2000000000L) {
            for (Pipe p : pipes) {
                if (p.isReady()) {
                    p.setScored(false);
                    p.move(6);
                    break;
                }
            }
            pipeMovementTimer = TimeUtils.nanoTime();
        }
    }


    private void updateScore() {
        for (Pipe p : pipes) {
            if (p.getXPos() <= V_WIDTH / 4f && !p.isScored()) {
                score++;
                p.setScored(true);
                sfxScore.play();
            }
        }
    }

    private void stopMovement() {
        for (Pipe p : pipes) {
            p.move(0);
        }
        for (Ground g : ground) {
            g.move(0);
        }
        bg.setSpeed(0);
    }

    private void initFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font_score.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 35 * Gdx.graphics.getWidth() / 360;
        params.color = Color.WHITE;
        params.borderColor = Color.BLACK;
        params.borderWidth = 2f * Gdx.graphics.getWidth() / 360f;
        font24 = generator.generateFont(params);
        generator.dispose();
        font24.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        fontSize = new GlyphLayout();
    }
}

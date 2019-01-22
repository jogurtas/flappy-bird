package com.gudu.flappybird.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gudu.flappybird.utils.Box2DBodyBuilder;

import java.util.Random;

import static com.gudu.flappybird.utils.Constants.*;

public class Pipe extends Actor {
    private final static float GAP = 6;
    private final Sprite pipeTop, pipeBottom;
    private Random rand;
    private boolean scored = false;

    private final float initPipeX, initTopPipeY, initBottomPipeY;

    private Body topCollider, bottomCollider;

    public Pipe(World world, Sprite pipeTop, Sprite pipeBottom) {
        this.pipeTop = pipeTop;
        this.pipeBottom = pipeBottom;
        rand = new Random();

        pipeTop.setSize(pipeTop.getWidth() * SCALE, pipeTop.getHeight() * SCALE);
        pipeBottom.setSize(pipeBottom.getWidth() * SCALE, pipeBottom.getHeight() * SCALE);

        initPipeX = pipeTop.getWidth() / 2 + V_WIDTH;
        initTopPipeY = V_HEIGHT / 2 + pipeTop.getHeight() / 2 + GAP / 2;
        initBottomPipeY = V_HEIGHT / 2 - pipeBottom.getHeight() / 2 - GAP / 2;

        int heightDif = calculateRandom(-2, 7);
        topCollider = Box2DBodyBuilder.rect(world, initPipeX, initTopPipeY + heightDif, pipeTop.getWidth(), pipeTop.getHeight());
        bottomCollider = Box2DBodyBuilder.rect(world, initPipeX, initBottomPipeY + heightDif, pipeBottom.getWidth(), pipeBottom.getHeight());

    }

    @Override
    public void act(float delta) {
        pipeTop.setPosition(topCollider.getPosition().x - pipeTop.getWidth() / 2, topCollider.getPosition().y - pipeTop.getHeight() / 2);
        pipeBottom.setPosition(bottomCollider.getPosition().x - pipeBottom.getWidth() / 2, bottomCollider.getPosition().y - pipeBottom.getHeight() / 2);

        if (pipeTop.getX() < -pipeTop.getWidth()) {
            topCollider.setLinearVelocity(0, 0);
            bottomCollider.setLinearVelocity(0, 0);

            int heightDif = calculateRandom(-2, 7);
            topCollider.setTransform(initPipeX, initTopPipeY + heightDif, topCollider.getAngle());
            bottomCollider.setTransform(initPipeX, initBottomPipeY + heightDif, bottomCollider.getAngle());
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        pipeTop.draw(batch);
        pipeBottom.draw(batch);
    }


    public void move(float speed) {
        topCollider.setLinearVelocity(-speed, 0);
        bottomCollider.setLinearVelocity(-speed, 0);
    }

    public boolean isReady() {
        final float X = pipeTop.getWidth() / 2 + V_WIDTH;
        return topCollider.getPosition().x == X;
    }

    private int calculateRandom(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public float getXPos() {
        return topCollider.getPosition().x + pipeTop.getWidth() / 1.8f;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }
}

package com.gudu.flappybird.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.gudu.flappybird.utils.Box2DBodyBuilder;
import com.gudu.flappybird.utils.Constants;

public class Bird extends Actor implements ContactListener {
    private Body birdBody;
    private TextureAtlas.AtlasRegion deadBirdFrame;
    private boolean isAlive;

    private final float birdWidth;
    private final float birdHeight;

    private Animation<TextureAtlas.AtlasRegion> animation;
    private float elapsedTime = 0;

    public Bird(World world, Array<TextureAtlas.AtlasRegion> frames) {
        this.birdWidth = frames.get(0).getRegionWidth() * Constants.SCALE;
        this.birdHeight = frames.get(0).getRegionHeight() * Constants.SCALE;
        this.deadBirdFrame = frames.get(1);

        birdBody = Box2DBodyBuilder.circle(world, Constants.V_WIDTH / 4f, Constants.V_HEIGHT / 1.4f, birdHeight / 2f);

        animation = new Animation<TextureAtlas.AtlasRegion>(1 / 6f, frames);

        world.setContactListener(this);

        isAlive = true;

        setBounds(birdBody.getPosition().x, birdBody.getPosition().y, birdWidth, birdHeight);
    }

    @Override
    public void act(float delta) {
        elapsedTime += delta;

        if (Math.toDegrees(birdBody.getAngle()) > 20)
            birdBody.setTransform(birdBody.getPosition(), (float) Math.toRadians(20));
        if (birdBody.getLinearVelocity().y < 0)
            birdBody.setAngularVelocity(-1.5f);

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isAlive)
            batch.draw(deadBirdFrame,
                    birdBody.getPosition().x - birdWidth / 2, birdBody.getPosition().y - birdHeight / 2,
                    birdWidth / 2f, birdHeight / 2f,
                    birdWidth, birdHeight,
                    1, 1, (float) Math.toDegrees(birdBody.getAngle()));
        else
            batch.draw(animation.getKeyFrame(elapsedTime, true),
                    birdBody.getPosition().x - birdWidth / 2, birdBody.getPosition().y - birdHeight / 2,
                    birdWidth / 2f, birdHeight / 2f,
                    birdWidth, birdHeight,
                    1, 1, (float) Math.toDegrees(birdBody.getAngle()));
    }

    public void flap(float amount) {
        birdBody.setLinearVelocity(0, amount);
        birdBody.setAngularVelocity(3);
    }

    @Override
    public void beginContact(Contact contact) {
        birdBody.setFixedRotation(true);
        birdBody.setAngularVelocity(0);
        birdBody.setLinearVelocity(0, 0);
        birdBody.setLinearDamping(3f);

        isAlive = false;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public float getBirdY() {
        return birdBody.getPosition().y + birdHeight / 2f;
    }
}

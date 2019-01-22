package com.gudu.flappybird.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gudu.flappybird.utils.Box2DBodyBuilder;

import static com.gudu.flappybird.utils.Constants.*;

public class Ground extends Actor {
    private Sprite ground;
    private Body groundCollider;

    public Ground(World world, Sprite ground, float x, float speed) {
        this.ground = ground;
        ground.setSize(ground.getWidth() * SCALE, ground.getHeight() * SCALE);

        groundCollider = Box2DBodyBuilder.rect(world, x + ground.getX() + ground.getWidth() / 2, ground.getY() + ground.getHeight() / 2, ground.getWidth(), ground.getHeight());
        move(speed);
    }

    @Override
    public void act(float delta) {
        ground.setPosition(groundCollider.getPosition().x - ground.getWidth() / 2, groundCollider.getPosition().y - ground.getHeight() / 2);

        if (ground.getX() <= -ground.getWidth()) {
            groundCollider.setTransform(V_WIDTH + ground.getWidth() / 2, ground.getHeight() / 2, groundCollider.getAngle());
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ground.draw(batch);
    }

    public void move(float speed) {
        groundCollider.setLinearVelocity(-speed, 0);
    }
}

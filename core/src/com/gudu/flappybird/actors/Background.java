package com.gudu.flappybird.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gudu.flappybird.utils.Constants;

public class Background extends Actor {

    private int speed = 3;
    private Sprite bg0, bg1;
    private float xBg0, xBg1;

    public Background(Sprite bg) {
        this.bg0 = new Sprite(bg);
        this.bg1 = new Sprite(bg);
        bg0.setSize(bg0.getWidth() * Constants.SCALE, bg0.getHeight() * Constants.SCALE);
        bg1.setSize(bg1.getWidth() * Constants.SCALE, bg1.getHeight() * Constants.SCALE);

        setBounds(bg.getX(), bg.getY(), bg.getWidth(), bg.getHeight());

        xBg1 = Constants.V_WIDTH;
    }

    @Override
    public void act(float delta) {
        xBg0 -= speed * delta;
        xBg1 = xBg0 + Constants.V_WIDTH;
        if (xBg0 <= -20) {
            xBg0 = 0;
            xBg1 = Constants.V_WIDTH;
        }

        bg0.setPosition(xBg0, 0);
        bg1.setPosition(xBg1, 0);

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        bg0.draw(batch);
        bg1.draw(batch);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

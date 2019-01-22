package com.gudu.flappybird.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.gudu.flappybird.utils.Constants;

public class GameOverText extends Actor {
    private final Sprite text;

    public GameOverText(Sprite text) {
        this.text = text;
        text.setSize(text.getWidth() * Constants.SCALE, text.getHeight() * Constants.SCALE);
        text.setPosition(Constants.V_WIDTH / 2f - text.getWidth() / 2f, -text.getHeight());

        setBounds(text.getX(), text.getY(), text.getWidth(), text.getHeight());

        this.addAction(Actions.moveTo(Constants.V_WIDTH / 2f - text.getWidth() / 2f, Constants.V_HEIGHT / 1.5f, 1f, Interpolation.exp5Out));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        text.setColor(getColor());
        text.draw(batch, parentAlpha);
    }

    @Override
    protected void positionChanged() {
        text.setPosition(getX(), getY());
    }
}

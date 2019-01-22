package com.gudu.flappybird.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.gudu.flappybird.utils.Constants.*;

public class GetReadyText extends Actor {
    private Sprite text;
    private TextureRegion instructions;

    private Animation<TextureAtlas.AtlasRegion> animation;
    private float elapsedTime = 0;
    private final float birdWidth;
    private final float birdHeight;


    public GetReadyText(Sprite text, TextureRegion instructions, Array<TextureAtlas.AtlasRegion> frames) {
        this.birdWidth = frames.get(0).getRegionWidth() * SCALE;
        this.birdHeight = frames.get(0).getRegionHeight() * SCALE;

        this.instructions = instructions;

        this.text = text;
        text.setSize(text.getWidth() * SCALE / 1.2f, text.getHeight() * SCALE / 1.2f);
        text.setOriginCenter();

        float fullWidth = (text.getWidth() + birdWidth + 1) / 2;
        text.setOriginBasedPosition(V_WIDTH / 2 - (fullWidth - (text.getWidth() / 2)), V_HEIGHT / 1.4f);

        animation = new Animation<TextureAtlas.AtlasRegion>(1 / 7f, frames);

        this.addAction(sequence(alpha(0), parallel(
                Actions.fadeIn(1f),
                forever(sequence(Actions.moveBy(0, 0.5f, 0.4f, Interpolation.exp5), Actions.moveBy(0, -0.5f, 0.4f, Interpolation.exp5)))
        )));

        setBounds(text.getX(), text.getY(), fullWidth * 2, text.getHeight());
    }

    @Override
    public void act(float delta) {
        elapsedTime += delta;
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        text.setColor(getColor());
        text.draw(batch, parentAlpha);

        TextureAtlas.AtlasRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        float fullWidth = text.getWidth() + birdWidth;

        batch.draw(currentFrame, getX() + fullWidth - birdWidth / 2,
                getY() + text.getHeight() / 2 - birdHeight / 2,
                currentFrame.getRegionWidth() * SCALE * 1.02f,
                currentFrame.getRegionHeight() * SCALE * 1.02f);

        float width = instructions.getRegionWidth() * SCALE * 0.8f;
        float height = instructions.getRegionHeight() * SCALE * 0.8f;
        batch.draw(instructions, V_WIDTH / 2 - width / 2, V_HEIGHT / 2 - height / 2, width, height);
    }

    @Override
    protected void positionChanged() {
        text.setPosition(getX(), getY());
    }
}

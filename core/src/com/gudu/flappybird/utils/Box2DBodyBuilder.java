package com.gudu.flappybird.utils;

import com.badlogic.gdx.physics.box2d.*;

public class Box2DBodyBuilder {
    public static Body rect(World world, float x, float y, float width, float height) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.KinematicBody;
        bDef.position.set(x, y);
        bDef.fixedRotation = true;
        bDef.gravityScale = 0;

        Body body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1f;
        fDef.friction = 1f;
        body.createFixture(fDef);

        shape.dispose();

        return body;
    }

    public static Body circle(World world, float x, float y, float radius) {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(x, y);

        Body body = world.createBody(bDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = circle;
        fDef.density = 1f;
        fDef.friction = 1f;
        body.createFixture(fDef);

        circle.dispose();

        return body;
    }
}

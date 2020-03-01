package com.geekbrains.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GeekRpgGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture textureGrass;
    private Texture texturePointer;
    private Vector2 pointerPosition;
    private float rt;
    private Hero hero;

    private float newX = 100.0f;
    private float newY = 100.0f;

    // Домашнее задание:
    // - Разобраться с кодом
    // - Персонаж должен двигаться к указателю

    @Override
    public void create() {
        batch = new SpriteBatch();
        hero = new Hero();
        textureGrass = new Texture("grass.png");
        texturePointer = new Texture("pointer.png");
        pointerPosition = new Vector2(100, 100);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(textureGrass, i * 80, j * 80);
            }
        }
        batch.draw(texturePointer, pointerPosition.x - 32, pointerPosition.y - 32, 32, 32, 64, 64, 0.5f, 0.5f, rt, 0, 0, 64, 64, false, false);
        hero.render(batch, newX);
        batch.end();
    }

    public void update(float dt) {
        rt -= dt * 90.0f;

        if (Gdx.input.isTouched()) {
            newX = Gdx.input.getX();
            newY = 720 - Gdx.input.getY();
            pointerPosition.set(Gdx.input.getX(), 720.0f - Gdx.input.getY());
        }

        hero.update(dt, newX, newY);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
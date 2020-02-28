package com.geekbrains.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private float speed;

    public Hero() {
        this.texture = new Texture("hero.png");
        this.position = new Vector2(100, 100);
        this.speed = 100.0f;
    }

    public void render(SpriteBatch batch, float newX) {
        if (newX > position.x)
        batch.draw(texture, position.x - 32, position.y - 32,32,32,64,64,1,1,0,0,0,126,145,true,false);
        if (newX <= position.x)
        batch.draw(texture, position.x - 32, position.y - 32,32,32,64,64,1,1,0,0,0,126,145,false,false);
    }

    public void update(float dt, float newX, float newY) {
        if (position.x < newX){
            position.x += speed * dt;
        }
        if (position.x > newX){
            position.x -= speed * dt;
        }
        if (position.y < newY){
            position.y += speed * dt;
        }
        if (position.y > newY){
            position.y -= speed * dt;
        }
    }
}
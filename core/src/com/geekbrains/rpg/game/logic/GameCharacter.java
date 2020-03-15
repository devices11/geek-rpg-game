package com.geekbrains.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.geekbrains.rpg.game.logic.utils.MapElement;
import com.geekbrains.rpg.game.screens.utils.Assets;

public abstract class GameCharacter implements MapElement {
    public enum State {
        IDLE, MOVE, ATTACK, PURSUIT, RETREAT
    }

    static final int WIDTH = 60;
    static final int HEIGHT = 60;

    protected GameController gc;

    protected TextureRegion[][] textures;
    protected TextureRegion textureHp;

    protected State state;
    protected float stateTimer;

    protected GameCharacter lastAttacker;
    protected GameCharacter target;

    protected Vector2 position;
    protected Vector2 dst;
    protected Vector2 tmp;
    protected Vector2 tmp2;

    protected Circle area;

    protected float lifetime;
    protected float attackTime;
    protected float walkTime;
    protected float timePerFrame;

    protected float visionRadius;
    protected float speed;
    protected int hp, hpMax;

    protected Weapon weapon;

    public int getCellX() {
        return (int) position.x / 80;
    }

    public int getCellY() {
        return (int) (position.y - 20) / 80;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void changePosition(float x, float y) {
        position.set(x, y);
        if (position.x < 0.1f) {
            position.x = 0.1f;
        }
        if (position.y - 20 < 0.1f) {
            position.y = 20.1f;
        }
        if (position.x > Map.MAP_CELLS_WIDTH * 80 - 1) {
            position.x = Map.MAP_CELLS_WIDTH * 80 - 1;
        }
        if (position.y - 20 > Map.MAP_CELLS_HEIGHT * 80 - 1) {
            position.y = Map.MAP_CELLS_HEIGHT * 80 - 1 + 20;
        }
        area.setPosition(position.x, position.y - 20);
    }

    public void changePosition(Vector2 newPosition) {
        changePosition(newPosition.x, newPosition.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getArea() {
        return area;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public GameCharacter(GameController gc, int hpMax, float speed) {
        this.gc = gc;
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.tmp = new Vector2(0.0f, 0.0f);
        this.tmp2 = new Vector2(0.0f, 0.0f);
        this.dst = new Vector2(0.0f, 0.0f);
        this.position = new Vector2(0.0f, 0.0f);
        this.area = new Circle(0.0f, 0.0f, 15);
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.speed = speed;
        this.state = State.IDLE;
        this.stateTimer = 1.0f;
        this.timePerFrame = 0.2f;
        this.target = null;
    }

    public int getCurrentFrameIndex() {
        return (int)(walkTime / timePerFrame) % textures[0].length;
    }

    public void update(float dt) {
        lifetime += dt;
        if (state == State.ATTACK) {
            dst.set(target.getPosition());
        }
        if (state == State.MOVE || state == State.RETREAT || (state == State.ATTACK && this.position.dst(target.getPosition()) > weapon.getRange() - 10)) {
            moveToDst(dt);
        }
        if (state == State.ATTACK && this.position.dst(target.getPosition()) < weapon.getRange()) {
            attackTime += dt;
            if (attackTime > weapon.getSpeed()) {
                attackTime = 0.0f;
                if (weapon.getType() == Weapon.Type.MELEE) {
                    target.takeDamage(this, weapon.generateDamage());
                }
                if (weapon.getType() == Weapon.Type.RANGED && target != null) {
                    gc.getProjectilesController().setup(this, position.x, position.y, target.getPosition().x, target.getPosition().y, weapon.generateDamage());
                }
            }
        }
    }

    public void moveToDst(float dt) {
        tmp.set(dst).sub(position).nor().scl(speed);
        tmp2.set(position);
        walkTime += dt;
        if (position.dst(dst) > speed * dt) {
            changePosition(position.x + tmp.x * dt, position.y + tmp.y * dt);
        } else {
            changePosition(dst);
            state = State.IDLE;
        }
        if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
            changePosition(tmp2.x + tmp.x * dt, tmp2.y);
            if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
                changePosition(tmp2.x, tmp2.y + tmp.y * dt);
                if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
                    changePosition(tmp2);
                }
            }
        }
    }

    public boolean takeDamage(GameCharacter attacker, int amount) {
        lastAttacker = attacker;
        hp -= amount;
        if (hp <= 0) {
            onDeath();
            return true;
        }
        return false;
    }

    public void resetAttackState() {
        dst.set(position);
        state = State.IDLE;
        target = null;
    }

    public void onDeath() {
        for (int i = 0; i < gc.getAllCharacters().size(); i++) {
            GameCharacter gameCharacter = gc.getAllCharacters().get(i);
            if (gameCharacter.target == this) {
                gameCharacter.resetAttackState();
            }
        }
    }
}

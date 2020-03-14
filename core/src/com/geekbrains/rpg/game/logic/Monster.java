package com.geekbrains.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.geekbrains.rpg.game.logic.utils.Poolable;
import com.geekbrains.rpg.game.screens.utils.Assets;

import static com.geekbrains.rpg.game.logic.GameCharacter.Type.MELEE;
import static com.geekbrains.rpg.game.logic.GameCharacter.Type.RANGED;

public class Monster extends GameCharacter implements Poolable {
    private float chance = MathUtils.random(0.0f, 100.0f);

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public Monster(GameController gc) {
        super(gc, 20, 100.0f);
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.changePosition(800.0f, 300.0f);
        this.dst.set(this.position);
        if (chance < 50.0f) this.type = RANGED;
        else this.type = MELEE;
        weapon.setup(type);
        this.visionRadius = weapon.getAttackRange();
        this.attackRadius = weapon.getAttackRange();
        this.damage = weapon.getDamage();
        this.attackSpeed = weapon.getAttackSpeed();
        System.out.println("-------" + getClass().getSimpleName() + "-------");
        System.out.println(weapon.getQualityWeapon());
        System.out.println("type " + type.toString());
        System.out.println("attackRange " + attackRadius);
        System.out.println("damage " + damage);
        System.out.println("attackSpeed " + attackSpeed);

    }

    public void generateMe() {
        do {
            changePosition(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        } while (!gc.getMap().isGroundPassable(position));
        hpMax = 20;
        hp = hpMax;
    }

    @Override
    public void onDeath() {
        super.onDeath();
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        batch.setColor(0.5f, 0.5f, 0.5f, 0.7f);
        batch.draw(texture, position.x - 30, position.y - 30, 30, 30, 60, 60, 1, 1, 0);
        batch.setColor(1, 1, 1, 1);
        batch.draw(textureHp, position.x - 30, position.y + 30, 60 * ((float) hp / hpMax), 12);
    }

    public void update(float dt) {
        super.update(dt);
        stateTimer -= dt;
        if (stateTimer < 0.0f) {
            if (state == State.ATTACK) {
                target = null;
            }
            state = State.values()[MathUtils.random(0, 1)];
            if (state == State.MOVE) {
                dst.set(MathUtils.random(1280), MathUtils.random(720));
            }
            stateTimer = MathUtils.random(2.0f, 5.0f);
        }
        if (state != State.RETREAT && this.position.dst(gc.getHero().getPosition()) < visionRadius) {
            state = State.ATTACK;
            target = gc.getHero();
            stateTimer = 10.0f;
        }
        if (hp < hpMax * 0.2 && state != State.RETREAT) {
            state = State.RETREAT;
            stateTimer = 1.0f;
            dst.set(position.x + MathUtils.random(100, 200) * Math.signum(position.x - lastAttacker.position.x),
                    position.y + MathUtils.random(100, 200) * Math.signum(position.y - lastAttacker.position.y));
        }
    }
}

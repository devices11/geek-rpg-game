package com.geekbrains.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.geekbrains.rpg.game.logic.utils.Poolable;

import static com.geekbrains.rpg.game.logic.GameCharacter.Type.RANGED;

public class Weapon implements Poolable {
//    public enum WeaponType {
//        MELEE, RANGED
//    }

    public enum QualityWeapon {
        COMMON, RARE, EPIC, HEROIC, DIVINE
    }

    protected QualityWeapon qualityWeapon;
    protected float attackRange;
    protected int damage;
    protected float attackSpeed;
    protected float chance;
    protected boolean withOwner;

    @Override
    public boolean isActive() {
        return true;
    }

    public QualityWeapon getQualityWeapon() {
        return qualityWeapon;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public int getDamage() {
        return damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public boolean isWithOwner() {
        return withOwner;
    }

    public Weapon (){
        this.chance = MathUtils.random(0.0f, 100.0f);
        this.qualityWeapon = definitionQuality(chance);
        this.attackRange = 0.0f;
        this.damage = 0;
        this.attackSpeed = 0.0f;
        this.withOwner = false; //если false не отрисовываем, пока не используется
    }

    public void setup(GameCharacter.Type type) {
        characteristic(qualityWeapon, type);
    }

    public QualityWeapon definitionQuality (float chance){
        if (chance > 0.0f && chance <= 0.5f) return QualityWeapon.DIVINE;
        else if (chance > 0.5f && chance <= 1.5f) return QualityWeapon.HEROIC;
        else if (chance > 1.5f && chance <= 5.0f) return QualityWeapon.EPIC;
        else if (chance > 5.0f && chance <= 10.0f) return QualityWeapon.RARE;
        else return QualityWeapon.COMMON;
    }

    public void characteristic (QualityWeapon qualityWeapon, GameCharacter.Type type) {
        if (qualityWeapon.equals(QualityWeapon.DIVINE)){
            updateCharacteristic(type,70.0f, MathUtils.random(10, 30), 0.1f);
        }
        if (qualityWeapon.equals(QualityWeapon.HEROIC)){
            updateCharacteristic(type,60.0f, MathUtils.random(8, 12), 0.2f);
        }
        if (qualityWeapon.equals(QualityWeapon.EPIC)){
            updateCharacteristic(type,50.0f, MathUtils.random(5, 8), 0.3f);
        }
        if (qualityWeapon.equals(QualityWeapon.RARE)){
            updateCharacteristic(type,45.0f, MathUtils.random(3, 6), 0.4f);
        }
        if (qualityWeapon.equals(QualityWeapon.COMMON)){
            updateCharacteristic(type,40.0f, MathUtils.random(2, 4), 0.5f);
        }
    }

    public void updateCharacteristic(GameCharacter.Type type, float attackRng, int dmg, float attackSpd){
        if (type.equals(RANGED)) {
            attackRange = attackRng * 6;
            damage = dmg * 2;
            attackSpeed = attackSpd * 1.5f;
        } else {
            attackRange = attackRng;
            damage = dmg;
            attackSpeed = attackSpd;
        }
    }

}

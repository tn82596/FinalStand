package entities;

import static utilities.Constants.EnemyConstants.*;

import main.Game;

import static utilities.Constants.Directions.*;
import static utilities.HelpMethods.CanMoveHere;

import gamestates.Playing;

public abstract class Enemy extends Entities {
	protected int enemyType;
	protected int walkDir = LEFT;
	protected int healthWidth;
	
	protected int attackRange = 30 * Game.SCALE;
	
	protected int lastHitDuration = 360;
	protected int lastHitTimer = 0;
	protected int freezeDuration = 600;
	protected int freezeTimer = 0;
	protected int bleedDuration = 480;
	protected int bleedTimer = 0;

	protected boolean lockedInAnimation = false;
	protected boolean attackChecked;
	protected boolean alive = true;

	public Enemy(float x, float y, int w, int h, Playing playing, int enemyType) {
		super(x, y, w, h, playing);
		this.enemyType = enemyType;
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = 0.25f * Game.SCALE;
	}

	protected void updateAnimationTick() {
		if (frozen() && freezeTimer > 0)
			return;
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getSpriteAmount(action, enemyType)) {
				aniIndex = 0;

				switch (action) {
				case ATTACK, KNOCKBACK -> changeAction(IDLE);
				case DEAD -> alive = false;
				}
			}
		}
	}

	protected void move() {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xSpeed;
			return;
		}
	}

	protected void knockback(float knockback) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = knockback;
		else
			xSpeed = -knockback;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xSpeed;
			return;
		}
	}

	public int hurt(int damage, boolean knockback) {
		lastHitTimer = lastHitDuration;
		int damageDone = damage;
		currentHealth -= damage;
		if (currentHealth < 0.001f) {
			damageDone = (int) currentHealth + damage;
			currentHealth = 0;
			freezeTimer = 0;
			changeAction(DEAD);
		}
		else if (knockback && !frozen())
			changeAction(KNOCKBACK);
		return damageDone;
	}

	public void hurt(float damage) {
		lastHitTimer = lastHitDuration;
		currentHealth -= damage;
		if (currentHealth < 0.001f) {
			currentHealth = 0;
			bleedTimer = 0;
			freezeTimer = 0;
			changeAction(DEAD);
		}
	}
	
	public void freeze() {
		freezeTimer = freezeDuration;
	}
	
	public void bleed() {
		bleedTimer = bleedDuration;
	}

	public int flipX() {
		if (walkDir == LEFT)
			return w * Game.SCALE;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == LEFT)
			return -1;
		else
			return 1;
	}
	
	protected void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * hitbox.width / 2);
	}

	protected void changeAction(int action) {
		this.action = action;
		aniIndex = 0;
		aniTick = 0;
	}
	
	protected boolean frozen() {
		return (freezeTimer > 0);
	}
	
	protected boolean bleeding() {
		return (bleedTimer > 0);
	}

	public boolean isDeadAni() {
		if (action == DEAD)
			return true;
		return false;
	}
	
	public boolean inRange(Player player) {
		if (playing.allyInRange(attackBox))
			return true;
		else if (player.inStealth())
			return false;
		else
			return attackBox.intersects(player.getHitbox());
	}

	/*
	 * private void setAggro() { if (playerInFront()) walkDir = LEFT; else walkDir =
	 * RIGHT; }
	 */

	public boolean isAlive() {
		return alive;
	}
}

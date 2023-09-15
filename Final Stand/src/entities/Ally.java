package entities;

import java.awt.geom.Rectangle2D;

import gamestates.Playing;

import static utilities.Constants.AllyConstants.*;
import static utilities.Constants.Directions.*;
import static utilities.HelpMethods.*;

import main.Game;

public abstract class Ally extends Entities {

	protected int allyType;
	protected int walkDir = RIGHT;

	protected int attackRange = 30 * Game.SCALE;

	protected boolean lockedInAnimation = false;
	protected boolean attackChecked = false;
	protected boolean alive = true;

	public Ally(float x, float y, int w, int h, Playing playing, int allyType) {
		super(x, y, w, h, playing);
		this.allyType = allyType;
		maxHealth = GetMaxHealth(allyType);
		currentHealth = maxHealth;
		walkSpeed = 0.25f * Game.SCALE;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(action, allyType)) {
				aniIndex = 0;

				switch (action) {
				case ATTACK -> changeAction(IDLE);
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

	public void hurt(int damage) {
		currentHealth -= damage;
		if (currentHealth <= 0)
			changeAction(DEAD);
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

	protected void changeAction(int action) {
		this.action = action;
		aniIndex = 0;
		aniTick = 0;
	}

	public boolean IsDeadAni() {
		if (action == DEAD)
			return true;
		return false;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getAllyAction() {
		return action;
	}

	public boolean isAlive() {
		return alive;
	}

}

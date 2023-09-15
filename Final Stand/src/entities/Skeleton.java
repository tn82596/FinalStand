package entities;

import static utilities.Constants.EnemyConstants.*;

import main.Game;

import static utilities.Constants.Directions.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import gamestates.Playing;
import main.Game;

public class Skeleton extends Enemy {
	private int attackCooldown = 120;
	private int attackTimer = attackCooldown;

	public Skeleton(float x, float y, Playing playing) {
		super(x, y, SKELETON_WIDTH, SKELETON_HEIGHT, playing, SKELETON);
		initializeHitbox(27 * Game.SCALE, 34 * Game.SCALE);
		initalizeAttackBox();
	}

	private void initalizeAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, 19 * Game.SCALE, 20 * Game.SCALE);
	}

	private void updateAttackBox() {
		if (walkDir == RIGHT) {
			attackBox.x = (int) (hitbox.x + (float) (27 * Game.SCALE) / 3);
		} else if (walkDir == LEFT) {
			attackBox.x = (int) (hitbox.x - attackBox.width + ((float) (27 * Game.SCALE) / 9));
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	public void update(Player player) {
		if (frozen()) {
			freezeTimer--;
			return;
		}
		
		if (bleeding()) {
			hurt((float) 10 / 120);
			bleedTimer--;
		}
		System.out.println(currentHealth);
		updateBehavior(player);
		updateAnimationTick();
		updateAttackBox();
		updateHealthBar();
		attackTimer--;
		lastHitTimer--;
	}


	// future proofed for adding backwards running
	private void updateBehavior(Player player) {
		boolean inRange = inRange(player);
		switch (action) {
		case IDLE:
			if (!inRange)
				changeAction(RUN);
			else if (attackTimer <= 0)
				changeAction(ATTACK);
			break;
		case RUN:
			if (inRange)
				changeAction(IDLE);
			else
				move();
			break;
		case ATTACK:
			attackTimer = attackCooldown;
			if (aniIndex == 0)
				attackChecked = false;

			if (aniIndex == 3 && !attackChecked) {
				attackChecked = true;
				if (!playing.checkAllyHit(attackBox, GetEnemyDmg(SKELETON)))
					player.checkPlayerHit(attackBox, GetEnemyDmg(SKELETON));
			}
			break;
		case KNOCKBACK:
			knockback(player.getKnockback());
			break;
		}
	}

	public void renderAttackBox(Graphics g, int mapOffset) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x - mapOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	public void renderHealthBar(Graphics g, int mapOffset) {
		if (currentHealth < maxHealth && lastHitTimer > 0) {
		g.setColor(Color.green);
		g.fillRect((int)(hitbox.x + hitbox.width / 4) - mapOffset, (int) hitbox.y - 4 * Game.SCALE, healthWidth, 2 * Game.SCALE);
		g.setColor(Color.red);
		g.fillRect((int)(hitbox.x + hitbox.width / 4 + healthWidth) - mapOffset, (int) hitbox.y - 4 * Game.SCALE, (int) hitbox.width / 2 - healthWidth, 2 * Game.SCALE);
		}
	}
	
}

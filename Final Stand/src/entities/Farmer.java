package entities;

import gamestates.Playing;
import main.Game;

import static utilities.Constants.AllyConstants.*;
import static utilities.Constants.Directions.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Farmer extends Ally {

	public Farmer(float x, float y, Playing playing) {
		super(x, y, FARMER_WIDTH, FARMER_HEIGHT, playing, FARMER);
		initializeHitbox(13 * Game.SCALE, 24 * Game.SCALE);
		initializeAttackBox();
	}

	private void initializeAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, 19 * Game.SCALE, 20 * Game.SCALE);
	}

	private void updateAttackBox() {
		if (walkDir == RIGHT) {
			attackBox.x = hitbox.x;
		} else if (walkDir == LEFT) {
			attackBox.y = hitbox.y;
		}
	}

	public void update() {
		updateBehavior();
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateBehavior() {
		switch (action) {
		case IDLE:
			changeAction(RUN);
			break;
		case RUN:
			if (playing.enemyInRange(attackBox))
				changeAction(ATTACK);
			else
				move();
			break;
		case ATTACK:
			if (aniIndex == 0)
				attackChecked = false;

			if (aniIndex == 3 && !attackChecked) {
				attackChecked = true;
				playing.checkEnemyHit(attackBox, GetAllyDmg(FARMER));
			}
			break;
		}
	}

	public void renderAttackBox(Graphics g, int mapOffset) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x - mapOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
}

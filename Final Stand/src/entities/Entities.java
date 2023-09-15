package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import gamestates.Playing;

public abstract class Entities {
	protected Playing playing;
	protected float x, y;
	protected int w, h;
	protected Rectangle2D.Float hitbox;
	protected Rectangle2D.Float attackBox;
	protected int aniTick, aniIndex, aniSpeed = 15;
	protected int action;

	protected float maxHealth, currentHealth;
	protected float walkSpeed;

	public Entities(float x, float y, int w, int h, Playing playing) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.playing = playing;

	}

	protected void initializeHitbox(float w, float h) {
		hitbox = new Rectangle2D.Float(x, y, w, h);
	}

	// debug hitbox
	protected void renderHitbox(Graphics g, int mapOffset) {
		g.setColor(Color.red);
		g.drawRect((int) hitbox.x - mapOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	// update hitbox position
	/*
	 * protected void updateHitbox() { hitbox.x = (int) x; hitbox.y = (int) y; }
	 */

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public int getAction() {
		return action;
	}

	public int getAniIndex() {
		return aniIndex;
	}
}

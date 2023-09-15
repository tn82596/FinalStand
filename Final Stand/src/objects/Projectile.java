package objects;

import java.awt.geom.Rectangle2D;
import main.Game;

import static utilities.Constants.Projectiles.*;

public class Projectile {
	private Rectangle2D.Float hitbox;
	private int direction;
	private int hitboxOffsetX, hitboxOffsetY;
	private int spawnOffsetX, spawnOffsetY;
	private int hitboxWidth, hitboxHeight;
	private int x, y;
	private int aoe;
	private int projectileType;
	private boolean active = true;
	int flipX = 0, hitboxFlip = 0, flipW = 1;

	public Projectile(int x, int y, int direction, int projectileType) {		
		this.direction = direction;
		this.projectileType = projectileType;
		
		switch(projectileType) {
		case ARROW: 
		case ICE_ARROW:
			spawnOffsetX = 21 * Game.SCALE;
			spawnOffsetY = 19 * Game.SCALE;
			hitboxOffsetX = ARROW_OFFSETX;
			hitboxOffsetY = ARROW_OFFSETY;
			hitboxWidth = ARROW_HITBOX_WIDTH;
			hitboxHeight = ARROW_HITBOX_HEIGHT;
			break;
		case BLADE_BEAM_PROJECTILE:
			spawnOffsetX = 38 * Game.SCALE;
			spawnOffsetY = -12 * Game.SCALE;
			hitboxOffsetX = BLADE_BEAM_OFFSETX;
			hitboxOffsetY = BLADE_BEAM_OFFSETY;
			hitboxWidth = BLADE_BEAM_HITBOX_WIDTH;
			hitboxHeight = BLADE_BEAM_HITBOX_HEIGHT;
		case DAGGER:
			spawnOffsetX = 8 * Game.SCALE;
			spawnOffsetY = 18 * Game.SCALE;
			hitboxOffsetX = DAGGER_OFFSETX;
			hitboxOffsetY = DAGGER_OFFSETY;
			hitboxWidth = DAGGER_HITBOX_WIDTH;
			hitboxHeight = DAGGER_HITBOX_HEIGHT;
		}
		
		if (direction == -1) {
			flipX = PROJECTILE_WIDTH * Game.SCALE;
			spawnOffsetX *= -1;
			flipW = -1;
		}

		this.x = x + spawnOffsetX;
		this.y = y + spawnOffsetY;
		hitbox = new Rectangle2D.Float(this.x, this.y, hitboxWidth, hitboxHeight);
	
	}
	

	public void updatePosition() {
		hitbox.x += direction * SPEED;
		x += direction * SPEED;
	}

	public void setPositon(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
	}

	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public int getFlipW() {
		return flipW;
	}
	
	public int getFlipX() {
		return flipX;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public int getHitboxOffsetX() {
		return hitboxOffsetX;
	}
	
	public int getHitboxOffsetY() {
		return hitboxOffsetY;
	} 
	
	public boolean isIceArrow() {
		if (projectileType == ICE_ARROW)
			return true;
		return false;
	}
}

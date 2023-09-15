package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilities.Constants.AttackTypes.*;
import static utilities.Constants.StatusEffects.*;

import entities.Player;
import gamestates.Playing;
import main.Game;
import utilities.SaveAndLoad;
import static utilities.Constants.Projectiles.*;

public class ObjectManager {
	private Playing playing;
	private BufferedImage[] projectileTypes;
	private int projectileType;
	private int damage;
	private ArrayList<Projectile> projectiles = new ArrayList<>();

	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadObjects();
		loadSprites();
	}

	public void update(Player player) {
		updateProjectiles(player);
	}

	public void render(Graphics g, int mapOffset) {
		renderProjectiles(g, mapOffset);
	}

	private void renderProjectiles(Graphics g, int mapOffset) {
		for (Projectile p : projectiles)
			if (p.isActive()) {
				g.drawImage(projectileTypes[projectileType], (int) (p.getX() - p.getHitboxOffsetX()) - mapOffset + p.getFlipX(), (int) (p.getY() - p.getHitboxOffsetY()), PROJECTILE_WIDTH * Game.SCALE * p.getFlipW(),
						PROJECTILE_HEIGHT * Game.SCALE, null);
				g.drawRect((int) p.getHitbox().x - mapOffset, (int) p.getHitbox().y, (int) p.getHitbox().width,
						(int) p.getHitbox().height);
			}
	}

	private void updateProjectiles(Player player) {
		int damageDone;
		for (Projectile p : projectiles)
			if (p.isActive()) {
				p.updatePosition();
				if (maxDistanceTraveled(p, player))
					p.setActive(false);
				if (p.isIceArrow())
					damageDone = playing.checkEnemyHit(p.getHitbox(), damage, false, SINGLE, FREEZE);
				else
					damageDone = playing.checkEnemyHit(p.getHitbox(), damage, false, SINGLE, NONE);
				if (damageDone > 0) {
					p.setActive(false);
					player.addProjectileUltCharge(damageDone);
				}
			}
	}

	private boolean maxDistanceTraveled(Projectile p, Player player) {
		if (Math.abs(player.getHitbox().x - p.getHitbox().x) >= player.getRange())
			return true;
		return false;
	}

	public void addProjectile(Player player, int projectileType, int damage) {
		projectiles.add(new Projectile((int) player.getHitbox().x, (int) player.getHitbox().y, player.getFlipW(), projectileType));
		this.projectileType = projectileType;
		this.damage = damage;
	}

	private void loadSprites() {
		BufferedImage img = SaveAndLoad.createSprite("/projectiles.png");

		projectileTypes = new BufferedImage[4];
		for (int i = 0; i < projectileTypes.length; i++)
				projectileTypes[i] = img.getSubimage(i * 128, 0, 128, 64);
	}

	public void loadObjects() {
		projectiles.clear();
	}

}

package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import main.Game;
import utilities.SaveAndLoad;
import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.AttackTypes.*;
import static utilities.Constants.StatusEffects.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] skeletonAnimations;
	private ArrayList<Skeleton> skeletons = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadSprites();
		addSkeleton();

	}

	public void update(Player player) {
		for (Skeleton s : skeletons)
			if (s.isAlive())
				s.update(player);
	}

	public void render(Graphics g, int mapOffset) {
		renderSkeletons(g, mapOffset);
	}

	public void addSkeleton() {
		skeletons.add(new Skeleton(920 * Game.SCALE, 116 * Game.SCALE, playing));

	}

	private void renderSkeletons(Graphics g, int mapOffset) {
		for (Skeleton s : skeletons) {
			if (s.isAlive()) {
				g.drawImage(skeletonAnimations[s.getAction()][s.getAniIndex()],
						(int) (s.getHitbox().x - SKELOFFSETX) - mapOffset + s.flipX(),
						(int) (s.getHitbox().y - SKELOFFSETY), s.w * Game.SCALE * s.flipW(), s.h * Game.SCALE, null);
				s.renderAttackBox(g, mapOffset);
				s.renderHitbox(g, mapOffset);
				s.renderHealthBar(g, mapOffset);
			}
		}
	}

	private void loadSprites() {
		BufferedImage img = SaveAndLoad.createSprite("/skeleton.png");

		skeletonAnimations = new BufferedImage[5][5];
		for (int i = 0; i < skeletonAnimations.length; i++)
			for (int j = 0; j < skeletonAnimations[i].length; j++)
				skeletonAnimations[i][j] = img.getSubimage(j * SKELETON_WIDTH, i * SKELETON_HEIGHT, SKELETON_WIDTH,
						SKELETON_HEIGHT);
	}

	public int checkEnemyHit(Rectangle2D.Float attackBox, int damage, boolean knockback, int attackType, int statusEffect) {
		int damageDone = 0;
		for (Skeleton s : skeletons)
			if (s.isAlive() && !s.isDeadAni())
				if (attackBox.intersects(s.getHitbox())) {
					damageDone += s.hurt(damage, knockback);
					if (statusEffect == FREEZE)
						s.freeze();
					else if (statusEffect == BLEED)
						s.bleed();
					if (attackType == SINGLE)
						return damageDone;
				}
		return damageDone;
	}

	public boolean checkEnemyHit(Rectangle2D.Float attackBox, int damage) {
		for (Skeleton s : skeletons)
			if (s.isAlive() && !s.isDeadAni())
				if (attackBox.intersects(s.getHitbox())) {
					s.hurt(damage);
					return true;
				}
		return false;
	}

	public boolean enemyInRange(Rectangle2D.Float attackBox) {
		for (Skeleton s : skeletons)
			if (s.isAlive())
				if (attackBox.intersects(s.getHitbox()))
					return true;
		return false;
	}
}

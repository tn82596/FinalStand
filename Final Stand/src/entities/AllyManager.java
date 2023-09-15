package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilities.Constants.AllyConstants.*;
import gamestates.Playing;
import main.Game;
import utilities.SaveAndLoad;

public class AllyManager {

	private Playing playing;
	private BufferedImage[][] farmerAnimations;
	private ArrayList<Farmer> farmers = new ArrayList<>();

	public AllyManager(Playing playing) {
		this.playing = playing;
		loadSprites();
	}

	public void update() {
		for (Farmer f : farmers)
			if (f.isAlive())
				f.update();
	}

	public void render(Graphics g, int mapOffset) {
		renderFarmers(g, mapOffset);
	}

	public void addFarmer() {
		farmers.add(new Farmer(0, 126 * Game.SCALE, playing));
	}

	private void renderFarmers(Graphics g, int mapOffset) {
		for (Farmer f : farmers) {
			if (f.isAlive()) {
				f.renderHitbox(g, mapOffset);
				g.drawImage(farmerAnimations[f.getAction()][f.getAniIndex()],
						(int) (f.getHitbox().x - FARMEROFFSETX) - mapOffset + f.flipX(),
						(int) (f.getHitbox().y - FARMEROFFSETY), f.w * Game.SCALE * f.flipW(), f.h * Game.SCALE, null);
			}
		}
	}

	private void loadSprites() {
		BufferedImage img = SaveAndLoad.createSprite("/farmer.png");

		farmerAnimations = new BufferedImage[4][8];
		for (int i = 0; i < farmerAnimations.length; i++)
			for (int j = 0; j < farmerAnimations[i].length; j++)
				farmerAnimations[i][j] = img.getSubimage(j * FARMER_WIDTH, i * FARMER_WIDTH, FARMER_WIDTH,
						FARMER_HEIGHT);
	}

	public boolean checkAllyHit(Rectangle2D.Float attackBox, int damage) {
		for (Farmer f : farmers)
			if (f.isAlive() && !f.IsDeadAni())
				if (attackBox.intersects(f.getHitbox())) {
					f.hurt(damage);
					return true;
				}
		return false;
	}

	public boolean allyInRange(Rectangle2D.Float attackBox) {
		for (Farmer f : farmers)
			if (f.isAlive())
				if (attackBox.intersects(f.getHitbox()))
					return true;
		return false;
	}
}

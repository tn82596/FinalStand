package levels;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import utilities.SaveAndLoad;

public class Map {
	private Game game;
	private BufferedImage gate, bridge, portal;
	private BufferedImage[] portalAnimations;
	private int aniTick, aniIndex, aniSpeed = 15;

	public Map(Game game) {
		this.game = game;

		loadSprites();
	}

	public void update() {
		updateAniTick();
	}

	public void updateAniTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= portalAnimations.length)
				aniIndex = 0;
		}
	}

	public void render(Graphics g, int mapOffset) {
		g.drawImage(gate, -mapOffset, 56 * Game.SCALE, 320 * Game.SCALE, 124 * Game.SCALE, null);
		g.drawImage(bridge, 320 * Game.SCALE - mapOffset, 79 * Game.SCALE, 320 * Game.SCALE, 101 * Game.SCALE, null);
		g.drawImage(portalAnimations[aniIndex], 2 * 320 * Game.SCALE - mapOffset, 38 * Game.SCALE, 320 * Game.SCALE,
				142 * Game.SCALE, null);
	}

	private void loadSprites() {
		gate = SaveAndLoad.createSprite("/gate.png");
		bridge = SaveAndLoad.createSprite("/bridge.png");
		portal = SaveAndLoad.createSprite("/portal.png");

		portalAnimations = new BufferedImage[8];

		for (int i = 0; i < portalAnimations.length; i++)
			portalAnimations[i] = portal.getSubimage(i * 319, 0, 319, 142);
	}
}

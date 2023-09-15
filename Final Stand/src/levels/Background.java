package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilities.SaveAndLoad;

public class Background {
	private BufferedImage img;

	public Background() {
		loadSprites();
	}

	private void loadSprites() {
		img = SaveAndLoad.createSprite("/background.png");
	}

	public void render(Graphics g) {
		g.drawImage(img, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	}

}

package main;

import java.awt.Graphics;

import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int setFPS = 120;

	private Playing playing;
	private Menu menu;

	public final static int SCALE = 4;
	public final static int GAME_WIDTH = 320 * SCALE;
	public final static int GAME_HEIGHT = 180 * SCALE;

	public Game() {
		initializeClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();

		startGameLoop();
	}

	private void initializeClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		default:
			break;
		}
	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.render(g);
			break;
		case PLAYING:
			playing.render(g, gamePanel);
			break;
		default:
			break;
		}
	}

	public void run() {

		double timePerFrame = 1000000000.0 / setFPS;
		long lastFrame = System.nanoTime();

		int frames = 0;
		long lastCheck = System.currentTimeMillis();

		// game loop
		while (true) {
			long now = System.nanoTime();
			if (now - lastFrame >= timePerFrame) {
				gamePanel.repaint();
				lastFrame = now;
				frames++;
			}

			// fps counter
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				// System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}
}

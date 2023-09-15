package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.Keyboard;
import inputs.Mouse;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
	private Game game;

	public GamePanel(Game game) {
		this.game = game;

		setPanelSize();

		addKeyListener(new Keyboard(this));
		addMouseListener(new Mouse(this));
	}

	// set panel size
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	// paint screen
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		game.update();
		game.render(g);

	}

	public Game getGame() {
		return game;
	}
}

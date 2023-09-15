package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gamestates.Gamestate;
import main.GamePanel;

public class Mouse implements MouseListener {
	private GamePanel gamePanel;

	public Mouse(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void mouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseClicked(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseClicked(e);
			break;
		default:
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		default:
			break;
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

}

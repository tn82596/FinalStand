package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import entities.AllyManager;
import entities.Assassin;
import entities.EnemyManager;
import entities.Player;
import entities.Samurai;
import levels.Background;
import levels.Map;
import main.Game;
import main.GamePanel;
import objects.ObjectManager;

import static utilities.Constants.playerTypes.*;

public class Playing extends State {
	private Map map;
	private Background background;
	private Player player;
	private EnemyManager enemyManager;
	private AllyManager allyManager;
	private ObjectManager objectManager;

	private int playerType = ASSASSIN;
	
	private int mapOffset = 0;
	private int leftBorder = (int) (0.4 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.4 * Game.GAME_WIDTH);
	private int mapWidth = 960 * Game.SCALE;
	private int maxMapOffset = mapWidth - Game.GAME_WIDTH;

	public Playing(Game game) {
		super(game);
		InitializeClasses();
	}

	public void InitializeClasses() {
		background = new Background();
		map = new Map(game);
		enemyManager = new EnemyManager(this);
		allyManager = new AllyManager(this);
		objectManager = new ObjectManager(this);
		initializePlayer();
	}

	public void update() {
		map.update();
		objectManager.update(player);
		player.update();
		enemyManager.update(player);
		allyManager.update();
		checkCloseToBorder();
	}

	public void render(Graphics g, GamePanel gamePanel) {
		background.render(g);
		map.render(g, mapOffset);
		player.render(g, mapOffset, gamePanel);
		enemyManager.render(g, mapOffset);
		allyManager.render(g, mapOffset);
		objectManager.render(g, mapOffset);
	}
	
	private void initializePlayer() {
		switch (playerType) {
		case SAMURAI:
			player = new Samurai(15, 117 * Game.SCALE, this);
			break;
		case ASSASSIN:
			player = new Assassin(15, 120 * Game.SCALE, this);
			break;
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - mapOffset;

		if (diff > rightBorder)
			mapOffset += diff - rightBorder;
		else if (diff < leftBorder)
			mapOffset += diff - leftBorder;

		if (mapOffset > maxMapOffset)
			mapOffset = maxMapOffset;
		else if (mapOffset < 0)
			mapOffset = 0;
	}

	public int checkEnemyHit(Rectangle2D.Float attackBox, int damage, boolean knockback, int attackType, int statusEffect) {
		return enemyManager.checkEnemyHit(attackBox, damage, knockback, attackType, statusEffect);
	}

	public boolean checkEnemyHit(Rectangle2D.Float attackBox, int damage) {
		return enemyManager.checkEnemyHit(attackBox, damage);
	}

	public boolean enemyInRange(Rectangle2D.Float attackBox) {
		return enemyManager.enemyInRange(attackBox);
	}

	public boolean allyInRange(Rectangle2D.Float attackBox) {
		return allyManager.allyInRange(attackBox);
	}

	public boolean checkAllyHit(Rectangle2D.Float attackBox, int damage) {
		return allyManager.checkAllyHit(attackBox, damage);
	}

	public void addProjectiles(int projectileType, int damage) {
		objectManager.addProjectile(player, projectileType, damage);
	}

	public void spawnFarmer() {
		allyManager.addFarmer();
	}

	// attack & shoot
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			player.setMeleeAttacking(true);
			break;
		case MouseEvent.BUTTON3:
			player.setRangedAttacking(true);
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			player.setMeleeAttacking(true);
			break;
		case MouseEvent.BUTTON3:
			player.setRangedAttacking(true);
			break;
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {

	}

	// WASD & Abilities
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_E:
			player.setAbility1(true);
			break;
		case KeyEvent.VK_SHIFT:
			player.setAbility2(true);
			break;
		case KeyEvent.VK_Q:
			player.Ultimate(true);
			break;
		case KeyEvent.VK_1:
			player.spawnFarmer();
			break;
		case KeyEvent.VK_2:
			enemyManager.addSkeleton();
			break;
		}

	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		}

	}

	public Player getPlayer() {
		return player;
	}

}
package entities;

import static utilities.HelpMethods.CanMoveHere;
import static utilities.Constants.AttackTypes.*;
import static utilities.Constants.AllyConstants.FARMER;
import static utilities.Constants.AllyConstants.GetCost;
import static utilities.Constants.Projectiles.*;
import static utilities.Constants.StatusEffects.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import gamestates.Playing;
import main.Game;
import main.GamePanel;
import utilities.SaveAndLoad;



public class Player extends Entities{

	//stats
	protected float knockback;
	protected int healSpeed;
	protected int range;
	protected int meleeDamage;
	protected int rangedDamage;
	
	//CDs
	protected int meleeTimer;
	protected int meleeCooldown;
	protected int rangedTimer;
	protected int rangedCooldown;
	protected int ultCharge = 0;
	protected int ultChargeReq;
	
	//abilities
	protected int ability1;
	protected int ability1UI;
	protected int ability2;
	protected int ability2UI;
	
	protected int meleeIndex1;
	protected int meleeIndex2;
	protected int rangedIndex;
	protected int projectile;
	
	//animation
	protected BufferedImage[][] animations;
	protected BufferedImage[] abilities;
	protected BufferedImage ultimate;
	
	protected int hitboxOffsetX;
	protected int hitboxOffsetY;
	
	//attacking and moving
	protected int flipX = 0, flipW = 1;
	protected boolean left, right, moving;
	protected boolean meleeAttacking, rangedAttacking, ulting;
	protected boolean lockedInAnimation;
	protected boolean attackChecked;
	
	//UI
	protected BufferedImage statusBarImg;
	protected int statusBarWidth = 192 * Game.SCALE / 2;
	protected int statusBarHeight = 58 * Game.SCALE / 2;
	protected int statusBarX = 10 * Game.SCALE / 2;
	protected int statusBarY = 10 * Game.SCALE / 2;

	protected int healthBarWidth = 150 * Game.SCALE / 2;
	protected int healthBarHeight = 4 * Game.SCALE / 2;
	protected int healthBarX = 34 * Game.SCALE / 2;
	protected int healthBarY = 14 * Game.SCALE / 2;
	protected int healthWidth = healthBarWidth;
	protected int healTick;
	protected int outOfCombatTick, outOfCombatSpeed = 240;

	protected int energyBarWidth = 104 * Game.SCALE / 2;
	protected int energyBarHeight = 2 * Game.SCALE / 2;
	protected int energyBarX = 44 * Game.SCALE / 2;
	protected int energyBarY = 34 * Game.SCALE / 2;
	protected int energyWidth = 0;
	protected int maxEnergy = 100, currentEnergy = 0;
	protected int energyGrowTick, energyGrowSpeed = 120 / 8;

	protected BufferedImage farmerUI;
	protected int farmerCooldown = 120 * 1;
	protected int farmerTimer = farmerCooldown;
	protected int farmerUIWidth = 37 * Game.SCALE / 2;
	protected int farmerUIConstHeight = 37 * Game.SCALE / 2;
	protected int farmerUIHeight;
	
	protected int ability1UIHeight;
	protected int ability2UIHeight;
	protected int abilityUIConstHeight = 37 * Game.SCALE / 2;
	protected int ultUIConstHeight = 41 * Game.SCALE / 2;
	protected int ultUIHeight = ultUIConstHeight;
	
	//file names
	protected String playerFileName;
	protected String abilitiesUIFileName;
	protected String ultimateUIFileName;

	
	
	public Player(float x, float y, int w, int h, Playing playing) {
		super(x, y, w, h, playing);
	}
	
	// changes array index for animation
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			attackChecked = false;
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= getAniAmount(action)) {
				aniIndex = 0;
				aniSpeed = 15;
				resetAbilityBooleans();
			}
		}
	}
	
	protected void drawPlayer(Graphics g, int mapOffset) {
		g.drawImage(animations[action][aniIndex], (int) (hitbox.x - hitboxOffsetX) - mapOffset + flipX,
				(int) (hitbox.y - hitboxOffsetY), w * Game.SCALE * flipW, h * Game.SCALE, null);
	}
	
	public void checkPlayerHit(Rectangle2D.Float attackBox, int damage) {
		if (attackBox.intersects(hitbox)) {
			changeHealth(-damage);
			outOfCombatTick = 0;
		}
	}
	
	protected void checkMeleeAttack() {
		if (attackChecked || (aniIndex != meleeIndex1 && aniIndex != meleeIndex2))
			return;
		attackChecked = true;
		ultCharge += playing.checkEnemyHit(attackBox, meleeDamage, true, SINGLE, NONE);
	}

	protected void checkRangedAttack() {
		if (attackChecked || aniIndex != rangedIndex)
			return;
		attackChecked = true;
		playing.addProjectiles(projectile, rangedDamage);
	}
	
	//moving
	protected void updatePosition() {
		moving = false;

		if (lockedInAnimation)
			return;

		if ((!left && !right) || (right && left))
			return;

		float xSpeed = 0, ySpeed = 0;

		if (right && !left) {
			xSpeed = walkSpeed;
		}

		else if (left && !right) {
			xSpeed = -walkSpeed;
		}

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height)) {
			hitbox.x += xSpeed;
			hitbox.y += ySpeed;
			moving = true;
		}
	}

	protected void setDirection(int mapOffset, GamePanel gamePanel) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, gamePanel);
		if (!lockedInAnimation) {
			if (p.getX() - (hitbox.x - mapOffset) >= 0) {
				flipX = 0;
				flipW = 1;
			} else {
				flipX = w * Game.SCALE;
				flipW = -1;
			}
		}
	}

	protected void updateUI() {
		updateHealthBar();
		updateEnergyBar();
		updateAllies();
	}
	
	protected void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

		g.setColor(Color.red);
		g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);

		g.setColor(Color.yellow);
		g.fillRect(energyBarX + statusBarX, energyBarY + statusBarY, energyWidth, energyBarHeight);

		g.drawImage(farmerUI, 5 * Game.SCALE, 156 * Game.SCALE, 37 * Game.SCALE / 2, 37 * Game.SCALE / 2, null);
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(5 * Game.SCALE, 156 * Game.SCALE, farmerUIWidth, farmerUIHeight);
	
		g.drawImage(abilities[ability2UI], (320 - 37 / 2 - 5) * Game.SCALE, 156 * Game.SCALE, abilityUIConstHeight, abilityUIConstHeight, null);
		g.fillRect((320 - 37 / 2 - 5) * Game.SCALE, 156 * Game.SCALE, abilityUIConstHeight, ability2UIHeight);
		
		g.drawImage(abilities[ability1UI], (320 - 37 - 10) * Game.SCALE, 156 * Game.SCALE, abilityUIConstHeight, abilityUIConstHeight, null);
		g.fillRect((320 - 37 - 10) * Game.SCALE, 156 * Game.SCALE, abilityUIConstHeight, ability1UIHeight);
		
		g.drawImage(ultimate, (320 / 2 - 41 / 4) * Game.SCALE, 155 * Game.SCALE, ultUIConstHeight, ultUIConstHeight, null);
		g.fillRect((320 / 2 - 41 / 4) * Game.SCALE, 155 * Game.SCALE, ultUIConstHeight, ultUIHeight);
	}
	
	protected void loadSprites() {
		statusBarImg = SaveAndLoad.createSprite("/health_power_bar.png");
		farmerUI = SaveAndLoad.createSprite("/farmerUI.png");
		
		BufferedImage img = SaveAndLoad.createSprite(playerFileName);

		animations = loadCharacterSprites();
		for (int i = 0; i < animations.length; i++)
			for (int j = 0; j < animations[i].length; j++)
				animations[i][j] = img.getSubimage(j * w, i * h, w, h);
		
		BufferedImage img2 = SaveAndLoad.createSprite(abilitiesUIFileName);
		
		abilities = new BufferedImage[4];
		for (int i = 0; i < abilities.length; i++)
			abilities[i] = img2.getSubimage(37 * i, 0, 37, 37);
		
		ultimate = SaveAndLoad.createSprite(ultimateUIFileName);
	}
	
	protected void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);

		outOfCombatTick++;
		if (outOfCombatTick >= outOfCombatSpeed) {
			healTick++;
			if (healTick >= healSpeed) {
				healTick = 0;
				changeHealth(1);
			}
		}
	}
	
	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
			// gameOver();
		} else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	protected void updateEnergyBar() {
		energyWidth = (int) ((currentEnergy / (float) maxEnergy) * energyBarWidth);

		energyGrowTick++;
		if (energyGrowTick >= energyGrowSpeed) {
			energyGrowTick = 0;
			changeEnergy(1);
		}
	}

	private void changeEnergy(int value) {
		currentEnergy += value;
		if (currentEnergy >= maxEnergy)
			currentEnergy = maxEnergy;
		else if (currentEnergy <= 0)
			currentEnergy = 0;
	}
	
	protected void updateAllies() {
		farmerUIHeight = (int) ((farmerTimer / (float) farmerCooldown) * farmerUIConstHeight);

		farmerTimer--;
		if (farmerTimer <= 0)
			farmerTimer = 0;
	}
	
	public void spawnFarmer() {
		int cost = GetCost(FARMER);
		if (currentEnergy >= cost && farmerTimer == 0) {
			currentEnergy -= cost;
			farmerTimer = farmerCooldown;
			playing.spawnFarmer();
		}
	}

	public void setMeleeAttacking(boolean meleeAttacking) {
			this.meleeAttacking = meleeAttacking;
	}

	public void setRangedAttacking(boolean rangedAttacking) {
		this.rangedAttacking = rangedAttacking;
	}
	
	public void Ultimate(boolean x) {
		if (ultCharge >= ultChargeReq)
			ulting = x;
	}
	public void addProjectileUltCharge(int damage) {
		ultCharge += damage;
	}

	public int getRange() {
		return range;
	}

	public int getFlipW() {
		return flipW;
	}
	
	public int getFlipX() {
		return flipX;
	}

	public float getKnockback() {
		return knockback;
	}
	
	public int getAction() {
		return action;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	protected void resetAbilityBooleans() {}

	protected int getAniAmount(int action) {
		return 0;
	}
	
	protected BufferedImage[][] loadCharacterSprites() {
		return new BufferedImage[0][0];
	}

	public boolean inStealth() {
		return false;
	}
	
	public void render(Graphics g, int mapOffset, GamePanel gamePanel) {}
	
	public void update() {}
	
	public void setAbility1(boolean b) {}
	
	public void setAbility2(boolean b) {}

}

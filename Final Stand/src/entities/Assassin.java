package entities;

import static utilities.Constants.AssassinConstants.*;
import static utilities.Constants.AttackTypes.*;
import static utilities.Constants.Projectiles.*;
import static utilities.Constants.AssassinAbilityConstants.*;
import static utilities.Constants.StatusEffects.*;
import static utilities.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import main.GamePanel;
import utilities.SaveAndLoad;

public class Assassin extends Player {
	
	private int empoweredDamage = 20;
	private int spinDamage = 20;
	private int ultDamage = 15;
	
	private float blinkDistance = 75.0f * Game.SCALE;
	private int maxBlinks = 2;
	private int blinkCount = maxBlinks;
	
	private int blinkTimer = 0;
	private int blinkCooldown = 720;
	private int spinTimer = 0;
	private int spinCooldown = 480;
	private int smokescreenTimer = 0;
	private int smokeScreenCooldown = 840;
	private int ghostDashTimer = 0;
	private int ghostDashCooldown = 1080;
	private int stealthDuration = 360;
	private int stealthTimer = 0;
	
	private Rectangle2D.Float empoweredAttackBox;
	private Rectangle2D.Float spinBox;
	private Rectangle2D.Float ultBox;
	
	private boolean blink, spin, smokescreen, ghostDash;
	
	public Assassin(float x, float y, Playing playing) {
		super(x, y, WIDTH, HEIGHT, playing);
		
		action = IDLE;
		maxHealth = 100;
		currentHealth = maxHealth;
		walkSpeed = 0.80f * Game.SCALE;
		knockback = 0.3f * Game.SCALE;
		range = 150 * Game.SCALE;
		
		meleeDamage = 20;
		meleeTimer = 0;
		meleeCooldown = 150;
		meleeIndex1 = MELEE_INDEX_1;
		meleeIndex2 = MELEE_INDEX_2;
		
		rangedDamage = 30;
		rangedTimer = 0;
		rangedCooldown = 120;
		rangedIndex = RANGED_INDEX;
		projectile = DAGGER;
		
		ultChargeReq = 800;
		
		ability1 = SPIN;
		ability2 = BLINK;
		ability1UI = SPIN_UI;
		ability2UI = BLINK_UI;
		
		hitboxOffsetX = HITBOX_OFFSET_X;
		hitboxOffsetY = HITBOX_OFFSET_Y;
		
		playerFileName = "/assassin.png";
		abilitiesUIFileName = "/assassinabilitiesUI.png";
		ultimateUIFileName = "/assassinultUI.png";
		
		loadSprites();
		initializeHitboxes(x, y);
	}
	
	private void initializeHitboxes(float x, float y) {
		initializeHitbox(16 * Game.SCALE, 30 * Game.SCALE);
		attackBox = new Rectangle2D.Float(x, y, 26 * Game.SCALE, 34 * Game.SCALE);
		ultBox = new Rectangle2D.Float(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
	}
	
	@Override
	public void update() {
		updateUI();
		updateAbilityUI();
		updateCooldowns();
		
		updatePosition();
		updateHitboxes();
		switch(action) {
		case DAGGER_ATTACK:
			checkMeleeAttack();
			break;
		case EMPOWERED_ATTACK:
			checkEmpoweredAttack();
			break;
		case DAGGER_THROW:
			checkRangedAttack();
			break;
		case BLINK:
			checkBlink();
			break;
		case SPIN:
			checkSpin();
			break;
		case BLOSSOM_DANCE:
			checkUlt();
			break;
		}

		updateAnimationTick();
		setAnimation();
	}
	
	private void updateCooldowns() {
		meleeTimer--;
		rangedTimer--;
		blinkTimer--;
		if (blinkTimer <= 0) {
			blinkTimer = 0;
			if (blinkCount < maxBlinks)
				blinkCount++;
			if (blinkCount < maxBlinks)
				blinkTimer = blinkCooldown;
		}
		spinTimer--;
		if (spinTimer <= 0) 
			spinTimer = 0;
		if (inStealth())
			stealthTimer--;
		if (ultCharge >= ultChargeReq)
			ultCharge = ultChargeReq;
	}
	
	private void checkEmpoweredAttack() {
		if (attackChecked || aniIndex != 5)
			return;
		attackChecked = true;
		ultCharge += playing.checkEnemyHit(attackBox, empoweredDamage, true, AOE, BLEED);
	}
	
	private void checkBlink() {
		if (attackChecked || aniIndex != 2)
			return;
		attackChecked = true;
		if (flipW == 1) {
			if (CanMoveHere(hitbox.x + blinkDistance, hitbox.y, hitbox.width, hitbox.height))
				hitbox.x += blinkDistance;
			else
				hitbox.x = 960 * Game.SCALE - hitbox.width;
		}
		else if (flipW == -1) {
			if (CanMoveHere(hitbox.x - blinkDistance, hitbox.y, hitbox.width, hitbox.height))
				hitbox.x -= blinkDistance;
			else
				hitbox.x = 0;
		}
	}
	
	private void checkSpin() {
		if (attackChecked || aniIndex != 2)
			return;
		attackChecked = true;
		ultCharge += playing.checkEnemyHit(attackBox, spinDamage, true, AOE, NONE);
	}
	
	private void checkUlt() {
		if (attackChecked || (aniIndex < 5 || aniIndex > 8))
			return;
		attackChecked = true;
		playing.checkEnemyHit(ultBox, ultDamage, true, AOE, BLEED);
	}
	
	private void updateHitboxes() {
		attackBox.y = hitbox.y - 4 * Game.SCALE;
		ultBox.x = hitbox.x - hitboxOffsetX;
		ultBox.y = hitbox.y - hitboxOffsetY;
		
		if (flipW == 1) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 4);
		} else if (flipW == -1) {
			attackBox.x = hitbox.x - attackBox.width - (int) (Game.SCALE * 4);
		}
	}
	
	@Override
	public void render(Graphics g, int mapOffset, GamePanel gamePanel) {
		drawPlayer(g, mapOffset);
		drawUI(g);
		drawBlinkCount(g);
		renderHitboxes(g, mapOffset);
		setDirection(mapOffset, gamePanel);
	}
	
	private void renderHitboxes(Graphics g, int mapOffset) {
		g.setColor(Color.red);
		renderHitbox(g, mapOffset);
		g.drawRect((int) attackBox.x - mapOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	private void drawBlinkCount(Graphics g) {
		g.setColor(Color.black);
		if (ability1 == BLINK)
			g.drawString(Integer.toString(blinkCount), (320 - 37 - 10 + 2) * Game.SCALE, (156 + 4) * Game.SCALE);
		else if (ability2 == BLINK)
			g.drawString(Integer.toString(blinkCount), (320 - 37 / 2 - 5 + 2) * Game.SCALE, (156 + 4) * Game.SCALE);
	}
	
	@Override
	protected void resetAbilityBooleans() {
		meleeAttacking = false;
		rangedAttacking = false;
		lockedInAnimation = false;
		blink = false;
		spin = false;
		ulting = false;
		if (action == EMPOWERED_ATTACK || action == DAGGER_THROW)
			stealthTimer = 0;
	}
	
	private void setAnimation() {
		int startAni = action;
		
		if (!lockedInAnimation) {
			if (moving) {
				if ((flipW == 1 && right) || (flipW == -1 && left))
					if (inStealth())
						action = STEALTH_RUN;
					else
						action = RUN;
				else if ((flipW == 1 && left) || (flipW == -1 && right))
					if (inStealth())
						action = STEALTH_BACK_RUN;
					else
						action = BACK_RUN;
			} 
			else {
				if (inStealth())
					action = STEALTH_IDLE;
				else
					action = IDLE;
			}
			
			if (meleeAttacking && meleeTimer <= 0) {
				if (inStealth()) {
					action = EMPOWERED_ATTACK;
				}
				else
					action = DAGGER_ATTACK;
				meleeTimer = meleeCooldown;
				lockedInAnimation = true;
			}
			
			if (rangedAttacking && rangedTimer <= 0) {
				action = DAGGER_THROW;
				rangedTimer = rangedCooldown;
				lockedInAnimation = true;
			}
			
			if (blink) {
				action = BLINK;
				aniSpeed = 8;
				if (blinkCount == maxBlinks)
					blinkTimer = blinkCooldown;
				blinkCount--;
				lockedInAnimation = true;
				stealthTimer = stealthDuration;
			}
			
			if (spin) {
				action = SPIN;
				aniSpeed = 6;
				spinTimer = spinCooldown;
				lockedInAnimation = true;
				stealthTimer = stealthDuration;
			}
			
			if (ulting) {
				action = BLOSSOM_DANCE;
				ultCharge = 0;
				lockedInAnimation = true;
			}
		}
		
		if (startAni != action) {
			aniIndex = 0;
			aniTick = 0;
		}
	}
	
	private void updateAbilityUI() {
		switch (ability1) {
		case BLINK:
			ability1UIHeight = (int) ((blinkTimer / (float) blinkCooldown) * abilityUIConstHeight);
			break;
		case SPIN:
			ability1UIHeight = (int) ((spinTimer / (float) spinCooldown) * abilityUIConstHeight);
			break;
		}
		switch (ability2) {
			case BLINK:
				ability2UIHeight = (int) ((blinkTimer / (float) blinkCooldown) * abilityUIConstHeight);
				break;
			case SPIN:
				ability2UIHeight = (int) ((spinTimer / (float) spinCooldown) * abilityUIConstHeight);
				break;
		}
		ultUIHeight = ultUIConstHeight - (int) ((ultCharge / (float) ultChargeReq) * ultUIConstHeight);
	}
	
	@Override
	public void setAbility1(boolean b) {
		switch(ability1) {
		case BLINK:
			if (blinkCount > 0)
				blink = b;
			break;
		case SPIN:
			if (spinTimer == 0)
				spin = b;
			break;
		}
	}
	
	@Override
	public void setAbility2(boolean b) {
		switch(ability2) {
		case BLINK:
			if (blinkCount > 0)
				blink = b;
			break;
		case SPIN:
			if (spinTimer == 0)
				spin = b;
			break;
		}
	}
	
	@Override
	protected int getAniAmount(int action) {
		return getSpriteAmount(action);
	}

	@Override
	protected BufferedImage[][] loadCharacterSprites() {
		return new BufferedImage[ASSASSIN_ROWS][ASSASSIN_COLUMNS];
	}
	
	@Override
	public boolean inStealth() {
		return (stealthTimer > 0);
	}
}

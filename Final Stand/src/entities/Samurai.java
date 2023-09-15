package entities;

import static utilities.Constants.SamuraiConstants.*;
import static utilities.Constants.AttackTypes.*;
import static utilities.Constants.Projectiles.*;
import static utilities.Constants.SamuraiAbilityConstants.*;
import static utilities.Constants.StatusEffects.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import main.GamePanel;
import utilities.SaveAndLoad;

public class Samurai extends Player {

	// player stats and CDs
	private int cycloneDamage = 30;
	private int mysticalArrowDamage = 50;
	private int bladeBeamDamage = 100;
	private int ultDamage = 100;

	private int cycloneTimer = 0;
	private int cycloneCooldown = 600;
	private int bladeBeamTimer = 0;
	private int bladeBeamCooldown = 300;
	private int mysticalArrowTimer = 0;
	private int mysticalArrowCooldown = 600;
	private int iceActivateTimer = 0;
	private int iceActivateCooldown = 1920;
	private int iceArrowsDuration = 1080;
	private int iceArrowsTimer = 0;

	// player hitboxes
	private Rectangle2D.Float cycloneBox;
	private Rectangle2D.Float mysticalArrowBox;
	private Rectangle2D.Float ultBox;

	private boolean cyclone, mysticalArrow, bladeBeam, iceActivate;

	public Samurai(float x, float y, Playing playing) {
		super(x, y, WIDTH, HEIGHT, playing);
	
		action = IDLE;
		maxHealth = 100;
		currentHealth = maxHealth;
		walkSpeed = 0.75f * Game.SCALE;
		knockback = 0.3f * Game.SCALE;
		range = 150 * Game.SCALE;
		
		meleeDamage = 20;
		meleeTimer = 0;
		meleeCooldown = 180;
		meleeIndex1 = MELEE_INDEX_1;
		meleeIndex2 = MELEE_INDEX_2;
		
		rangedDamage = 30;
		rangedTimer = 0;
		rangedCooldown = 180;
		rangedIndex = RANGED_INDEX;
		projectile = ARROW;
		
		ultChargeReq = 1000;

		ability1 = CYCLONE;
		ability2 = MYSTICAL_ARROW;
		ability1UI = CYCLONE_UI;
		ability2UI = MYSTICAL_ARROW_UI;

		hitboxOffsetX = HITBOX_OFFSET_X;
		hitboxOffsetY = HITBOX_OFFSET_Y;
		
		playerFileName = "/samurai.png";
		abilitiesUIFileName = "/samuraiabilitiesUI.png";
		ultimateUIFileName = "/samuraiultUI.png";
				
		loadSprites();
		initializeHitboxes(x, y);

	}

	private void initializeHitboxes(float x, float y) {
		initializeHitbox(24 * Game.SCALE, 33 * Game.SCALE);
		attackBox = new Rectangle2D.Float(x, y, 24 * Game.SCALE, 20 * Game.SCALE);
		cycloneBox = new Rectangle2D.Float(x - 39 * Game.SCALE, y, 102 * Game.SCALE, 20 * Game.SCALE);
		mysticalArrowBox = new Rectangle2D.Float(x, y, x + 135 * Game.SCALE, 20 * Game.SCALE);
		ultBox = new Rectangle2D.Float(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
	}

	// animate and move player
	@Override
	public void update() {
		updateUI();
		updateAbilityUI();
		updateCooldowns();

		updatePosition();
		updateHitboxes();
		
		switch(action) {
		case SWORD_ATTACK:
			checkMeleeAttack();
			break;
		case BOW_ATTACK:
			checkRangedAttack();
			break;
		case ICE_ARROW_ATTACK:
			checkIceAttack();
			break;
		case CYCLONE:
			checkCyclone();
			break;
		case MYSTICAL_ARROW:
			checkMysticalArrow();
			break;
		case BLADE_BEAM:
			checkBladeBeam();
			break;
		case ICE_ARROWS_ACTIVATE:
			checkIceActivate();
			break;
		case WATER_BREATHING_SLASH:
			checkUlt();
			break;
		}
		
		updateAnimationTick();
		setAnimation();
	}

	private void updateCooldowns() {
		meleeTimer--;
		rangedTimer--;
		cycloneTimer--;
		if (cycloneTimer <= 0)
			cycloneTimer = 0;
		bladeBeamTimer--;
		if (bladeBeamTimer <= 0)
			bladeBeamTimer = 0;
		mysticalArrowTimer--;
		if (mysticalArrowTimer <= 0)
			mysticalArrowTimer = 0;
		iceActivateTimer--;
		if (iceActivateTimer <= 0)
			iceActivateTimer = 0;
		if (ultCharge >= ultChargeReq)
			ultCharge = ultChargeReq;
		if (iceArrows())
			iceArrowsTimer--;
	}

	
	private void checkIceAttack() {
		if (attackChecked || aniIndex != 4)
			return;
		attackChecked = true;
		playing.addProjectiles(ICE_ARROW, rangedDamage);
	}

	private void checkCyclone() {
		if (attackChecked || (aniIndex != 0 && aniIndex != 2 && aniIndex != 4 && aniIndex != 6))
			return;
		attackChecked = true;
		ultCharge += playing.checkEnemyHit(cycloneBox, cycloneDamage, true, AOE, NONE);
	}

	private void checkMysticalArrow() {
		if (attackChecked || (aniIndex != 4))
			return;
		attackChecked = true;
		ultCharge += playing.checkEnemyHit(mysticalArrowBox, mysticalArrowDamage, true, AOE, NONE);
	}
	
	private void checkBladeBeam() {
		if (attackChecked || (aniIndex != 4))
			return;
		attackChecked = true;
		playing.addProjectiles(BLADE_BEAM_PROJECTILE, bladeBeamDamage);
	}
	
	private void checkIceActivate() {
		if (attackChecked || aniIndex != 4)
			return;
		attackChecked = true;
		iceArrowsTimer = iceArrowsDuration;
	}
	
	private void checkUlt() {
		if (attackChecked || aniIndex != 9)
			return;
		attackChecked = true;
		playing.checkEnemyHit(ultBox, ultDamage, true, AOE, NONE);
	}
	
	private boolean iceArrows() {
		return (iceArrowsTimer > 0);
	}
		
	private void updateHitboxes() {
		cycloneBox.x = hitbox.x - Game.SCALE * 39;
		cycloneBox.y = hitbox.y + Game.SCALE * 10;
		attackBox.y = hitbox.y + Game.SCALE * 10;
		ultBox.x = hitbox.x - hitboxOffsetX;
		ultBox.y = hitbox.y - hitboxOffsetY;

		if (flipW == 1) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
			mysticalArrowBox.x = hitbox.x;
		} else if (flipW == -1) {
			attackBox.x = hitbox.x - attackBox.width - (int) (Game.SCALE * 10);
			mysticalArrowBox.x = hitbox.x - mysticalArrowBox.width + hitbox.width;
		}
	}

	// draw player
	@Override
	public void render(Graphics g, int mapOffset, GamePanel gamePanel) {
		renderUltFlash(g);
		drawPlayer(g, mapOffset);
		drawUI(g);
		renderHitboxes(g, mapOffset);
		setDirection(mapOffset, gamePanel);
	}

	private void renderHitboxes(Graphics g, int mapOffset) {
		g.setColor(Color.red);
		renderHitbox(g, mapOffset);
		g.drawRect((int) attackBox.x - mapOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
		g.drawRect((int) cycloneBox.x - mapOffset, (int) cycloneBox.y, (int) cycloneBox.width, (int) cycloneBox.height);
		g.drawRect((int) mysticalArrowBox.x - mapOffset, (int) mysticalArrowBox.y, (int) mysticalArrowBox.width, (int) mysticalArrowBox.height);
	}

	private void renderUltFlash(Graphics g) {
		if (aniIndex == 9 && action == WATER_BREATHING_SLASH) {
			g.setColor(Color.blue);
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
	}
	
	@Override
	protected void resetAbilityBooleans() {
		meleeAttacking = false;
		rangedAttacking = false;
		lockedInAnimation = false;
		cyclone = false;
		mysticalArrow = false;
		bladeBeam = false;
		iceActivate = false;
		ulting = false;
	}

	// set player's animation
	private void setAnimation() {
		int startAni = action;

		if (!lockedInAnimation) {
			if (moving) {
				if ((flipW == 1 && right) || (flipW == -1 && left))
					action = RUN;
				else if ((flipW == 1 && left) || (flipW == -1 && right))
					action = BACK_RUN;
			} else
				action = IDLE;

			if (meleeAttacking && meleeTimer <= 0) {
				action = SWORD_ATTACK;
				meleeTimer = meleeCooldown;
				lockedInAnimation = true;
			}

			if (rangedAttacking && rangedTimer <= 0) {
				if (iceArrows())
					action = ICE_ARROW_ATTACK;
				else
					action = BOW_ATTACK;
				rangedTimer = rangedCooldown;
				lockedInAnimation = true;
			}

			if (cyclone) {
				action = CYCLONE;
				aniSpeed = 8;
				cycloneTimer = cycloneCooldown;
				lockedInAnimation = true;
			}
			
			if (mysticalArrow) {
				action = MYSTICAL_ARROW;
				mysticalArrowTimer = mysticalArrowCooldown;
				lockedInAnimation = true;
			}
			
			if (bladeBeam) {
				action = BLADE_BEAM;
				bladeBeamTimer = bladeBeamCooldown;
				lockedInAnimation = true;	
			}
			
			if (iceActivate) {
				action = ICE_ARROWS_ACTIVATE;
				iceActivateTimer = iceActivateCooldown;
				lockedInAnimation = true;
			}
			
			if (ulting) {
				action = WATER_BREATHING_SLASH;
				aniSpeed = 18;
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
		case CYCLONE:
			ability1UIHeight = (int) ((cycloneTimer / (float) cycloneCooldown) * abilityUIConstHeight);
			break;
		case BLADE_BEAM:
			ability1UIHeight = (int) ((bladeBeamTimer / (float) bladeBeamCooldown) * abilityUIConstHeight);
			break;
		case MYSTICAL_ARROW:
			ability1UIHeight = (int) ((mysticalArrowTimer / (float) mysticalArrowCooldown) * abilityUIConstHeight);
			break;
		case ICE_ARROWS_ACTIVATE:
			ability1UIHeight = (int) ((iceActivateTimer / (float) iceActivateCooldown) * abilityUIConstHeight);
			break;
		}
		
		switch (ability2) {
		case CYCLONE:
			ability2UIHeight = (int) ((cycloneTimer / (float) cycloneCooldown) * abilityUIConstHeight);;
			break;
		case BLADE_BEAM:
			ability2UIHeight = (int) ((bladeBeamTimer / (float) bladeBeamCooldown) * abilityUIConstHeight);
			break;
		case MYSTICAL_ARROW:
			ability2UIHeight = (int) ((mysticalArrowTimer / (float) mysticalArrowCooldown) * abilityUIConstHeight);
			break;
		case ICE_ARROWS_ACTIVATE:
			ability2UIHeight = (int) ((iceActivateTimer / (float) iceActivateCooldown) * abilityUIConstHeight);
			break;
		}
		
		ultUIHeight = ultUIConstHeight - (int) ((ultCharge / (float) ultChargeReq) * ultUIConstHeight);
	}

	@Override
	public void setAbility1(boolean b) {
		switch (ability1) {
		case CYCLONE:
			if (cycloneTimer == 0)
				cyclone = b;
			break;
		case MYSTICAL_ARROW:
			if (mysticalArrowTimer == 0)
				mysticalArrow = b;
			break;
		case BLADE_BEAM:
			if (bladeBeamTimer == 0)
				bladeBeam = b;
			break;
		case ICE_ARROWS_ACTIVATE:
			if (iceActivateTimer == 0)
				iceActivate = b;
			break;
		}
	}
	
	@Override
	public void setAbility2(boolean b) {
		switch (ability2) {
		case CYCLONE:
			if (cycloneTimer == 0)
				cyclone = b;
			break;
		case MYSTICAL_ARROW:
			if (mysticalArrowTimer == 0)
				mysticalArrow = b;
			break;
		case BLADE_BEAM:
			if (bladeBeamTimer == 0)
				bladeBeam = b;
			break;
		case ICE_ARROWS_ACTIVATE:
			if (iceActivateTimer == 0)
				iceActivate = b;
			break;
		}
	}
	
	@Override
	protected int getAniAmount(int action) {
		return getSpriteAmount(action);
	}
	
	@Override
	protected BufferedImage[][] loadCharacterSprites() {
		return new BufferedImage[SAMURAI_ROWS][SAMURAI_COLUMNS];
	}
	

}

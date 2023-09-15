package utilities;

import main.Game;

public class Constants {

	public static class playerTypes {
		public static final int SAMURAI = 0;
		public static final int ASSASSIN = 1;
	}
	
	public static class Projectiles {
		public static final int PROJECTILE_WIDTH = 128;
		public static final int PROJECTILE_HEIGHT = 64;
		public static final float SPEED = 2.0f * Game.SCALE;

		public static final int ARROW = 0;
		public static final int ICE_ARROW = 1;
		public static final int BLADE_BEAM_PROJECTILE = 2;
		public static final int DAGGER = 3;
		
		public static final int ARROW_OFFSETX = Game.SCALE * 56;
		public static final int ARROW_OFFSETY = Game.SCALE * 30;
		public static final int ARROW_HITBOX_HEIGHT = Game.SCALE * 3;
		public static final int ARROW_HITBOX_WIDTH = Game.SCALE * 15;
		
		public static final int BLADE_BEAM_OFFSETX = Game.SCALE * 56;
		public static final int BLADE_BEAM_OFFSETY = Game.SCALE * 6; 
		public static final int BLADE_BEAM_HITBOX_WIDTH = Game.SCALE * 18;
		public static final int BLADE_BEAM_HITBOX_HEIGHT = Game.SCALE * 51;
		 
		public static final int DAGGER_OFFSETX = Game.SCALE * 56;
		public static final int DAGGER_OFFSETY = Game.SCALE * 31;
		public static final int DAGGER_HITBOX_HEIGHT = Game.SCALE * 2;
		public static final int DAGGER_HITBOX_WIDTH = Game.SCALE * 15;
	}

	public static class AttackTypes {
		public static final int SINGLE = 1;
		public static final int AOE = 2;
	}
	
	public static class StatusEffects {
		public static final int NONE = 1;
		public static final int FREEZE = 2;
		public static final int BLEED = 3;
	}

	public static class Directions {
		public static final int LEFT = -1;
		public static final int RIGHT = 1;
	}

	public static class SamuraiConstants {
		public static final int WIDTH = 256;
		public static final int HEIGHT = 128;
		public static final int HITBOX_OFFSET_X = 117 * Game.SCALE;
		public static final int HITBOX_OFFSET_Y = 47 * Game.SCALE;
		public static final int SAMURAI_ROWS = 12;
		public static final int SAMURAI_COLUMNS = 10;

		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int BACK_RUN = 2;
		public static final int SWORD_ATTACK = 3;
		public static final int BOW_ATTACK = 4;
		public static final int ICE_ARROW_ATTACK = 5;
		public static final int DEAD = 6;
		public static final int ICE_ARROWS_ACTIVATE = 7;
		public static final int CYCLONE = 8;
		public static final int BLADE_BEAM = 9;
		public static final int MYSTICAL_ARROW = 10;
		public static final int WATER_BREATHING_SLASH = 11;
		
		public static final int MELEE_INDEX_1 = 2, MELEE_INDEX_2 = 6;
		public static final int RANGED_INDEX = 4;

		public static int getSpriteAmount(int playerAction) {
			switch (playerAction) {
			case WATER_BREATHING_SLASH:
				return 10;
			case IDLE:
			case RUN:
			case BACK_RUN:
			case SWORD_ATTACK:
			case CYCLONE:
			case MYSTICAL_ARROW:
				return 8;
			case BOW_ATTACK:
			case ICE_ARROW_ATTACK:
			case ICE_ARROWS_ACTIVATE:
			case BLADE_BEAM:
				return 7;
			case DEAD:
			default:
				return 1;
			}
		}
	}
	
	public static class AssassinConstants {
		public static final int WIDTH = 256;
		public static final int HEIGHT = 128;
		public static final int HITBOX_OFFSET_X = 122 * Game.SCALE;
		public static final int HITBOX_OFFSET_Y = 49 * Game.SCALE;
		public static final int ASSASSIN_ROWS = 15;
		public static final int ASSASSIN_COLUMNS = 10;
		
		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int BACK_RUN = 2;
		public static final int STEALTH_IDLE = 3;
		public static final int STEALTH_RUN = 4;
		public static final int STEALTH_BACK_RUN = 5;
		public static final int DAGGER_ATTACK = 6;
		public static final int EMPOWERED_ATTACK = 7;
		public static final int DAGGER_THROW = 8;
		public static final int DEAD = 9;
		public static final int BLINK = 10;
		public static final int SPIN = 11;
		public static final int SMOKESCREEN = 12;
		public static final int GHOST_DASH = 13;
		public static final int BLOSSOM_DANCE = 14;
		
		public static final int MELEE_INDEX_1 = 1, MELEE_INDEX_2 = 6;
		public static final int RANGED_INDEX = 4;
		
		public static int getSpriteAmount(int playerAction) {
			switch (playerAction) {
			case BLOSSOM_DANCE:
				return 10;
			case IDLE:
			case RUN:
			case BACK_RUN:
			case STEALTH_IDLE:
			case STEALTH_RUN:
			case STEALTH_BACK_RUN:
			case DAGGER_ATTACK:
			case GHOST_DASH:
				return 8;
			case EMPOWERED_ATTACK:
				return 7;
			case SPIN:
				return 6;
			case DAGGER_THROW:
				return 5;
			case BLINK:
			case SMOKESCREEN:
				return 4;
			case DEAD:
			default:
				return 1;
			}
		}
	}

	public static class SamuraiAbilityConstants {
		public static final int CYCLONE_UI = 0;
		public static final int BLADE_BEAM_UI = 1;
		public static final int MYSTICAL_ARROW_UI = 2;
		public static final int ICE_ARROWS_UI = 3;
	}
	
	public static class AssassinAbilityConstants {
		public static final int BLINK_UI = 0;
		public static final int SPIN_UI = 1;
		public static final int SMOKESCREEN_UI = 2;
		public static final int GHOST_DASH_UI = 3;
	}
	
	public static class AllyConstants {
		public static final int FARMER = 0;
		public static final int FARMER_WIDTH = 64;
		public static final int FARMER_HEIGHT = 64;
		public static final int FARMEROFFSETX = 24 * Game.SCALE;
		public static final int FARMEROFFSETY = 23 * Game.SCALE;

		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int ATTACK = 2;
		public static final int DEAD = 3;

		public static final int GetSpriteAmount(int allyAction, int allyType) {
			switch (allyType) {
			case FARMER:
				switch (allyAction) {
				case IDLE:
					return 1;
				case RUN:
					return 8;
				case ATTACK:
					return 6;
				case DEAD:
					return 4;
				}
			}
			return 0;
		}

		public static final int GetMaxHealth(int allyType) {
			switch (allyType) {
			case FARMER:
				return 50;
			default:
				return 1;
			}
		}

		public static final int GetCost(int allyType) {
			switch (allyType) {
			case FARMER:
				return 20;
			default:
				return 0;
			}
		}

		public static final int GetAllyDmg(int allyType) {
			switch (allyType) {
			case FARMER:
				return 50;
			default:
				return 20;
			}
		}
	}

	public static class EnemyConstants {
		public static final int SKELETON = 0;
		public static final int SKELETON_WIDTH = 64;
		public static final int SKELETON_HEIGHT = 64;
		public static final int SKELOFFSETX = 18 * Game.SCALE;
		public static final int SKELOFFSETY = 17 * Game.SCALE;

		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int ATTACK = 2;
		public static final int KNOCKBACK = 3;
		public static final int DEAD = 4;

		public static final int getSpriteAmount(int enemyAction, int enemyType) {
			switch (enemyType) {
			case SKELETON:
				switch (enemyAction) {
				case IDLE:
					return 1;
				case RUN:
				case ATTACK:
					return 5;
				case KNOCKBACK:
					return 2;
				case DEAD:
					return 4;
				}
			}
			return 0;
		}

		public static int GetMaxHealth(int enemyType) {
			switch (enemyType) {
			case SKELETON:
				return 100;
			default:
				return 1;
			}
		}

		public static int GetEnemyDmg(int enemyType) {
			switch (enemyType) {
			case SKELETON:
				return 15;
			default:
				return 0;
			}
		}
	}
}

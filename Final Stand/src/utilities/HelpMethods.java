package utilities;

import main.Game;

public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float w, float h) {
		if (!IsSolid(x, y))
			if (!IsSolid(x + w, y + h))
				if (!IsSolid(x + w, y))
					if (!IsSolid(x, y + h))
						return true;
		return false;
	}

	private static boolean IsSolid(float x, float y) {
		if (x < 0 || x > 960 * Game.SCALE)
			return true;
		if (y < 0 || y > Game.GAME_HEIGHT)
			return true;
		return false;

		// add checks for other hitboxes
	}
}

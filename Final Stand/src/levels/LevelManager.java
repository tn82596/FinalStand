package levels;

public class LevelManager {
	private Level levelOne;

	// constructor that calls methods that create every level
	// one method to create each level with the data of the level
	public LevelManager(int skeletonAmount) {
		levelOne = new Level(skeletonAmount);
	}
}

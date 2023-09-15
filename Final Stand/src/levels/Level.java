package levels;

public class Level {

	private int skeletonAmount;

	// # of monsters, etc.
	public Level(int skeletonAmount) {
		this.skeletonAmount = skeletonAmount;
	}

	public int getSkeletonAmont() {
		return skeletonAmount;
	}
}

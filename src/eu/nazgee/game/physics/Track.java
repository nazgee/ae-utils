package eu.nazgee.game.physics;


abstract public class Track implements ITrack {
	private float mScale = 1;
	private float mNorm = 0;
	private boolean isNormalized = false;

	@Override
	public float getScale() {
		return mScale;
	}

	@Override
	public void setScale(float pScale) {
		mScale = pScale;
	}

	@Override
	public float getNorm() {
		return mNorm;
	}

	@Override
	public void setNorm(float pNorm) {
		mNorm = pNorm;
		isNormalized = true;
	}

	@Override
	public boolean isNormalized() {
		return (isNormalized);
	}
}

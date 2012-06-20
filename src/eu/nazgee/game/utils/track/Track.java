//package eu.nazgee.game.utils.track;
//
//import com.badlogic.gdx.math.Vector2;
//
//
//public class Track implements ITrack {
//	private float mScale = 1;
//	private float mNorm = 0;
//	private boolean isNormalized = false;
//	protected Vector2 mVec;
//
//	public Track(float x, float y) {
//		mVec = new Vector2(x, y);
//	}
//
//	@Override
//	public Vector2 getTrack() {
//		if (isNormalized()) {
//			mVec.set(mVec.nor()).mul(getNorm());
//		}
//
//		if (getScale() != 1) {
//			return mVec.mul(getScale());
//		} else {
//			return mVec;
//		}
//	}
//
//	@Override
//	public float getScale() {
//		return mScale;
//	}
//
//	@Override
//	public void setScale(float pScale) {
//		mScale = pScale;
//	}
//
//	@Override
//	public float getNorm() {
//		return mNorm;
//	}
//
//	@Override
//	public void setNorm(float pNorm) {
//		mNorm = pNorm;
//		isNormalized = true;
//	}
//
//	@Override
//	public boolean isNormalized() {
//		return (isNormalized);
//	}
//}

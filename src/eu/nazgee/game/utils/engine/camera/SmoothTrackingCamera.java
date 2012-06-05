package eu.nazgee.game.utils.engine.camera;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.misc.UtilsMath;
import eu.nazgee.game.utils.track.ITrack;

public class SmoothTrackingCamera extends SmoothCamera {
	private IEntity mChaseBody;
	private ITrack mTrack;
	private float mMaxVelocityDeg = 0;
	private float mOffsetDeg = 0;
	Vector2 mTrackHelper = new Vector2();

	final ISmoother mSmootherX;
	final ISmoother mSmootherY;
	final ISmoother mSmootherRot;


	public SmoothTrackingCamera(float pX, float pY, float pWidth, float pHeight, final float pMaxZoomFactorChange, ISmoother pSmootherX, ISmoother pSmootherY, ISmoother pSmootherRot) {
		super(pX, pY, pWidth, pHeight, 0, 0, pMaxZoomFactorChange);
		mSmootherX = pSmootherX;
		mSmootherY = pSmootherY;
		mSmootherRot = pSmootherRot;
	}

	public void setTracking(IEntity pChaseBody, ITrack pTracking, float pOffsetDeg) {
		super.setChaseEntity(pChaseBody);
		mChaseBody = pChaseBody;
		setTracking(pTracking, pOffsetDeg);
	}

	public void setTracking(ITrack pTracking, float pOffsetDeg) {
		mTrack = pTracking;
		mOffsetDeg = pOffsetDeg;
	}

	public ITrack getTracking() {
		return mTrack;
	}

	protected float limitToMaxDeg(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0) {
			return Math.min(pValue, this.getMaxVelocityDeg() * pSecondsElapsed);
		} else {
			return Math.max(pValue, -this.getMaxVelocityDeg() * pSecondsElapsed);
		}
	}

	@Override
	public void reset() {
		super.reset();
		setTracking(null, null, 0);
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		// Adjust maximum X-Y speeds
		final float dX = Math.abs(this.getCenterX() - this.getTargetCenterX());
		final float dY = Math.abs(this.getCenterY() - this.getTargetCenterY());
		setMaxVelocityX(mSmootherX.getCorrectionSpeed(dX, getMaxVelocityX()));
		setMaxVelocityY(mSmootherY.getCorrectionSpeed(dY, getMaxVelocityY()));
		// Set X-Y position
		super.onUpdate(pSecondsElapsed);

		if (mTrack == null)
			return;

		// Adjust maximum Rot speed
		Vector2 track = Vector2Pool.obtain(mTrack.getTrack());
		float currDeg = getRotation();
		float dDeg = currDeg - (mOffsetDeg - UtilsMath.getAngleDeg(track));
		dDeg = UtilsMath.normalizeAngleDeg(dDeg, 0);
		setMaxVelocityDeg(mSmootherRot.getCorrectionSpeed(Math.abs(dDeg), getMaxVelocityDeg()));
		float deltaDeg = limitToMaxDeg(dDeg, pSecondsElapsed);
		// Set X-Y rotation
		this.setRotation(currDeg - deltaDeg);
		this.setCameraSceneRotation(0);
		
//		Log.d(getClass().getSimpleName(), "onUpdate maxX=" + getMaxVelocityX() + "maxY=" + getMaxVelocityY());
		Vector2Pool.recycle(track);	
	}

	@Override
	public void setCenter(float pCenterX, float pCenterY) {
		if(mChaseBody != null) {
			// This is how we center not on an object itself, but on a given point
			// on a ITrack vector
			mTrackHelper.set(mTrack.getTrack());
			pCenterX += mTrackHelper.x;
			pCenterY += mTrackHelper.y;
//			Log.d(getClass().getSimpleName(), "setCenter cX=" + pCenterX + "cY=" + pCenterY);
		} else {
//			Log.d(getClass().getSimpleName(), "setCenter (null-chase) cX=" + pCenterX + "cY=" + pCenterY);
		}
		super.setCenter(pCenterX, pCenterY);
	}

	public float getMaxVelocityDeg() {
		return mMaxVelocityDeg;
	}
	
	public void setMaxVelocityDeg(float velocityDeg) {
		mMaxVelocityDeg = velocityDeg;
	}
}

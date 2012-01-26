package eu.nazgee.game.engine.camera;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.physics.ITrack;
import eu.nazgee.game.utils.UtilsMath;

public class SmoothTrackingCamera extends SmoothCamera {
	private PhysicsConnector mChaseBody;
	private ITrack mTrack;
	private float mMaxVelocityDeg = 0;
	private float mOffsetDeg = 0;
	private float mOffsetLen = 0;
	
	final ISmoother mSmootherX;
	final ISmoother mSmootherY;
	final ISmoother mSmootherRot;
	
	
	public SmoothTrackingCamera(float pX, float pY, float pWidth, float pHeight, final float pMaxZoomFactorChange, ISmoother pSmootherX, ISmoother pSmootherY, ISmoother pSmootherRot) {
		super(pX, pY, pWidth, pHeight, 0, 0, pMaxZoomFactorChange);
		mSmootherX = pSmootherX;
		mSmootherY = pSmootherY;
		mSmootherRot = pSmootherRot;
	}
	
	public void setTracking(PhysicsConnector pChaseBody, ITrack pTracking, float pOffsetDeg, float pOffsetLen) {
		super.setChaseEntity(pChaseBody.getShape());
		mTrack = pTracking;
		mChaseBody = pChaseBody;
		mOffsetDeg = pOffsetDeg;
		mOffsetLen = pOffsetLen;
	}
	
	protected float limitToMaxDeg(final float pValue, final float pSecondsElapsed) {
		if(pValue > 0) {
			return Math.min(pValue, this.getMaxVelocityDeg() * pSecondsElapsed);
		} else {
			return Math.max(pValue, -this.getMaxVelocityDeg() * pSecondsElapsed);
		}
	}
	
	private float getAdjustedSpeed(float pSpeed, float pDistance, float pNormalSpeed, float pSpeedupThreshold, float pNormalThreshold, float pSlowdownThreshold) {
		
		float ETA = pDistance / pSpeed;
		
		if (ETA > pSpeedupThreshold) {
			// speed up as needed not to stay behind
			return pDistance;
		} else if (pSpeed >= pNormalSpeed && ETA < pSlowdownThreshold) {
			// slow down, as we can afford it now
			return pNormalSpeed / 2;
		} else if (pSpeed != pNormalSpeed && ETA < pNormalThreshold) {
			// we were slow/fast. set the normal pace now
			return pNormalSpeed;
		}
		return pSpeed;
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
		
		// Adjust maximum Rot speed
		Vector2 track = Vector2Pool.obtain(mTrack.getTrack());
		float currDeg = getRotation();
		float dDeg = currDeg - (mOffsetDeg - UtilsMath.getAngleDeg(track));
		dDeg = UtilsMath.normalizeAngleDeg(dDeg, 0);
		setMaxVelocityDeg(mSmootherRot.getCorrectionSpeed(Math.abs(dDeg), getMaxVelocityDeg()));
		float deltaDeg = limitToMaxDeg(dDeg, pSecondsElapsed);
		// Set X-Y rotation
		this.setRotation(currDeg - deltaDeg);
		
		Vector2Pool.recycle(track);	
	}

	@Override
	public void setCenter(float pCenterX, float pCenterY) {
		if(mChaseBody != null && (mOffsetLen != 0)) {
			// This is how we center not on an object itself, but on a given point
			// on a ITrack vector
			Vector2 track = Vector2Pool.obtain(mTrack.getTrack());
			track.nor().mul(mOffsetLen);
			pCenterX += track.x;
			pCenterY += track.y;
			Vector2Pool.recycle(track);
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

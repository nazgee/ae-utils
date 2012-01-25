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
	private float mOffsetDeg = 0;
	private float mOffsetLen = 0;
	private float mMaxVelocityDeg;
	private final float mMaxVelocityDegOrig;
	private final float mMaxVelocityOrigX;
	private final float mMaxVelocityOrigY;
	
	final float mSlowdownThresholdX = 0.4f;
	final float mNormalThresholdX = 0.7f;
	final float mSpeedupThresholdX = 1;
	final float mSlowdownThresholdY = 0.4f;
	final float mNormalThresholdY = 0.7f;
	final float mSpeedupThresholdY = 1;
	final float mSlowdownThresholdDeg = 0.1f;
	final float mNormalThresholdDeg = 0.2f;
	final float mSpeedupThresholdDeg = 0.3f;
	
	
	public SmoothTrackingCamera(float pX, float pY, float pWidth, float pHeight, final float pMaxVelocityX, final float pMaxVelocityY, final float pMaxZoomFactorChange, final float pMaxVelocityDeg) {
		super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);
		setMaxVelocityDeg(pMaxVelocityDeg);
		
		// store velocities for the future reference
		mMaxVelocityDegOrig = pMaxVelocityDeg;
		mMaxVelocityOrigX = pMaxVelocityX;
		mMaxVelocityOrigY = pMaxVelocityY;
	}
	
	public void setTracking(PhysicsConnector pChaseBody, ITrack pTracking, float pOffsetDeg, float pOffsetLen) {
		super.setChaseEntity(pChaseBody.getShape());
		this.mTrack = pTracking;
		this.mChaseBody = pChaseBody;
		this.mOffsetDeg = pOffsetDeg;
		this.mOffsetLen = pOffsetLen;
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
		
		setMaxVelocityX( getAdjustedSpeed(getMaxVelocityX(), dX, mMaxVelocityOrigX, mSpeedupThresholdX, mNormalThresholdX, mSlowdownThresholdX) );
		setMaxVelocityY( getAdjustedSpeed(getMaxVelocityY(), dY, mMaxVelocityOrigY, mSpeedupThresholdY, mNormalThresholdY, mSlowdownThresholdY) );
		
		// this will update x-y position by eventually calculating
		// new, smooth x-y, calling updateChaseEntity() and then setCenter()
		super.onUpdate(pSecondsElapsed);
		
		// update rotation
		Vector2 track = Vector2Pool.obtain(mTrack.getTrack());
		float currDeg = getRotation();
		float dDeg = currDeg - (mOffsetDeg - UtilsMath.getAngleDeg(track));
		dDeg = UtilsMath.normalizeAngleDeg(dDeg, 0);
		setMaxVelocityDeg(getAdjustedSpeed(getMaxVelocityDeg(), Math.abs(dDeg), mMaxVelocityDegOrig, mSpeedupThresholdDeg, mNormalThresholdDeg, mSlowdownThresholdDeg) );
		float deltaDeg = limitToMaxDeg(dDeg, pSecondsElapsed);
		this.setRotation(currDeg - deltaDeg);
		
//		Log.e("SmoothRotation", "dDeg=" + dDeg + "; deltaDeg=" + deltaDeg + "; currDeg=" + currDeg);
		Vector2Pool.recycle(track);	
	}

	@Override
	public void setCenter(float pCenterX, float pCenterY) {
		// move given camera center in tracking direction by mOffsetLen
		if(mChaseBody != null && (mOffsetLen != 0)) {
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

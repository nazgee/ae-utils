package eu.nazgee.game.utils.physics;

import org.andengine.engine.handler.BaseEntityUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.util.math.MathUtils;

import eu.nazgee.game.utils.misc.UtilsMath;
import eu.nazgee.game.utils.track.Track;

public class BoundedPhysicsHandler extends BaseEntityUpdateHandler{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mEnabledVelocity = true;
	private boolean mEnabledRotation = true;
	private boolean mEnabledRotationAuto = true;

	private Track mLinearAccelerationTrack = new Track(0, 0);
	private Track mLinearVelocityTrack = new Track(0, 0);

	private float mAngularOffset = 0;
	protected float mAccelerationAngular = 0;
	protected float mAngularVelocity = 0;

	protected float mMaxLinearVelocity = Float.MAX_VALUE;
	protected float mMinLinearVelocity = 0;
	protected float mMaxAngularVelocity = Float.MAX_VALUE;
	protected float mMinAngularVelocity = 0;
	// ===========================================================
	// Constructors
	// ===========================================================
	public BoundedPhysicsHandler(IEntity pEntity) {
		super(pEntity);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public float getAngularOffset() {
		return mAngularOffset;
	}

	public void setAngularOffset(float mAngularOffset) {
		this.mAngularOffset = mAngularOffset;
	}

	public boolean isEnabledVelocity() {
		return this.mEnabledVelocity;
	}

	public void setEnabledVelocity(final boolean pEnabled) {
		this.mEnabledVelocity = pEnabled;
	}

	public boolean isEnabledRotation() {
		return this.mEnabledRotation;
	}

	public void setEnabledRotation(final boolean pEnabled) {
		this.mEnabledRotation = pEnabled;
	}

	public boolean isEnabledRotationAuto() {
		return this.mEnabledRotationAuto;
	}

	public void setEnabledRotationAuto(final boolean pEnabled) {
		this.mEnabledRotationAuto = pEnabled;
	}

	public float getVelocityX() {
		return this.mLinearVelocityTrack.getTrack().x;
	}

	public float getVelocityY() {
		return this.mLinearVelocityTrack.getTrack().y;
	}

	public void setVelocityRatio(float ratio) {
		mLinearVelocityTrack.getTrack().mul(ratio);
	}

	public void setVelocity(float pVelocity) {
		float ratio = pVelocity / getVelocity();
		setVelocityRatio(ratio);
	}
	

	public float getVelocity() {
		return MathUtils.length(getVelocityX(), getVelocityY());
	}

	public Track getVelocityTrack() {
		return mLinearVelocityTrack;
	}

	public void setVelocityX(final float pVelocityX) {
		mLinearVelocityTrack.getTrack().set(pVelocityX, mLinearVelocityTrack.getTrack().y);
	}

	public void setVelocityY(final float pVelocityY) {
		mLinearVelocityTrack.getTrack().set(mLinearVelocityTrack.getTrack().x, pVelocityY);
	}

	public void setVelocity(final float pVelocityX, final float pVelocityY) {
		mLinearVelocityTrack.getTrack().set(pVelocityX, pVelocityY);
	}

	public float getAccelerationX() {
		return mLinearAccelerationTrack.getTrack().x;
	}

	public float getAccelerationY() {
		return mLinearAccelerationTrack.getTrack().y;
	}

	public void setAccelerationX(final float pAccelerationX) {
		mLinearAccelerationTrack.getTrack().set(pAccelerationX, mLinearAccelerationTrack.getTrack().y);
	}

	public void setAccelerationY(final float pAccelerationY) {
		mLinearAccelerationTrack.getTrack().set(mLinearAccelerationTrack.getTrack().x, pAccelerationY);
	}

	public void setAccelerationRatio(float ratio) {
		mLinearAccelerationTrack.getTrack().mul(ratio);
	}

	public void setAcceleration(float pAcceleration) {
		mLinearAccelerationTrack.getTrack().set(mLinearVelocityTrack.getTrack());
		mLinearAccelerationTrack.getTrack().nor().mul(pAcceleration);
	}

	public float getAcceleration() {
		return MathUtils.length(getAccelerationX(), getAccelerationY());
	}

	public void setAcceleration(final float pAccelerationX, final float pAccelerationY) {
		mLinearAccelerationTrack.getTrack().set(pAccelerationX, pAccelerationY);
	}

	public void accelerate(final float pAccelerationX, final float pAccelerationY) {
		mLinearAccelerationTrack.getTrack().add(pAccelerationX, pAccelerationY);
	}

	public float getAccelerationAngular() {
		return this.mAccelerationAngular;
	}

	public void setAccelerationAngular(final float pAccelerationAngular) {
		this.mAccelerationAngular = pAccelerationAngular;
	}

	public float getAngularVelocity() {
		return this.mAngularVelocity;
	}

	public void setAngularVelocity(final float pAngularVelocity) {
		this.mAngularVelocity = pAngularVelocity;
	}

	public void setLimitsLinear(float pMin, float pMax) {
		mMaxLinearVelocity = pMax;
		mMinLinearVelocity = pMin;
	}

	public void setLimitsAngular(float pMin, float pMax) {
		mMaxAngularVelocity= pMax;
		mMinAngularVelocity = pMin;
	}

	public float getMaxLinearVelocity() {
		return mMaxLinearVelocity;
	}

	public void setMaxLinearVelocity(float mMaxLinearVelocity) {
		this.mMaxLinearVelocity = mMaxLinearVelocity;
	}

	public float getMinLinearVelocity() {
		return mMinLinearVelocity;
	}

	public void setMinLinearVelocity(float mMinLinearVelocity) {
		this.mMinLinearVelocity = mMinLinearVelocity;
	}

	public float getMaxAngularVelocity() {
		return mMaxAngularVelocity;
	}

	public void setMaxAngularVelocity(float mMaxAngularVelocity) {
		this.mMaxAngularVelocity = mMaxAngularVelocity;
	}

	public float getMinAngularVelocity() {
		return mMinAngularVelocity;
	}

	public void setMinAngularVelocity(float mMinAngularVelocity) {
		this.mMinAngularVelocity = mMinAngularVelocity;
	}

	protected void applyAccelerationAngular(float pSecondsElapsed) {
		/* Apply linear acceleration. */
		final float accelerationAng = this.mAccelerationAngular;
		if(accelerationAng != 0) {
			this.mAngularVelocity += accelerationAng * pSecondsElapsed;
		}
	}

	protected void applyAccelerationLinear(float pSecondsElapsed) {
		/* Apply linear acceleration. */
		final float accelerationX = mLinearAccelerationTrack.getTrack().x;
		final float accelerationY = mLinearAccelerationTrack.getTrack().y;
		if(accelerationX != 0 || accelerationY != 0) {
			this.mLinearVelocityTrack.getTrack().add(
					accelerationX * pSecondsElapsed,
					accelerationY * pSecondsElapsed);
		}
	}

	protected void applyLimitsAngular() {
		final float v = mAngularVelocity;
		if (v > mMaxAngularVelocity) {
			mAngularVelocity = mMaxAngularVelocity;
		} else if (v < mMinAngularVelocity) {
			mAngularVelocity = mMinAngularVelocity;
		}
	}

	protected void applyLimitsLinear() {
		final float v = getVelocity();
		if (v > mMaxLinearVelocity) {
			setVelocityRatio(mMaxLinearVelocity / v);
		} else if (v < mMinLinearVelocity) {
			setVelocityRatio(mMinLinearVelocity / v);
		}
	}

	protected void applyVelocityAngular(IEntity pEntity, float pSecondsElapsed) {
		/* Apply angular velocity. */
		final float angularVelocity = this.mAngularVelocity;
		if(angularVelocity != 0) {
			pEntity.setRotation(pEntity.getRotation() + angularVelocity * pSecondsElapsed);
		}
	}

	protected void applyVelocityLinear(IEntity pEntity, float pSecondsElapsed) {
		/* Apply linear velocity. */
		final float velocityX = this.mLinearVelocityTrack.getTrack().x;
		final float velocityY = this.mLinearVelocityTrack.getTrack().y;
		if(velocityX != 0 || velocityY != 0) {
			pEntity.setPosition(pEntity.getX() + velocityX * pSecondsElapsed, pEntity.getY() + velocityY * pSecondsElapsed);
		}
	}

	@Override
	protected void onUpdate(float pSecondsElapsed, IEntity pEntity) {
		if (mEnabledRotation) {
			applyAccelerationAngular(pSecondsElapsed);
			applyLimitsAngular();

			if (mEnabledRotationAuto) {
				UtilsMath.vectorRotateDeg(mLinearVelocityTrack.getTrack(), mAngularVelocity*pSecondsElapsed);
			}
		}

		if (mEnabledVelocity) {
			applyAccelerationLinear(pSecondsElapsed);
			applyLimitsLinear();
			applyVelocityLinear(pEntity, pSecondsElapsed);
		}
		if (mEnabledRotation) {
			if (mEnabledRotationAuto) {
				pEntity.setRotation(UtilsMath.getAngleDeg(mLinearVelocityTrack.getTrack()) + getAngularOffset());
			} else {
				applyVelocityAngular(pEntity, pSecondsElapsed);
			}
		}
	}

	@Override
	public void reset() {
		this.mLinearAccelerationTrack.getTrack().set(0, 0);
		this.mLinearVelocityTrack.getTrack().set(0,0);
		this.mAccelerationAngular = 0;
		this.mAngularVelocity = 0;
		this.mMaxLinearVelocity = Float.MAX_VALUE;
		this.mMinLinearVelocity = 0;
		this.mMaxAngularVelocity = Float.MAX_VALUE;
		this.mMinAngularVelocity = 0;
	}
}

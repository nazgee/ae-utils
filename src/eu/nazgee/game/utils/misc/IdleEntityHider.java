package eu.nazgee.game.utils.misc;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.util.modifier.ease.IEaseFunction;

public abstract class IdleEntityHider<T extends IEntity> implements IUpdateHandler{
	private boolean mShown;
	private final T mHideThis;
	private final float hiddenX;
	private final float hiddenY;
	private final float shownX;
	private final float shownY;
	private final IEaseFunction mEasing;
	private final float mDuration;
	private IEntityModifier mHider;

	public IdleEntityHider(T pHideThis, final float moveX, final float moveY, final IEaseFunction pEasing, final float pDuration) {
		mHideThis = pHideThis;
		mEasing = pEasing;
		mDuration = pDuration;
		shownX = pHideThis.getX();
		shownY = pHideThis.getY();
		hiddenX = shownX + moveX;
		hiddenY = shownY + moveY;
		mShown = true;
	}

	/**
	 * Should return true until item in question is to be visible on original position
	 * @param pItem in question
	 * @return false to hide, true to show pItem
	 */
	abstract protected boolean keepShowing(T pItem);

	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (mShown != keepShowing(mHideThis)) {
			float toX;
			float toY;
			if (keepShowing(mHideThis)) {
				toX = shownX;
				toY = shownY;
				mShown = true;
			} else {
				toX = hiddenX;
				toY = hiddenY;
				mShown = false;
			}

			mHideThis.unregisterEntityModifier(mHider);
			mHider = new MoveModifier(mDuration,
					mHideThis.getX(), toX,
					mHideThis.getY(), toY, mEasing);
			mHider.setAutoUnregisterWhenFinished(true);
			mHideThis.registerEntityModifier(mHider);
		}
	}
	@Override
	public void reset() {
	}
}

package eu.nazgee.game.utils.misc;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.util.modifier.ease.IEaseFunction;

public abstract class IdleEntityHider<T extends IEntity> {
	private boolean mShown;
	protected final T mHideThis;
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

	protected void hide() {
		move(hiddenX, hiddenY);
		mShown = false;
	}

	protected void show() {
		move(shownX, shownY);
		mShown = true;
	}

	protected void move(final float toX, final float toY) {
		mHideThis.unregisterEntityModifier(mHider);
		mHider = new MoveModifier(mDuration,
				mHideThis.getX(), toX,
				mHideThis.getY(), toY, mEasing);
		mHider.setAutoUnregisterWhenFinished(false);
		mHideThis.registerEntityModifier(mHider);
	}

	public boolean isShown() {
		return mShown;
	}
}

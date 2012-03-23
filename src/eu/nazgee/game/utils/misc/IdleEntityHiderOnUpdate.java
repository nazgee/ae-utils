package eu.nazgee.game.utils.misc;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.IEaseFunction;

public class IdleEntityHiderOnUpdate<T extends IEntity> extends IdleEntityHider<T> implements IUpdateHandler {

	public IdleEntityHiderOnUpdate(T pHideThis, float moveX, float moveY,
			IEaseFunction pEasing, float pDuration) {
		super(pHideThis, moveX, moveY, pEasing, pDuration);
	}

	@Override
	protected boolean keepShowing(IEntity pItem) {
		return false;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (isShown() != keepShowing(mHideThis)) {
			if (keepShowing(mHideThis)) {
				show();
			} else {
				hide();
			}
		}
	}

	@Override
	public void reset() {
	}
}

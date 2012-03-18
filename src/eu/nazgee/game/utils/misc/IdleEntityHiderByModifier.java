package eu.nazgee.game.utils.misc;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.util.modifier.ease.IEaseFunction;

public class IdleEntityHiderByModifier<T extends IEntity> extends IdleEntityHider<T>{

	private IEntityModifier mModifier;

	public IdleEntityHiderByModifier(T pHideThis, final float moveX,
			final float moveY, final IEaseFunction pEasing, final float pDuration, final IEntityModifier pModifier) {
		super(pHideThis, moveX, moveY, pEasing, pDuration);
		setModifier(pModifier);
	}

	@Override
	protected boolean keepShowing(T pItem) {
		if (getModifier() == null)
			return false;

		return !getModifier().isFinished();
	}

	public IEntityModifier getModifier() {
		return mModifier;
	}

	public void setModifier(IEntityModifier mModifier) {
		this.mModifier = mModifier;
	}
}

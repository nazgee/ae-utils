package eu.nazgee.game.utils.misc;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.IEaseFunction;

public class IdleEntityHiderByModifier<T extends IEntity> extends IdleEntityHider<T>{

	private IEntityModifier mModifier;
	private ModListener mListener = new ModListener();

	public IdleEntityHiderByModifier(T pHideThis, final float moveX,
			final float moveY, final IEaseFunction pEasing, final float pDuration, final IEntityModifier pModifier) {
		super(pHideThis, moveX, moveY, pEasing, pDuration);
		setModifier(pModifier);
	}

	@Override
	protected boolean keepShowing(T pItem) {
		throw new RuntimeException("This class should rely on events!");
	}

	public IEntityModifier getModifier() {
		return mModifier;
	}

	public void setModifier(IEntityModifier mModifier) {
		if (mModifier != null) {
			mModifier.removeModifierListener(mListener);
		}

		this.mModifier = mModifier;

		if (mModifier != null) {
			mModifier.addModifierListener(mListener);
		}
	}

	private class ModListener implements IEntityModifierListener {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier,
				IEntity pItem) {
			show();
		}

		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier,
				IEntity pItem) {
			hide();
		}
	}
}

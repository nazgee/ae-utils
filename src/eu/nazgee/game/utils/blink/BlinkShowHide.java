package eu.nazgee.game.utils.blink;

import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;

public class BlinkShowHide implements IBlinkFactory {

	private final float mDurationShow;
	private final float mDurationHide;
	private final float mDurationKeep;

	public BlinkShowHide(float pDurationShow, float pDurationHide,
			float pDurationKeep) {
		super();
		this.mDurationShow = pDurationShow;
		this.mDurationHide = pDurationHide;
		this.mDurationKeep = pDurationKeep;
	}

	@Override
	public IEntityModifier populateBlink() {
		IEntityModifier mod = new SequenceEntityModifier(
						new FadeInModifier(mDurationShow),
						new DelayModifier(mDurationKeep),
						new FadeOutModifier(mDurationHide)
						);
		return mod;
	}
}

package eu.nazgee.game.utils.blink;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.IEaseFunction;

public class BlinkScale implements IBlinkFactory {

	private final int mCount;
	private final float mSingleDuration;
	private final float mScaleFrom;
	private final float mScaleTo;
	private final IEaseFunction mEasingFrom;
	private final IEaseFunction mEasingTo;

	public BlinkScale(int pCount, float pSingleDuration, float pScaleFrom, float pScaleTo) {
		this(pCount, pSingleDuration, pScaleFrom, pScaleTo, EaseStrongInOut.getInstance(), EaseStrongInOut.getInstance());
	}

	public BlinkScale(int pCount, float pSingleDuration, float pScaleFrom, float pScaleTo, IEaseFunction pEasingFrom, IEaseFunction pEasingTo) {
		super();
		mCount = pCount;
		mSingleDuration = pSingleDuration;
		mScaleFrom = pScaleFrom;
		mScaleTo = pScaleTo;
		mEasingFrom = pEasingFrom;
		mEasingTo = pEasingTo;
	}

	@Override
	public IEntityModifier populateBlink() {
		IEntityModifier mod = new LoopEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(mSingleDuration/2, mScaleFrom, mScaleTo, mEasingFrom),
						new ScaleModifier(mSingleDuration/2, mScaleTo, mScaleFrom, mEasingTo)
						), mCount);
		return mod;
	}

}

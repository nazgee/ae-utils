package eu.nazgee.game.utils.blink;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.EaseStrongOut;

public class BlinkNodYourHead implements IBlinkFactory {

	private final int mNodsCount;
	private final float mNodDuration;
	private final float mNodAngle;

	public BlinkNodYourHead(int pNodsCount, float pNodDuration,
			float pNodAngle) {
		super();
		this.mNodsCount = pNodsCount;
		this.mNodDuration = pNodDuration;
		this.mNodAngle = pNodAngle;
	}

	@Override
	public IEntityModifier populateBlink() {
		IEntityModifier mod = new LoopEntityModifier(
				new SequenceEntityModifier(
						new RotationModifier(mNodDuration/4, 0,        -mNodAngle/2, EaseStrongOut.getInstance()),
						new RotationModifier(mNodDuration/2, -mNodAngle/2, mNodAngle,    EaseStrongInOut.getInstance()),
						new RotationModifier(mNodDuration/4, mNodAngle,    0,        EaseStrongIn.getInstance())
						), mNodsCount);
		return mod;
	}

}

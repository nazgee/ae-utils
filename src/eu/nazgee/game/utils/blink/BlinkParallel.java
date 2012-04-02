package eu.nazgee.game.utils.blink;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;

public class BlinkParallel implements IBlinkFactory {

	private final IBlinkFactory mBlinkFactories[];

	public BlinkParallel(final IBlinkFactory ... mBlinkFactories) {
		super();
		this.mBlinkFactories = mBlinkFactories;
	}

	@Override
	public IEntityModifier populateBlink() {
		IEntityModifier[] mods = new IEntityModifier[mBlinkFactories.length];

		for (int i = 0; i < mods.length; i++) {
			mods[i] = mBlinkFactories[i].populateBlink();
		}

		return new ParallelEntityModifier(mods);
	}

}

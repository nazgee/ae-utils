package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;

public abstract class Loadable implements ILoadable, ILoadingHandler {

	abstract public Loader getLoader();

	@Override
	final public void load(final Engine e, Context c) {
		getLoader().load(e, c);
	}

	@Override
	final public void unload() {
		getLoader().unload();
	}

	@Override
	final public boolean isLoaded() {
		return getLoader().isLoaded();
	}
}

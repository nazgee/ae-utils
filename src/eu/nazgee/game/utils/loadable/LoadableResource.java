package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;

public abstract class LoadableResource extends Loadable implements ILoadableResource, ILoadingHandlerResource {
	@Override
	abstract public LoaderResource getLoader();

	@Override
	final public void loadResources(Engine e, Context c) {
		getLoader().loadResources(e, c);
	}

	@Override
	final public boolean isResourceLoaded() {
		return getLoader().isResourceLoaded();
	}
}

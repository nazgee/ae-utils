package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;

public class LoaderResource extends Loader implements ILoadableResource {
	// ===========================================================
	// Fields
	// ===========================================================
	private ILoadingHandlerResource mLoadingHandler;
	protected boolean mResourcesLoaded = false;
	// ===========================================================
	// Constructors
	// ===========================================================
	public LoaderResource(ILoadingHandlerResource pLoadingHandler) {
		super(pLoadingHandler);
		setLoadingHandler(pLoadingHandler);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	public boolean uninstall(ILoadableResource pRes) {
		return super.uninstall(pRes);
	}

	@Override
	public void install(ILoadableResource pRes) {
		super.install(pRes);
	}

	public ILoadingHandlerResource getLoadingHandler() {
		return mLoadingHandler;
	}

	public void setLoadingHandler(ILoadingHandlerResource pHandler) {
		mLoadingHandler = pHandler;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void loadResources(Engine e, Context c) {
		if (!isResourceLoaded()) {
			for (ILoadable res : mResources) {
				((ILoadableResource)res).loadResources(e, c);
			}
			if (getLoadingHandler() != null) {
				getLoadingHandler().onLoadResources(e, c);
			}
			setResourceLoaded(true);
		}
	}

	@Override
	public boolean isResourceLoaded() {
		return mResourcesLoaded;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	protected void setResourceLoaded(boolean pValue) {
		mResourcesLoaded = pValue;
	}
}

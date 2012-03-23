package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;
import android.util.Log;

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
	public boolean uninstall(ILoadableResource pRes) {
		return super.uninstall(pRes);
	}

	public void install(ILoadableResource pRes) {
		super.install(pRes);
	}

	public void installForced(ILoadableResource pRes, Engine e, Context c) {
		pRes.loadResources(e, c);
		super.installForced(pRes, e, c);
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
			synchronized (mResources) {
				for (ILoadable res : mResources) {
					Log.d(getClass().getSimpleName(), "About to loadResources()" + res.toString());
					((ILoadableResource)res).loadResources(e, c);
				}
			}
			if (getLoadingHandler() != null) {
				Log.d(getClass().getSimpleName(), "About to onLoadResources()" + getLoadingHandler().toString());
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

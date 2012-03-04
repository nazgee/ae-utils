package eu.nazgee.game.utils.loadable;

import java.util.LinkedList;

import org.andengine.engine.Engine;

import eu.nazgee.game.utils.misc.Reversed;

import android.content.Context;

public class ResourceLoader implements ILoadableResource {
	// ===========================================================
	// Fields
	// ===========================================================
	private IResourceLoadingHandler mLoadingHandler;
	private LinkedList<ILoadableResource> mResources = new LinkedList<ILoadableResource>();
	protected Boolean mLoaded = new Boolean(false);
	protected boolean mResourcesLoaded = false;
	// ===========================================================
	// Constructors
	// ===========================================================
	public ResourceLoader(IResourceLoadingHandler pLoadingHandler) {
		setLoadingHandler(pLoadingHandler);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public boolean uninstall(ILoadableResource pRes) {
		return mResources.remove(pRes);
	}

	public void install(ILoadableResource pRes) {
		mResources.add(pRes);
	}

	public IResourceLoadingHandler getLoadingHandler() {
		return mLoadingHandler;
	}

	public void setLoadingHandler(IResourceLoadingHandler pHandler) {
		mLoadingHandler = pHandler;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void load(Engine e, Context c) {
		synchronized (mLoaded) {
			assertLoaded(false);
			for (ILoadableResource res : mResources) {
				res.load(e, c);
			}
			if (getLoadingHandler() != null) {
				getLoadingHandler().onLoad(e, c);
			}
			setLoaded(true);
		}
	}
	@Override
	public void unload() {
		synchronized (mLoaded) {
			assertLoaded(true);

			if (getLoadingHandler() != null) {
				getLoadingHandler().onUnload();
			}

			Reversed<ILoadableResource> rev = new Reversed<ILoadableResource>(mResources);
			for (ILoadableResource res : rev) {
				res.unload();
			}
			setLoaded(false);
		}
	}
	@Override
	public void loadResources(Engine e, Context c) {
		if (!isResourceLoaded()) {
			for (ILoadableResource res : mResources) {
				res.loadResources(e, c);
			}
			if (getLoadingHandler() != null) {
				getLoadingHandler().onLoadResources(e, c);
			}
			setResourceLoaded(true);
		}
	}

	@Override
	public boolean isLoaded() {
		return mLoaded.booleanValue();
	}

	@Override
	public boolean isResourceLoaded() {
		return mResourcesLoaded;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	protected void setLoaded(boolean pValue) {
		mLoaded = pValue;
	}

	protected void setResourceLoaded(boolean pValue) {
		mResourcesLoaded = pValue;
	}

	protected void assertLoaded(boolean pValue) {
		if (isLoaded() != pValue)
			throw new RuntimeException(getClass().getSimpleName() + " loaded != " + pValue);
	}
}

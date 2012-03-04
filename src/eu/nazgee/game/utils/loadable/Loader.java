package eu.nazgee.game.utils.loadable;

import java.util.LinkedList;

import org.andengine.engine.Engine;

import eu.nazgee.game.utils.misc.Reversed;

import android.content.Context;

public class Loader implements ILoadable {
	// ===========================================================
	// Fields
	// ===========================================================
	private ILoadingHandlerResource mLoadingHandler;
	protected LinkedList<ILoadable> mResources = new LinkedList<ILoadable>();
	protected Boolean mLoaded = new Boolean(false);
	// ===========================================================
	// Constructors
	// ===========================================================
	public Loader(ILoadingHandlerResource pLoadingHandler) {
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
	public void load(Engine e, Context c) {
		synchronized (mLoaded) {
			assertLoaded(false);
			for (ILoadable res : mResources) {
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

			Reversed<ILoadable> rev = new Reversed<ILoadable>(mResources);
			for (ILoadable res : rev) {
				res.unload();
			}
			setLoaded(false);
		}
	}

	@Override
	public boolean isLoaded() {
		return mLoaded.booleanValue();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	protected void setLoaded(boolean pValue) {
		mLoaded = pValue;
	}

	protected void assertLoaded(boolean pValue) {
		if (isLoaded() != pValue)
			throw new RuntimeException(getClass().getSimpleName() + " loaded != " + pValue);
	}
}

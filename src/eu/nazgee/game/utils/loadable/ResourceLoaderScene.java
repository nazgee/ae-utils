package eu.nazgee.game.utils.loadable;

import org.andengine.entity.scene.Scene;

public class ResourceLoaderScene extends ResourceLoader implements ILoadableResourceScene {

	private final IResourceLoadingHandlerScene mLoadingHandler;
	public ResourceLoaderScene(IResourceLoadingHandlerScene pLoadingHandler) {
		super(pLoadingHandler);
		mLoadingHandler = pLoadingHandler;
	}
	// ===========================================================
	// Fields
	// ===========================================================
	// ===========================================================
	// Constructors
	// ===========================================================
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@Override
	public Scene getScene() {
		synchronized (mLoaded) {
			assertLoaded(true);
			return mLoadingHandler.onGetScene();
		}
	}
}

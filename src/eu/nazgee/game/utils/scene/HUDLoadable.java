package eu.nazgee.game.utils.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;
import eu.nazgee.game.utils.loadable.LoaderResourceScene;
import eu.nazgee.game.utils.loadable.ILoadingHandlerResourceScene;

abstract public class HUDLoadable extends HUD implements ILoadableResourceScene, ILoadingHandlerResourceScene {
	float mW, mH;
	final private LoaderResourceScene mLoader = new LoaderResourceScene(this);
	private VertexBufferObjectManager mVertexBufferObjectManager;

	public HUDLoadable(final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pVertexBufferObjectManager);
	}

	public HUDLoadable(float W, float H, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super();
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mW = W;
		mH = H;
	}
	protected LoaderResourceScene getLoader() {
		return mLoader;
	}
	/*=========================================================================
	 * 							from ILoadableResourceScene
	 *=======================================================================*/
	@Override
	public Scene getScene() {
		return mLoader.getScene();
	}

	@Override
	final public void load(final Engine e, Context c) {
		setVertexBufferObjectManager(e.getVertexBufferObjectManager());
		mLoader.load(e, c);
	}

	@Override
	final public void unload() {
		mLoader.unload();
	}

	@Override
	final public void loadResources(Engine e, Context c) {
		mLoader.loadResources(e, c);
	}

	@Override
	final public boolean isLoaded() {
		return mLoader.isLoaded();
	}

	@Override
	final public boolean isResourceLoaded() {
		return mLoader.isResourceLoaded();
	}

	@Override
	public Scene onGetScene() {
		return this;
	}
	/*=========================================================================
	 * 							getters & setters
	 *=======================================================================*/
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return mVertexBufferObjectManager;
	}

	public void setVertexBufferObjectManager(VertexBufferObjectManager pVertexBufferObjectManager) {
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	public float getH() {
		return mH;
	}

	public float getW() {
		return mW;
	}
}

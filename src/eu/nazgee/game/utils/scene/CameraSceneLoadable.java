package eu.nazgee.game.utils.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;
import eu.nazgee.game.utils.loadable.ILoadingHandlerResourceScene;
import eu.nazgee.game.utils.loadable.LoaderResourceScene;

abstract public class CameraSceneLoadable extends CameraScene implements ILoadableResourceScene, ILoadingHandlerResourceScene {
	private float mW, mH;
	final private LoaderResourceScene mLoader = new LoaderResourceScene(this);
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public CameraSceneLoadable(Camera pCamera, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pCamera, 0, 0, pVertexBufferObjectManager);
	}

	public CameraSceneLoadable(Camera pCamera, float W, float H, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCamera);
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mW = W;
		mH = H;
	}
	public LoaderResourceScene getLoader() {
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

	public float getH() {
		return mH;
	}

	public float getW() {
		return mW;
	}
}

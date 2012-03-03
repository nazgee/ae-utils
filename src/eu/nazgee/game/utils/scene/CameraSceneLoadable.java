package eu.nazgee.game.utils.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;

abstract public class CameraSceneLoadable extends CameraScene implements ILoadableResourceScene {
	private float mW, mH;
	private Boolean mLoaded = new Boolean(false);
	private boolean mResourcesLoaded = false;
	private static Boolean mStaticResourcesLoaded = new Boolean(false);
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

	/*=========================================================================
	 * 							from ISceneLoadable
	 *=======================================================================*/
	@Override
	public Scene getScene() {
		synchronized (mLoaded) {
			assertLoaded(true);
			return this;
		}
	}

	@Override
	public void load(final Engine e, Context c) {
		synchronized (mLoaded) {
			assertLoaded(false);
			setLoaded(true);
		}
	}

	@Override
	public void unload() {
		synchronized (mLoaded) {
			assertLoaded(true);

			clearUpdateHandlers();
			reset();
			setLoaded(false);
		}
	}

	@Override
	public void loadResources(Engine e, Context c) {
		synchronized (mStaticResourcesLoaded) {
			if (mStaticResourcesLoaded.booleanValue() == false) {
				loadResourcesOnceStatic(e, c);
				mStaticResourcesLoaded = true;
			}
		}
		if (!isResourceLoaded()) {
			loadResourcesOnce(e, c);
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

	// XXX after we quit the game once, this one never gets called :(
	abstract protected void loadResourcesOnceStatic(Engine e, Context c);
	abstract protected void loadResourcesOnce(Engine e, Context c);
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

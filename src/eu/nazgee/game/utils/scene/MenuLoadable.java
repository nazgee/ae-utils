package eu.nazgee.game.utils.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;

abstract public class MenuLoadable extends MenuScene implements ILoadableResourceScene {
	float mW, mH;
	private Boolean mLoaded = new Boolean(false);
	boolean mResourcesLoaded = false;
	private static Boolean mStaticResourcesLoaded = new Boolean(false);
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public MenuLoadable(Camera pCamera, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pCamera, pVertexBufferObjectManager);
	}

	public MenuLoadable(float W, float H, Camera pCamera, final VertexBufferObjectManager pVertexBufferObjectManager) {
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

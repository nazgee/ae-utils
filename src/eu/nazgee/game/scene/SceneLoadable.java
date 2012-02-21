package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.misc.ILoadableResourceScene;

abstract public class SceneLoadable extends Scene implements ILoadableResourceScene {
	private float mW, mH;
	private Boolean mLoaded = new Boolean(false);
	private boolean mResourcesLoaded = false;
	private static Boolean mStaticResourcesLoaded = new Boolean(false);
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public SceneLoadable(final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pVertexBufferObjectManager);
	}

	public SceneLoadable(float W, float H, final VertexBufferObjectManager pVertexBufferObjectManager) {
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
			if (!mLoaded.booleanValue())
				throw new RuntimeException(getClass().getSimpleName() + "instance aquired without prior loading!");

			return this;
		}
	}

	@Override
	public void load(final Engine e, Context c) {
		synchronized (mLoaded) {
			if (mLoaded.booleanValue())
				throw new RuntimeException(getClass().getSimpleName() + "instance double loaded!");

			mLoaded = true;
		}
	}

	@Override
	public void unload() {
		synchronized (mLoaded) {
			if (!mLoaded.booleanValue())
				throw new RuntimeException(getClass().getSimpleName() + "instance double unloaded (or not loaded at all)!");

			clearUpdateHandlers();
			reset();
			mLoaded = false;
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
		if (!mResourcesLoaded) {
			loadResourcesOnce(e, c);
			mResourcesLoaded = true;
		}
	}
	// XXX after we quit the game once, this one never gets called :(
	abstract protected void loadResourcesOnceStatic(Engine e, Context c);
	abstract protected void loadResourcesOnce(Engine e, Context c);

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

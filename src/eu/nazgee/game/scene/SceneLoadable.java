package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.util.Log;

abstract public class SceneLoadable extends Scene implements ISceneLoadable {
	private float mW, mH;
	private boolean mLoaded = false;
	private static Boolean mLoadedStatic = new Boolean(false);
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public SceneLoadable(final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, pVertexBufferObjectManager);
	}

	public SceneLoadable(float W, float H, final VertexBufferObjectManager pVertexBufferObjectManager) {
		mVertexBufferObjectManager = pVertexBufferObjectManager;
		mW = W;
		mH = H;
	}
	public float getH() {
		return mH;
	}

	public float getW() {
		return mW;
	}

	@Override
	public void loadResources(Engine e, Context c) {
		synchronized (mLoadedStatic) {
			if (mLoadedStatic.booleanValue() == false) {
				loadResourcesOnceStatic(e, c);
				mLoadedStatic = true;
			}
		}
		if (!mLoaded) {
			loadResourcesOnce(e, c);
			mLoaded = true;
		}
	}

	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return mVertexBufferObjectManager;
	}

	abstract public void loadResourcesOnceStatic(Engine e, Context c);
}

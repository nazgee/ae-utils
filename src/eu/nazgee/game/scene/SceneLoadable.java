package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

abstract public class SceneLoadable extends Scene implements ISceneLoadable {
	float mW, mH;
	boolean mLoaded = false;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

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
		if (!mLoaded) {
			loadResourcesOnce(e, c);
			mLoaded = true;
		}
	}

	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return mVertexBufferObjectManager;
	}

	abstract public void loadResourcesOnce(Engine e, Context c);
}

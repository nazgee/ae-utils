package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.misc.ISceneLoadable;

abstract public class SceneMenuLoadable extends MenuScene implements ISceneLoadable {
	float mW, mH;
	boolean mLoaded = false;
	private final VertexBufferObjectManager mVertexBufferObjectManager;

	public SceneMenuLoadable(float W, float H, Camera pCamera, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCamera);
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
		reset();
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

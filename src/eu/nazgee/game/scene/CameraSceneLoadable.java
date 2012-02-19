package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.misc.ISceneLoadable;

abstract public class CameraSceneLoadable extends CameraScene implements ISceneLoadable {
	private float mW, mH;
	private boolean mLoaded = false;
	private static Boolean mLoadedStatic = new Boolean(false);
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

	abstract protected void loadResourcesOnceStatic(Engine e, Context c);
	abstract protected void loadResourcesOnce(Engine e, Context c);
}

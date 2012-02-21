package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.misc.ILoadableResources;

abstract public class MenuLoadable extends MenuScene implements ILoadableResources {
	float mW, mH;
	boolean mLoaded = false;
	private static Boolean mLoadedStatic = new Boolean(false);
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
	public Scene load(final Engine e, Context c) {
		return this;
	}

	@Override
	public void unload() {
		clearUpdateHandlers();
		reset();
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

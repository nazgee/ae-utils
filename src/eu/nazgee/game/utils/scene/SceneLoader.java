package eu.nazgee.game.utils.scene;

import java.lang.ref.WeakReference;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;
import eu.nazgee.game.utils.tasklet.IAsyncTasklet;
import eu.nazgee.game.utils.tasklet.TaskletsRunner;

public class SceneLoader {
	private ILoadableResourceScene mLoadingScene;
	private boolean mShowLoadingScene = true;
	private boolean mUnloadLoadingScene = true;

	/**
	 *
	 * @param pLoadingLoader "Loading..." scene to be used with this SceneLoader
	 * @param e
	 * @param c
	 */
	public SceneLoader(ILoadableResourceScene pLoadingLoader, final Engine e, final Activity c) {
		super();
		mLoadingScene = pLoadingLoader;
	}

	/**
	 * Modifies the behavior of {@link SceneLoader#loadScene(ILoadableResourceScene, Engine, Activity, ISceneLoaderListener)}
	 *
	 * @param pShow if true, loadScene will cause showing of "Loading..." scene;
	 * if false, loadScene will not show "Loading..." scene
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setLoadingSceneShow(boolean pShow) {
		mShowLoadingScene = pShow;
		return this;
	}

	/**
	 * Modifies the behavior of {@link SceneLoader#loadScene(ILoadableResourceScene, Engine, Activity, ISceneLoaderListener)}
	 *
	 * @param pUnload if true, loadScene will cause unloading of "Loading..." scene after requested scene is loaded;
	 * if false, loadScene will unload "Loading..." scene after requested scene is loaded
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setLoadingSceneUnload(boolean pUnload) {
		mUnloadLoadingScene = pUnload;
		return this;
	}

	/**
	 * Starts the process of loading pScene in the background. Before loading
	 * process begins, current scene will be unloaded
	 * pListener will be called when pScene gets loaded and is about to show.
	 *
	 * @param pScene Scene to be loaded in background
	 * @param e
	 * @param c
	 * @param pListener will be called when loading is finished
	 */
	public void loadScene(final ILoadableResourceScene pScene, final Engine e, final Activity c, ISceneLoaderListener pListener) {
		this.loadScene(pScene, e, c, pListener, mShowLoadingScene, mUnloadLoadingScene);
	}

	/**
	 * Starts the process of loading pScene in the background. Before loading 
	 * process begins, current scene will be unloaded. "Loading..." scene will be shown if
	 * pShowLoadingScene is set to true.
	 * pListener will be called when pScene will be loaded and about to show.
	 * If pUnloadLoadingScene is set to true, "Loading..." scene will get unloaded after loading process is finished.
	 *
	 * @param pScene Scene to be loaded in backround
	 * @param e
	 * @param c
	 * @param pListener will be called when loading is finished
	 * @param pShowLoadingScene if true, "Loading..." scene will be shown before loading process begins
	 * @param pUnloadLoadingScene if true, "Loading..." scene will be unloaded
	 * after loading process is finished. Setting it to false might be useful
	 * when the same "Loading..." scene is small (resource-wise), but used many
	 * times- in this case loading/unloading might be considered as a waste of time
	 */
	private void loadScene(final ILoadableResourceScene pScene, final Engine e, final Activity c, ISceneLoaderListener pListener, boolean pShowLoadingScene, boolean pUnloadLoadingScene) {
		// Make sure that current scene is unloaded, so we do not leak resources
		Scene current = e.getScene();
		if (current != mLoadingScene && current instanceof ILoadableResourceScene) {
			ILoadableResourceScene loaded = (ILoadableResourceScene) current;
			if (loaded.isLoaded()) {
				loaded.unload();
			}
		}

		// Make sure "Loading..." scene is ready to be used
		if (!mLoadingScene.isLoaded()) {
			mLoadingScene.loadResources(e, c);
			mLoadingScene.load(e, c);
		}

		// Set "Loading..." scene as current if requested by user and
		// not being currently shown
		if (current != mLoadingScene && pShowLoadingScene) {
			e.setScene(mLoadingScene.getScene());
		}

		// Start loading process in the background
		final SceneLoaderTasklet loader = new SceneLoaderTasklet(e, c, (SceneLoadable) mLoadingScene.getScene(), pScene, pListener, pUnloadLoadingScene);
		c.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new TaskletsRunner(loader).execute(loader);
			}
		});
	}

	public SceneLoadable getLoadingScene() {
		return (SceneLoadable) mLoadingScene.getScene();
	}

	public void setLoadingScene(final SceneLoadable pLoadingScene) {
		mLoadingScene = pLoadingScene;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface ISceneLoaderListener {
		void onSceneLoaded(Scene pScene);
	}

	/*
	 * Here's where the assets are loaded in the background behind the loading scene.
	 */
	private class SceneLoaderTasklet implements IAsyncTasklet {
		WeakReference<Engine> mEngine; 
		WeakReference<Context> mContext;

		private final ISceneLoaderListener mListener;
		private final SceneLoadable mPleaseWaitScene;
		private final ILoadableResourceScene mLoaderToBeLoaded;
		private volatile Scene mLoadedScene;
		private final boolean mUnloadPleaseWaitScene;

		/**
		 * Used to load a scene in the background, showing loading scene in the meanwhile
		 * @param e
		 * @param c
		 * @param pPleaseWaitScene will be unloaded, after pToBeLoaded will be loaded
		 * @param pToBeLoaded scene that should be loaded
		 * @param pUnloadPleaseWaitScene if true, pPleaseWaitScene will be unloaded after pToBeLoaded is loaded
		 */
		public SceneLoaderTasklet(final Engine e, final Context c, SceneLoadable pPleaseWaitScene, ILoadableResourceScene pToBeLoaded, ISceneLoaderListener pListener, boolean pUnloadPleaseWaitScene) {
			mEngine = new WeakReference<Engine>(e);
			mContext = new WeakReference<Context>(c);

			mListener = pListener;
			mPleaseWaitScene = pPleaseWaitScene;
			mLoaderToBeLoaded = pToBeLoaded;
			mUnloadPleaseWaitScene = pUnloadPleaseWaitScene;
		}

		@Override
		public void onAboutToStart() {
			// mEngine.get().setScene(mPleaseWaitScene);
		}

		@Override
		public void workToDo() {
			mLoaderToBeLoaded.loadResources(mEngine.get(), mContext.get());
			mLoaderToBeLoaded.load(mEngine.get(), mContext.get());
			mLoadedScene = mLoaderToBeLoaded.getScene();
		}
		@Override
		public void onComplete() {
			if (mPleaseWaitScene instanceof SceneSplash) {
				Log.d(getClass().getSimpleName(), "SceneSplash loaded, waiting for end");
				final SceneSplash splash = (SceneSplash) mPleaseWaitScene;

				splash.registerUpdateHandler(new IUpdateHandler() {
					@Override
					public void reset() {
					}
					@Override
					public void onUpdate(float pSecondsElapsed) {
						if (splash.isComplete()) {
							splash.waitForCompleted();
							if (mListener != null) {
								mListener.onSceneLoaded(mLoadedScene);
							}
							splash.unload();
							mEngine.get().setScene(mLoadedScene);
						}
					}
				});
			} else {
				Log.d(getClass().getSimpleName(), "Scene loaded");
				if (mListener != null) {
					Log.d(getClass().getSimpleName(), "Calling listener");
					mListener.onSceneLoaded(mLoadedScene);
				}
				if (mUnloadPleaseWaitScene) {
					mPleaseWaitScene.unload();
				}
				Log.d(getClass().getSimpleName(), "Setting loaded scene as active");
				mEngine.get().setScene(mLoadedScene);
			}
		}
	}
}

package eu.nazgee.game.scene;

import java.lang.ref.WeakReference;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

import android.app.Activity;
import android.content.Context;
import eu.nazgee.game.misc.ILoadableResources;
import eu.nazgee.game.ui.activity.IAsyncTasklet;
import eu.nazgee.game.ui.activity.TaskletsRunner;

public class SceneLoader {
	private SceneLoadable mLoadingScene;
	
	public SceneLoader(SceneLoadable pLoadingLoader, final Engine e, final Activity c) {
		super();
		mLoadingScene = pLoadingLoader;
		mLoadingScene.loadResources(e, c);
		mLoadingScene.load(e, c);
	}

	public void loadScene(final ILoadableResources pScene, final Engine e, final Activity c, ISceneLoaderListener pListener) {
		final SceneLoaderTasklet loader = new SceneLoaderTasklet(e, c, mLoadingScene, pScene, pListener);
		c.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new TaskletsRunner(loader).execute(loader);
			}
		});
	}

	public SceneLoadable getLoadingScene() {
		return mLoadingScene;
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
		private final ILoadableResources mLoaderToBeLoaded;
		private volatile Scene mLoadedScene;

		/**
		 * Used to load a scene in the background, showing loading scene in the meanwhile
		 * @param e
		 * @param c
		 * @param pPleaseWaitScene will be unloaded, after pToBeLoaded will be loaded
		 * @param pToBeLoaded scene that should be loaded
		 */
		public SceneLoaderTasklet(final Engine e, final Context c, SceneLoadable pPleaseWaitScene, ILoadableResources pToBeLoaded, ISceneLoaderListener pListener) {
			mEngine = new WeakReference<Engine>(e);
			mContext = new WeakReference<Context>(c);

			mListener = pListener;
			mPleaseWaitScene = pPleaseWaitScene;
			mLoaderToBeLoaded = pToBeLoaded;
		}

		@Override
		public void onAboutToStart() {
			mEngine.get().setScene(mPleaseWaitScene);
		}

		@Override
		public void workToDo() {
			mLoaderToBeLoaded.loadResources(mEngine.get(), mContext.get());
			mLoadedScene = mLoaderToBeLoaded.load(mEngine.get(), mContext.get());
		}
		@Override
		public void onComplete() {
			if (mPleaseWaitScene instanceof SceneSplash) {
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
				if (mListener != null) {
					mListener.onSceneLoaded(mLoadedScene);
				}
				mEngine.get().setScene(mLoadedScene);
			}
		}
	}
}

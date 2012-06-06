package eu.nazgee.game.utils.scene;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;
import eu.nazgee.game.utils.misc.Reversed;
import eu.nazgee.game.utils.tasklet.IAsyncTasklet;
import eu.nazgee.game.utils.tasklet.TaskletsRunner;

public class SceneLoader {
	public enum eLoadingSceneHandling {
		SCENE_SET_CHILD,
		SCENE_SET_ACTIVE,
		SCENE_DONT_TOUCH
	}

	public enum eNewSceneHandling {
		SCENE_SET_CHILD,
		SCENE_SET_CHILD_NESTED,
		SCENE_SET_ACTIVE,
	}

	public enum eOldSceneHandling {
		UNLOAD_OLD_AFTER_LOADING_NEW,
		UNLOAD_OLD_BEFORE_LOADING_NEW
	}

	private ILoadableResourceScene mLoadingScene;

	private eLoadingSceneHandling mLoadingSceneHandling = eLoadingSceneHandling.SCENE_SET_ACTIVE;
	private eOldSceneHandling mOldSceneHandling = eOldSceneHandling.UNLOAD_OLD_BEFORE_LOADING_NEW;

	private boolean mUnloadLoadingScene = true;

	private boolean mChildSceneModalUpdate = true;
	private boolean mChildSceneModalDraw = true;
	private boolean mChildSceneModalTouch = true;

	/**
	 *
	 * @param pLoadingLoader "Loading..." scene to be used with this SceneLoader
	 * @param e
	 * @param c
	 */
	public SceneLoader(ILoadableResourceScene pLoadingLoader) {
		super();
		mLoadingScene = pLoadingLoader;
	}

	public boolean isChildSceneModalUpdate() {
		return mChildSceneModalUpdate;
	}
	public boolean isChildSceneModalTouch() {
		return mChildSceneModalTouch;
	}
	public boolean isChildSceneModalDraw() {
		return mChildSceneModalDraw;
	}
	public eLoadingSceneHandling getShowLoadingScene() {
		return mLoadingSceneHandling;
	}
	public boolean isUnloadLoadingScene() {
		return mUnloadLoadingScene;
	}
	public eOldSceneHandling getOldSceneHandling() {
		return mOldSceneHandling;
	}

	/**
	 * Modifies the behavior of {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)} and {@link SceneLoader#loadScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)}
	 *
	 * @param mOldSceneHandling
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setOldSceneHandling(eOldSceneHandling mOldSceneHandling) {
		this.mOldSceneHandling = mOldSceneHandling;
		return this;
	}

	/**
	 * Modifies the behavior of {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)}
	 *
	 * @param pModalDraw will be passed as a first argument to {@link Scene#setChildScene(Scene, boolean, boolean, boolean)}
	 * when {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)} will be called
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setChildSceneModalDraw(boolean pModalDraw) {
		this.mChildSceneModalDraw = pModalDraw;
		return this;
	}
	/**
	 * Modifies the behavior of {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)}
	 *
	 * @param pModalTouch will be passed as a second argument to {@link Scene#setChildScene(Scene, boolean, boolean, boolean)}
	 * when {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)} will be called
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setChildSceneModalUpdate(boolean pModalTouch) {
		this.mChildSceneModalUpdate = pModalTouch;
		return this;
	}
	/**
	 * Modifies the behavior of {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)}
	 *
	 * @param pModalTouch will be passed as a third argument to {@link Scene#setChildScene(Scene, boolean, boolean, boolean)}
	 * when {@link SceneLoader#loadChildScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)} will be called
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setChildSceneModalTouch(boolean pModalTouch) {
		this.mChildSceneModalTouch = pModalTouch;
		return this;
	}

	/**
	 * Modifies the behavior of {@link SceneLoader#loadScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)}
	 *
	 * @param pShow
	 * @return instance of self for calls chaining
	 */
	public SceneLoader setLoadingSceneHandling(eLoadingSceneHandling pShow) {
		mLoadingSceneHandling = pShow;
		return this;
	}

	/**
	 * Modifies the behavior of {@link SceneLoader#loadScene(ILoadableResourceScene, Engine, Context, ISceneLoaderListener)}
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
	 * Starts the background process of loading pScene as a current scene.
	 * Current scene will be unloaded, depending on settings of {@link SceneLoader#setOldSceneHandling(eOldSceneHandling)}
	 * pListener will be called when pScene gets loaded and is about to show.
	 *
	 * @param pScene Scene to be loaded in background
	 * @param e
	 * @param c
	 * @param pListener will be called when loading is finished
	 */
	public void loadScene(final ILoadableResourceScene pScene, final Engine e, final Context c, ISceneLoaderListener pListener) {
		this.loadScene(pScene, eNewSceneHandling.SCENE_SET_ACTIVE, e, c, pListener, mLoadingSceneHandling, mUnloadLoadingScene);
	}

	/**
	 * Starts the background process of loading pScene as a child scene of current scene.
	 * Current scene will be unloaded, depending on settings of {@link SceneLoader#setOldSceneHandling(eOldSceneHandling)}
	 * pListener will be called when pScene gets loaded and is about to show.
	 *
	 * @param pScene Scene to be loaded in background
	 * @param e
	 * @param c
	 * @param pListener will be called when loading is finished
	 */
	public void loadChildScene(final ILoadableResourceScene pScene, final Engine e, final Context c, ISceneLoaderListener pListener) {
		this.loadScene(pScene, eNewSceneHandling.SCENE_SET_CHILD, e, c, pListener, mLoadingSceneHandling, mUnloadLoadingScene);
	}

	/**
	 * Starts the background process of loading pScene as a child scene of current scene.
	 * Current scene nor child scene will NOT be unloaded, regardless of settings of {@link SceneLoader#setOldSceneHandling(eOldSceneHandling)}
	 * pListener will be called when pScene gets loaded and is about to show.
	 *
	 * @param pScene Scene to be loaded in background
	 * @param e
	 * @param c
	 * @param pListener will be called when loading is finished
	 */
	public void loadChildSceneNested(final ILoadableResourceScene pScene, final Engine e, final Context c, ISceneLoaderListener pListener) {
		this.loadScene(pScene, eNewSceneHandling.SCENE_SET_CHILD_NESTED, e, c, pListener, mLoadingSceneHandling, mUnloadLoadingScene);
	}

	/**
	 * Unloads every child scene of the given pScene if it is an instance of ILoadableResourceScene
	 * @param pScene
	 * @note pScene.back() WILL be called for you- do not do it yourself!
	 */
	public void unloadEveryYoungerSceneCallBack(final Scene pScene) {

		LinkedList<Scene> children = new LinkedList<Scene>();

		// find the youngest child
		Scene scene = pScene;
		while (scene.getChildScene() != null) {
			scene = scene.getChildScene();
			children.add(scene);
		}

		for (Iterator<Scene> iterator = new Reversed<Scene>(children).iterator(); iterator.hasNext();) {
			Scene child = (Scene) iterator.next();
			child.back();
			tryUnloadSafe(child);
		}
	}

	/**
	 * Unloads every child scene of the given pScene if it is an instance of ILoadableResourceScene.
	 * pScene also gets unloaded.
	 * @param pScene
	 * @note pScene.back() WILL be called for you- do not do it yourself!
	 */
	public void unloadEveryYoungerSceneWithGivenCallBack(final Scene pScene) {
		unloadEveryYoungerSceneCallBack(pScene);
		tryUnloadSafe(pScene);
		pScene.back();
	}

	/**
	 * Unloads only the youngest child scene of the given pScene if it is an instance of ILoadableResourceScene
	 * @param pScene
	 * @return parent of the unloaded child scene
	 * @note pScene.back() WILL be called for you- do not do it yourself!
	 */
	public static Scene unloadYoungestChildSceneCallBack(final Scene pScene) {

		SceneFamily family = findYoungestChild(pScene);
		if (family.child != pScene)
		{
			family.child.back();

			ILoadableResourceScene loadableChild = (ILoadableResourceScene) family.child;
			if (loadableChild.isLoaded()) {
				loadableChild.unload();
			} else {
				new RuntimeException("Oops! Unloaded scene was set as one of the children!");
			}
		}
		return family.parent;
	}

	/**
	 * Starts the process of loading pScene in the background. Before loading 
	 * process begins, current scene will be unloaded. "Loading..." scene will be shown if
	 * pLoadingSceneHandling is set to true.
	 * pListener will be called when pScene will be loaded and about to show.
	 * If pUnloadLoadingScene is set to true, "Loading..." scene will get unloaded after loading process is finished.
	 *
	 * @param pScene Scene to be loaded in background
	 * @param e
	 * @param c
	 * @param pListener will be called when loading is finished
	 * @param pLoadingSceneHandling describes how "Loading..." scene will be displayed
	 * @param pUnloadLoadingScene if true, "Loading..." scene will be unloaded
	 * after loading process is finished. Setting it to false might be useful
	 * when the same "Loading..." scene is small (resource-wise), but used many
	 * times- in this case loading/unloading might be considered as a waste of time
	 */
	private void loadScene(final ILoadableResourceScene pScene, final eNewSceneHandling sceneHandling, final Engine e, final Context c, ISceneLoaderListener pListener, eLoadingSceneHandling pLoadingSceneHandling, boolean pUnloadLoadingScene) {
		Scene oldScene = e.getScene();

		switch (sceneHandling) {
		case SCENE_SET_ACTIVE:
			if (	mOldSceneHandling == eOldSceneHandling.UNLOAD_OLD_BEFORE_LOADING_NEW) {
				unloadIfNotSplashscreen(oldScene);
			}
			break;
		case SCENE_SET_CHILD:
			if (mOldSceneHandling == eOldSceneHandling.UNLOAD_OLD_BEFORE_LOADING_NEW) {
				unloadIfNotSplashscreen(oldScene.getChildScene());
			}
			break;
		case SCENE_SET_CHILD_NESTED:
			/* we are not unloadint anything in this case */
			break;
		default:
			break;
		}

		prepareForLoading(e, c, pLoadingSceneHandling);

		// Start loading process in the background
		final SceneLoaderTasklet loader = new SceneLoaderTasklet(e, c, 
				(SceneLoadable) mLoadingScene.getScene(), pScene, pListener, 
				pUnloadLoadingScene, oldScene, new SetupHandler() {
			@Override
			public void setupScene(Engine e, Scene pNewScene, Scene pOldScene) {
				switch (sceneHandling) {
				case SCENE_SET_ACTIVE:
					e.setScene(pNewScene);
					break;
				case SCENE_SET_CHILD:
					e.setScene(pOldScene);
					pOldScene.setChildScene(pNewScene, mChildSceneModalDraw, mChildSceneModalUpdate, mChildSceneModalTouch);
					break;
				case SCENE_SET_CHILD_NESTED:
					// set new scene as a child of last child of pOldScene
					e.setScene(pOldScene);
					SceneFamily family = findYoungestChild(pOldScene);
					if (family.child == mLoadingScene) {
						Log.d(getClass().getSimpleName(), "Gently removing Loading... scene of the stack");
						family.child.back();
					}

					family.parent.setChildScene(pNewScene, mChildSceneModalDraw, mChildSceneModalUpdate, mChildSceneModalTouch);
					break;
				default:
					break;
				}
			}

			@Override
			public void unloadOldScene(Scene pOldScene) {
				switch (sceneHandling) {
				case SCENE_SET_ACTIVE:
					if (mOldSceneHandling == eOldSceneHandling.UNLOAD_OLD_AFTER_LOADING_NEW) {
						tryUnloadSafe(pOldScene);
					}
					break;
				case SCENE_SET_CHILD:
					if (mOldSceneHandling == eOldSceneHandling.UNLOAD_OLD_AFTER_LOADING_NEW) {
						tryUnloadSafe(pOldScene.getChildScene());
					}
					break;
				case SCENE_SET_CHILD_NESTED:
					/* we are not unloadint anything in this case */
					break;
				default:
					break;
				}
			}
		});

		if (c instanceof Activity) {
			Activity act = (Activity) c;
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					new TaskletsRunner(loader).execute(loader);
				}
			});
		} else {
			Log.w(getClass().getSimpleName(), "this might be risky- not calling execute from UI thread!");
			new TaskletsRunner(loader).execute(loader);
		}
	}

	private void prepareForLoading(final Engine e, final Context c,
			eLoadingSceneHandling pLoadingSceneHandling) {
		Scene current = e.getScene();


		// Make sure "Loading..." scene is ready to be used
		if (!mLoadingScene.isLoaded()) {
			mLoadingScene.loadResources(e, c);
			mLoadingScene.load(e, c);
		}
		mLoadingScene.getScene().reset();

		// Set "Loading..." scene as current if requested by user and
		// not being currently shown
		switch (pLoadingSceneHandling) {
		case SCENE_SET_ACTIVE:
			if (current != mLoadingScene) {
				e.setScene(mLoadingScene.getScene());
			}
			break;
		case SCENE_SET_CHILD:
				Scene scene = e.getScene();
				if (scene != null) {
					SceneFamily familyMember = findYoungestChild(scene);
					if (!(familyMember.child instanceof MenuScene)) {
						familyMember.child.setChildScene(mLoadingScene.getScene(), false, false, true);
					} else {
						Log.w(getClass().getSimpleName(), "Loading... scene could not be shown- parent is menu scene!");
					}
				}
			break;
		default:
			break;
		}
	}

	private boolean unloadIfNotSplashscreen(Scene unloadScene) {
		if (unloadScene != mLoadingScene) { // splash screen case
			return tryUnloadSafe(unloadScene);
		}
		return false;
	}

	private boolean tryUnloadSafe(Scene unloadScene) {
		if (unloadScene instanceof ILoadableResourceScene) {
		ILoadableResourceScene loaded = (ILoadableResourceScene) unloadScene;
			if (loaded.isLoaded()) {
				loaded.unload();
				return true;
			}
		}
		return false;
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
		private final Scene mOldScene;
		private final SetupHandler mSetupHandler;

		/**
		 * Used to load a scene in the background, showing loading scene in the meanwhile
		 * @param e
		 * @param c
		 * @param pPleaseWaitScene will be unloaded, after pToBeLoaded will be loaded
		 * @param pToBeLoaded scene that should be loaded
		 * @param pUnloadPleaseWaitScene if true, pPleaseWaitScene will be unloaded after pToBeLoaded is loaded
		 */
		public SceneLoaderTasklet(final Engine e, final Context c,
				SceneLoadable pPleaseWaitScene, ILoadableResourceScene pToBeLoaded,
				ISceneLoaderListener pListener,
				boolean pUnloadPleaseWaitScene,
				final Scene pOldScene,
				final SetupHandler pSetupHandler) {
			mEngine = new WeakReference<Engine>(e);
			mContext = new WeakReference<Context>(c);

			mListener = pListener;
			mPleaseWaitScene = pPleaseWaitScene;
			mLoaderToBeLoaded = pToBeLoaded;
			mUnloadPleaseWaitScene = pUnloadPleaseWaitScene;
			mOldScene = pOldScene;
			mSetupHandler = pSetupHandler;
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
							setupScene();
							printScenesStack(mEngine.get().getScene());
						}
					}
				});
			} else {
				Log.d(getClass().getSimpleName(), "Scene loaded !!!");
				setupScene();
				printScenesStack(mEngine.get().getScene());
			}
		}

		private void printScenesStack(Scene pScene) {
			SceneFamily familyMember = new SceneFamily(pScene, pScene);

			String offset = "";
			while (familyMember.child.getChildScene() != null) {
				familyMember.parent = familyMember.child;
				familyMember.child = familyMember.parent.getChildScene();
				Log.d(getClass().getSimpleName(), offset + sceneToString(familyMember.parent) + "   (has a child: " + sceneToString(familyMember.child) + ")");
				offset += " ";
			}
			Log.d(getClass().getSimpleName(), offset + sceneToString(familyMember.child) + "   (no child)");
		}

		private String sceneToString(Scene pScene) {
			if (null != pScene) {
				return pScene.getClass().getSimpleName();
			} else {
				return "none";
			}
		}

		private void setupScene() {
			if (mUnloadPleaseWaitScene) {
				mPleaseWaitScene.unload();
			}

			Log.d(getClass().getSimpleName(), "Setting loaded scene as active");

			mSetupHandler.setupScene(mEngine.get(), mLoadedScene, mOldScene);
			mSetupHandler.unloadOldScene(mOldScene);

			if (mListener != null) {
				mLoadedScene.postRunnable(new Runnable() {
					@Override
					public void run() {
						Log.d(getClass().getSimpleName(), "calling onSceneLoaded()");
						mListener.onSceneLoaded(mLoadedScene);
					}
				});
			}
		}
	}

	private interface SetupHandler {
		void setupScene(Engine e, Scene pNewScene, Scene pOldScene);
		void unloadOldScene(Scene pOldScene);
	}

	/**
	 * Finds youngest child scene, starting from pBaseScene
	 * @param pBaseScene
	 * @return youngest child of given pBaseScene; pBaseScene if no children were found
	 */
	protected static SceneFamily findYoungestChild(Scene pBaseScene) {
		// find the youngest child
		SceneFamily ret = new SceneFamily(pBaseScene, pBaseScene);

		while (ret.child.getChildScene() != null) {
			ret.parent = ret.child;
			ret.child = ret.parent.getChildScene();
		}
		return ret;
	}

	public static Scene getYoungestScene(Scene pScene) {
		while (pScene.getChildScene() != null) {
			pScene = pScene.getChildScene();
		}
		return pScene;
	}

	static class SceneFamily {
		public Scene parent;
		public Scene child;
		public SceneFamily(Scene parent, Scene child) {
			super();
			this.parent = parent;
			this.child = child;
		}
	}
}

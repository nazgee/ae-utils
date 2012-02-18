package eu.nazgee.game.ui.activity;

import java.lang.ref.WeakReference;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Context;
import eu.nazgee.game.misc.ILoadable;
import eu.nazgee.game.scene.SceneSplash;


abstract public class ActivitySplash extends SimpleBaseGameActivity implements ILoadable {
	@Override
	public void onCreateResources() {
		SoundFactory.setAssetBasePath("mfx/");
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");
	}

	/*
	 * Here's where the assets are loaded in the background behind the loading scene.
	 */
	class Loader implements IAsyncTasklet {
		private volatile Scene mScene;
		WeakReference<Engine> mEngine; 
		WeakReference<Context> mContext;
		WeakReference<SceneSplash> mSplash;
		
		public Loader(final Engine e, final Context c, SceneSplash pSplash) {
			mEngine = new WeakReference<Engine>(e);
			mContext = new WeakReference<Context>(c);
			mSplash = new WeakReference<SceneSplash>(pSplash);
		}
		
		@Override
		public void workToDo() {
			loadResources(mEngine.get(), mContext.get());
			mScene = load(mEngine.get(), mContext.get());
		}
		@Override
		public void onComplete() {
			mSplash.get().registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {
				}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if (mSplash.get().isComplete()) {
						mSplash.get().waitForCompleted();
						mSplash.get().unload();
						mEngine.get().setScene(mScene);
					}
				}
			});
		}
	}

	public Scene onCreateScene(SceneSplash pSplash) {
		pSplash.load(getEngine(), this);
		final Loader loader = new Loader(getEngine(), this, pSplash);
		// We need to defer this executing TaskletRunner, since it needs running looper
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new TaskletsRunner().execute(loader);
			}
		});

		return pSplash;
	}
}

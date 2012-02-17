package eu.nazgee.game.ui.activity;

import java.lang.ref.WeakReference;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import android.content.Context;
import eu.nazgee.game.misc.ILoadable;

public class AsyncLoader implements IAsyncTasklet {
	
	private volatile Scene mScene;
	WeakReference<Engine> mEngine; 
	WeakReference<Context> mContext;
	ILoadable mLoadable;
	
	public AsyncLoader(final Engine e, final Context c, ILoadable pLoadable) {
		mEngine = new WeakReference<Engine>(e);
		mContext = new WeakReference<Context>(c);
		mLoadable = pLoadable;
	}

	@Override
	public void workToDo() {
		mLoadable.loadResources(mEngine.get(), mContext.get());
		mScene = mLoadable.load(mEngine.get(), mContext.get());
	}

	@Override
	public void onComplete() {
		mEngine.get().setScene(mScene);
	}

}

package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;

public interface ILoadingHandler {
	void onLoad(Engine e, Context c);
	void onUnload();
}

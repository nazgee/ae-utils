package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;

public interface IResourceLoadingHandler {
	void onLoadResources(Engine e, Context c);
	void onLoad(Engine e, Context c);
	void onUnload();
}

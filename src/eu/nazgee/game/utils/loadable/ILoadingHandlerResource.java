package eu.nazgee.game.utils.loadable;

import org.andengine.engine.Engine;

import android.content.Context;

public interface ILoadingHandlerResource extends ILoadingHandler{
	void onLoadResources(Engine e, Context c);
}

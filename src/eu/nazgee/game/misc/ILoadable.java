package eu.nazgee.game.misc;

import org.andengine.engine.Engine;

import android.content.Context;

public interface ILoadable {
	public void load(final Engine e, final Context c);
	public void unload();
}

package eu.nazgee.game.misc;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import android.content.Context;

public interface ILoadable {
	public Scene load(final Engine e, final Context c);
	public void unload();
}

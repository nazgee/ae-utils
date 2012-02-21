package eu.nazgee.game.misc;

import org.andengine.engine.Engine;

import android.content.Context;

public interface ISceneLoadable extends ILoadable {
	public void loadResources(final Engine e, final Context c);
//	public VertexBufferObjectManager getVertexBufferObjectManager();
}

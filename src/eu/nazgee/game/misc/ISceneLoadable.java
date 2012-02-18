package eu.nazgee.game.misc;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

public interface ISceneLoadable {
	public void loadResources(final Engine e, final Context c);
	public Scene load(final Engine e, final Context c);
	public void unload();
	public VertexBufferObjectManager getVertexBufferObjectManager();
}

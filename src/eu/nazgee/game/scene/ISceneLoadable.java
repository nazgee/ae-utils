package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

import eu.nazgee.game.misc.ILoadable;

public interface ISceneLoadable extends ILoadable {
	public VertexBufferObjectManager getVertexBufferObjectManager();
	public void loadResourcesOnce(Engine e, Context c);
}

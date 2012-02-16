package eu.nazgee.game.scene;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.game.misc.ILoadable;

public interface ISceneLoadable extends ILoadable {
	public VertexBufferObjectManager getVertexBufferObjectManager();
}

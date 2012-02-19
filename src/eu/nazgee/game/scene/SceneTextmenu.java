package eu.nazgee.game.scene;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;

abstract public class SceneTextmenu extends SceneMenuLoadable {

	private Font mFont;

	public SceneTextmenu(float W, float H, Camera pCamera, Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pVertexBufferObjectManager);
		mFont = pFont;
	}

	public void addMenuEntry(String pText, int pVal, final Color pSelected, final Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem = new ColorMenuItemDecorator(
				new TextMenuItem(pVal, getFont(), pText, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		addMenuItem(menuItem);
	}
	
	@Override
	public void loadResources(Engine e, Context c) {
		reset();
		super.loadResources(e, c);
	}

	public Font getFont() {
		return mFont;
	}
}

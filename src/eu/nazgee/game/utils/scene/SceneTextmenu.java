package eu.nazgee.game.utils.scene;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

abstract public class SceneTextmenu extends MenuLoadable {

	private Font mFont;

	public SceneTextmenu(float W, float H, Camera pCamera, Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pVertexBufferObjectManager);
		mFont = pFont;
	}

	/*=========================================================================
	 * 							getters & setters
	 *=======================================================================*/
	public Font getFont() {
		return mFont;
	}

	public void addMenuEntry(String pText, int pVal, final Color pSelected, final Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem = new ColorMenuItemDecorator(
				new TextMenuItem(pVal, getFont(), pText, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		addMenuItem(menuItem);
	}
}

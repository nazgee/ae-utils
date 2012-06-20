package eu.nazgee.game.utils.scene.menu;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.content.Context;

abstract public class SceneMenu extends MenuLoadable {

	private Font mFont;

	public SceneMenu(float W, float H, Camera pCamera, Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pVertexBufferObjectManager);
		mFont = pFont;
	}

	/*=========================================================================
	 * 							getters & setters
	 *=======================================================================*/
	public Font getFont() {
		return mFont;
	}

	public IMenuItem addMenuEntry(String pText, int pVal, final Color pSelected, final Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem = new ColorMenuItemDecorator(
				new TextMenuItem(pVal, getFont(), pText, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		addMenuItem(menuItem);
		return menuItem;
	}

	public IMenuItem addMenuEntry(float w, float h, ITextureRegion pTextureRegion, int pID, Color pSelected,
			Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem = new ColorMenuItemDecorator(
				new SpriteMenuItem(pID, w, h, pTextureRegion, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		addMenuItem(menuItem);
		return menuItem;
	}

	public IMenuItem addMenuEntry(ITextureRegion pTextureRegion, int pID, Color pSelected,
			Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem = new ColorMenuItemDecorator(
				new SpriteMenuItem(pID, pTextureRegion, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		addMenuItem(menuItem);
		return menuItem;
	}

	@Override
	public void onLoad(Engine e, Context c) {
		buildAnimations();
	}
}

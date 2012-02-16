package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import android.content.Context;

public class SceneLoading extends SceneLoadable {
	private Text mTextLoading;
	private final Font mFont;
	public SceneLoading(float W, float H, final Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		mFont = pFont;
		setBackground(new Background(0, 0, 0));
	}

	@Override
	public Scene load(Engine e, Context c) {
		mTextLoading.reset();
		return this;
	}

	@Override
	public void unload() {
	}

	@Override
	public void loadResourcesOnce(Engine e, Context c) {
		mTextLoading = new Text(getW() + 100, getH() / 2, mFont,
				"Loading...", getVertexBufferObjectManager());
		
		final float loadingHalfW = mTextLoading.getWidth() / 2;
		mTextLoading.registerEntityModifier(new SequenceEntityModifier(
				new MoveXModifier(0.3f, getW(), getW() / 2 - loadingHalfW, EaseExponentialOut.getInstance())));

		this.attachChild(mTextLoading);
	}
}

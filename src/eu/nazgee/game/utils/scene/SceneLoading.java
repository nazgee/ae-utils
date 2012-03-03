package eu.nazgee.game.utils.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import android.content.Context;

public class SceneLoading extends SceneLoadable {
	private Text mTextLoading;
	private final Font mFont;
	private IEntityModifier mMod;
	public SceneLoading(float W, float H, final Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		mFont = pFont;
		setBackground(new Background(0, 0, 0));
	}

	@Override
	public void load(Engine e, Context c) {
		mTextLoading.reset();
		mTextLoading.setPosition(getW(), getH() / 2);
		mMod.reset();
	}

	@Override
	public void unload() {
	}

	@Override
	public void loadResourcesOnce(Engine e, Context c) {
		mTextLoading = new Text(getW(), getH() / 2, mFont,
				"Loading...", getVertexBufferObjectManager());
		this.attachChild(mTextLoading);

		final float loadingHalfW = mTextLoading.getWidth() / 2;
		mMod = new SequenceEntityModifier(
				new MoveXModifier(0.5f, getW(), getW() / 2 - loadingHalfW, EaseExponentialOut.getInstance()));
		mTextLoading.registerEntityModifier(mMod);
	}

	@Override
	public void loadResourcesOnceStatic(Engine e, Context c) {
	}
}

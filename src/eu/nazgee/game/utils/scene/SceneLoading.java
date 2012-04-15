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
	private final String mLoading;
	public SceneLoading(float W, float H, final Font pFont, String pLoading, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		mFont = pFont;
		mLoading = pLoading;
		setBackground(new Background(0, 0, 0));
	}
	/*=========================================================================
	 * 							from IResourceLoadingHandlerScene
	 *=======================================================================*/
	@Override
	public void onLoadResources(Engine e, Context c) {
		mTextLoading = new Text(getW(), getH() / 2, mFont,
				mLoading, getVertexBufferObjectManager());
		this.attachChild(mTextLoading);

	}

	@Override
	public void onLoad(Engine e, Context c) {
		prepareText();
		prepareAnimation(mTextLoading);
	}
	

	@Override
	public void onUnload() {
	}

	private void prepareText() {
		mTextLoading.reset();
		mTextLoading.clearEntityModifiers();
		mTextLoading.clearUpdateHandlers();
	}

	@Override
	protected void prepareAnimation(Text pTextLoading) {
		pTextLoading.setPosition(getW(), getH() / 2);

		final float loadingHalfW = pTextLoading.getWidth() / 2;
		IEntityModifier mod = new SequenceEntityModifier(
				new MoveXModifier(0.5f, getW(), getW() / 2 - loadingHalfW, EaseExponentialOut.getInstance()));
		pTextLoading.registerEntityModifier(mod);
	}

	@Override
	public void reset() {
		if (isLoaded()) {
			prepareText();
			prepareAnimation(mTextLoading);
		}
		super.reset();
	}
}

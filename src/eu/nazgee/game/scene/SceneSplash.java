package eu.nazgee.game.scene;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialIn;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import android.content.Context;
import android.util.Log;

abstract public class SceneSplash extends SceneLoadable implements IOnSceneTouchListener, IOnAreaTouchListener {

	BitmapTextureAtlas mAtlasLogos;

	LinkedList<Sprite> mSprites = new LinkedList<Sprite>();
	private Text mTextLoading;
	private float mTotalTime;
	private volatile boolean mComplete = false;
	private final Font mFont;
	
	public SceneSplash(float W, float H, float totalTime, final Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		mTotalTime = totalTime;
		mFont = pFont;
		setBackground(new Background(0, 0, 0));
	}

	@Override
	public void loadResourcesOnce(Engine e, Context c) {
		mTextLoading = new Text(getW() + 100, getH() / 2, mFont, "Loading...", getVertexBufferObjectManager());
		e.getTextureManager().loadTexture(mAtlasLogos);
	}

	protected void addLoadingSprite(Sprite pSprite) {
		mSprites.add(pSprite);
	}

	@Override
	public Scene load(Engine e, Context c) {
		float logoTime = mTotalTime / (3 * mSprites.size() + 1) * 3;

		for (int i = 0; i < mSprites.size(); i++) {
			Sprite sprite = mSprites.get(i);

			float halfW = sprite.getWidth()/2;
			if (sprite != mSprites.getLast()) {
				// attach to the next sprite
				sprite.registerEntityModifier(new SequenceEntityModifier(
					new LogoAttacher(this, mSprites.get(i+1)), 
					new MoveXModifier(logoTime / 3, getW(), getW() / 2 - halfW, EaseExponentialOut.getInstance()), 
					new DelayModifier(logoTime / 3), 
					new MoveXModifier(logoTime / 3, getW() / 2 - halfW, -2 * halfW, EaseExponentialIn.getInstance())));
			} else {
				// attach to the loading text
				sprite.registerEntityModifier(new SequenceEntityModifier(
					new LogoAttacher(this, mTextLoading), 
					new MoveXModifier(logoTime / 3, getW(), getW() / 2 - halfW, EaseExponentialOut.getInstance()), 
					new DelayModifier(logoTime / 3), 
					new MoveXModifier(logoTime / 3, getW() / 2 - halfW, -2 * halfW, EaseExponentialIn.getInstance())));
			}
		}

		float halfW = mTextLoading.getWidth()/2;
		mTextLoading.registerEntityModifier(new SequenceEntityModifier(
				new IEntityModifierListener() {
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,	IEntity pItem) {
					}
					@Override
					public void onModifierFinished(	IModifier<IEntity> pModifier, IEntity pItem) {
						setComplete(true);
					}
				}, new MoveXModifier(logoTime / 4, getW(), getW() / 2 - halfW, EaseExponentialOut.getInstance())));

		if (mSprites.size() > 0) {
			attachChild(mSprites.getFirst());
		} else {
			attachChild(mTextLoading);
		}

		setOnSceneTouchListener(this);
		setOnAreaTouchListener(this);
		return this;
	}

	@Override
	public void unload() {
		detachChildren();
		reset();
	}

	protected void setComplete(boolean pComplete) {
		this.mComplete = pComplete;
	}

	/**
	 * Used to wait until splashscreen is gone
	 */
	public void waitForCompleted() {
		while (true) {
			if (mComplete) {
				return;
			}

			try {
				Thread.sleep(200, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Used to check if splashscreen is gone
	 * @return true when splashscreen is complete
	 */
	public boolean isComplete() {
		return mComplete;
	}

	class LogoAttacher implements IEntityModifierListener {
		private IEntity mEntity;
		private Scene mScene;

		public LogoAttacher(Scene pScene, IEntity mEntity) {
			this.mScene = pScene;
			this.mEntity = mEntity;
		}

		public LogoAttacher(Scene pScene) {
			this.mScene = pScene;
			this.mEntity = null;
		}

		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier,
				IEntity pItem) {
		}

		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier,
				IEntity pItem) {
			mScene.detachChild(pItem);
			if (mEntity != null) {
				mScene.attachChild(mEntity);
			}
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return true;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		Log.d("SceneSplash", "Someone is poking the screen while loading!");
		return true;
	}
}

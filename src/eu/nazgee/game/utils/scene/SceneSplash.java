package eu.nazgee.game.utils.scene;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialIn;
import org.andengine.util.modifier.ease.EaseExponentialOut;

import android.content.Context;
import android.util.Log;

public class SceneSplash extends SceneLoadable implements IOnSceneTouchListener, IOnAreaTouchListener {

	LinkedList<Sprite> mSprites = new LinkedList<Sprite>();
	private Text mTextLoading;
	private float mTotalTime;
	private volatile boolean mComplete = false;
	private final Font mFont;
	
	public SceneSplash(float W, float H, Color pColor, float totalTime, final Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		mTotalTime = totalTime;
		mFont = pFont;
		setBackground(new Background(pColor));
	}

	/*=========================================================================
	 * 							from IResourceLoadingHandlerScene
	 *=======================================================================*/
	@Override
	public void onLoadResources(Engine e, Context c) {
		mTextLoading = new Text(getW() + 100, getH() / 2, mFont, "Loading...", getVertexBufferObjectManager());
	}

	@Override
	public void onLoad(Engine e, Context c) {
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
	}

	@Override
	public void onUnload() {
		detachChildren();
	}
	/*=========================================================================
	 * 							getters & setters
	 *=======================================================================*/
	/**
	 * Used to check if splashscreen is finished
	 * @return true when splashscreen is complete
	 */
	public boolean isComplete() {
		return mComplete;
	}

	/**
	 * Waits until splashscreen is finished, then returns
	 */
	public void waitForCompleted() {
		Log.d(getClass().getSimpleName(), "Starting to wait for splash screen to finish");
		while (true) {
			if (isComplete()) {
				Log.d(getClass().getSimpleName(), "Splash screen finished");
				return;
			}
			
			try {
				Thread.sleep(200, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void setComplete(boolean pComplete) {
		Log.d(getClass().getSimpleName(), "Splash screen setComplete(" + pComplete + ")");
		this.mComplete = pComplete;
	}

	public void addSplashScreen(Sprite pSprite) {
		mSprites.add(pSprite);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		Log.d(getClass().getSimpleName(), "Someone is toching the screen while loading!");
		return true;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		Log.d(getClass().getSimpleName(), "Someone is poking the screen while loading!");
		return true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
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
}

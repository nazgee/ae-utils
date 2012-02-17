package eu.nazgee.game.ui.activity;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.physics.ITrack;
import eu.nazgee.game.scene.ScenePhysics;
import eu.nazgee.game.utils.UtilsMath;

abstract public class ActivityPhysicsSplash extends ActivitySplash {

	private ScenePhysics mSceneMain;
	private ITrack mGravityTrack;
	private Vector2 mGravityVector;
	
	public ActivityPhysicsSplash() {
		super();
		mGravityVector = Vector2Pool.obtain(0, 0);
	}
	
	protected void setMainScene(ScenePhysics pSceneMain) {
		mSceneMain = pSceneMain;
	}
	
	protected ScenePhysics getMainScene() {
		return mSceneMain;
	}

	public ITrack getGravityTranslationTrack() {
		return mGravityTrack;
	}

	protected void setGravityTranslationTrack(ITrack pGravityTrack) {
		this.mGravityTrack = pGravityTrack;
	}

	public Vector2 getGravity() {
		return mGravityVector;
	}

	public void setGravity(Vector2 grav) {
		setGravity(grav.x, grav.y);
	}

	public void setGravity(float pX, float pY) {
		this.mGravityVector.set(pX, pY);
		
		if (mSceneMain != null) {
			if (getGravityTranslationTrack() != null) {
				// compensate gravitation vector the given gravity track
				Vector2 track = getGravityTranslationTrack().getTrack();
				float deg = UtilsMath.getAngleDeg(track);
				UtilsMath.vectorRotateDeg(mGravityVector, deg + 90);
			}

			mSceneMain.getPhysics().setGravity(getGravity());
		}
	}
}

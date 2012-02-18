package eu.nazgee.game.ui.activity;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.physics.ITrack;
import eu.nazgee.game.scene.ScenePhysics;
import eu.nazgee.game.utils.UtilsMath;

abstract public class ActivityPhysics extends SimpleBaseGameActivity {

	private ScenePhysics mScenePhysics;
	private Vector2 mGravityVector;

	private ITrack mGravityTrack;
	private float mGravityTrackRotation;
	
	public ActivityPhysics() {
		super();
		mGravityVector = Vector2Pool.obtain(0, 0);
	}
	
	protected void setPhysicsScene(ScenePhysics pScenePhysics) {
		mScenePhysics = pScenePhysics;
	}
	
	protected ScenePhysics getPhysicsScene() {
		return mScenePhysics;
	}

	public ITrack getGravityTranslationTrack() {
		return mGravityTrack;
	}

	protected void setGravityTranslationTrack(ITrack pGravityTrack) {
		setGravityTranslationTrack(pGravityTrack, 0);
	}

	protected void setGravityTranslationTrack(ITrack pGravityTrack, float pGravityTrackRotation) {
		mGravityTrack = pGravityTrack;
		mGravityTrackRotation = pGravityTrackRotation;
	}

	public Vector2 getGravity() {
		return mGravityVector;
	}

	public void setGravity(Vector2 grav) {
		setGravity(grav.x, grav.y);
	}

	public void setGravity(float pX, float pY) {
		this.mGravityVector.set(pX, pY);
		
		if (getPhysicsScene() != null) {
			if (getGravityTranslationTrack() != null) {
				// compensate gravitation vector the given gravity track
				Vector2 track = getGravityTranslationTrack().getTrack();
				float deg = UtilsMath.getAngleDeg(track);
				UtilsMath.vectorRotateDeg(mGravityVector, deg + mGravityTrackRotation);
			}

			getPhysicsScene().getPhysics().setGravity(getGravity());
		}
	}
}

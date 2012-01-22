package eu.nazgee.game.ui.activity;

import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.scene.ScenePhysics;
import eu.nazgee.game.utils.UtilsMath;

abstract public class ActivityPhysics extends SimpleBaseGameActivity {

	private ScenePhysics mSceneMain;
	private IEntity mGravitationEntity;
	private Vector2 mGravityVector;
	
	public ActivityPhysics() {
		super();
		mGravityVector = Vector2Pool.obtain(0, 0);
	}
	
	protected void setMainScene(ScenePhysics pSceneMain) {
		mSceneMain = pSceneMain;
	}
	
	protected ScenePhysics getMainScene() {
		return mSceneMain;
	}

	public IEntity getGravitationEntity() {
		return mGravitationEntity;
	}

	protected void setGravitationEntity(IEntity pGravitationEntity) {
		this.mGravitationEntity = pGravitationEntity;
	}

	public Vector2 getGravity() {
		return mGravityVector;
	}

	public void setGravity(float pX, float pY) {
		this.mGravityVector.set(pX, pY);
		
		if (mSceneMain != null) {
			if (getGravitationEntity() == null) {
				mSceneMain.getPhysics().setGravity(getGravity());
			} else {
				UtilsMath.vectorRotateDeg(getGravity(), getGravitationEntity().getRotation());
				mSceneMain.getPhysics().setGravity(getGravity());
			}
		}
	}
}

package eu.nazgee.game.physics;

import org.andengine.extension.physics.box2d.PhysicsConnector;

import com.badlogic.gdx.math.Vector2;

public class TrackVelocity extends Track {

	private Vector2 mVec;  
	private PhysicsConnector mBody; 

	public TrackVelocity(PhysicsConnector pBody) {
		mBody = pBody;
		mVec = new Vector2(1,0);
	}

	@Override
	public Vector2 getTrack() {
		mVec.set(mBody.getBody().getLinearVelocity());

		if (isNormalized()) {
			mVec.set(mVec.nor()).mul(getNorm());
		}

		if (getScale() != 1) {
			return mVec.mul(getScale());
		} else {
			return mVec;
		}
	}
}

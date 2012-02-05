package eu.nazgee.game.physics;

import org.andengine.extension.physics.box2d.PhysicsConnector;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.UtilsMath;

public class TrackAngle extends Track {

	private Vector2 mVec;  
	private PhysicsConnector mBody; 

	public TrackAngle(PhysicsConnector pBody) {
		mBody = pBody;
		mVec = new Vector2(1,0);
		setNorm(1);
	}

	@Override
	public Vector2 getTrack() {
		mVec.set(getNorm() * getScale(), 0);
		float deg = mBody.getShape().getRotation();
		UtilsMath.vectorRotateDeg(mVec, deg);
		return mVec;
	}
}

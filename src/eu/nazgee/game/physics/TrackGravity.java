package eu.nazgee.game.physics;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

public class TrackGravity extends Track {
	private Vector2 mVec;  
	private PhysicsWorld mWorld; 

	public TrackGravity(PhysicsWorld pWorld) {
		mWorld = pWorld;
		mVec = new Vector2(1,0);
	}

	@Override
	public Vector2 getTrack() {
		mVec.set(mWorld.getGravity());

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

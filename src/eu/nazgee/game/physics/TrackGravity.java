package eu.nazgee.game.physics;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;

public class TrackGravity implements ITrack {

	private Vector2 mVec;  
	private PhysicsWorld mWorld; 

	public TrackGravity(PhysicsWorld pWorld) {
		mWorld = pWorld;
		mVec = new Vector2(1,0);
	}

	@Override
	public Vector2 getTrack() {
		mVec.set(mWorld.getGravity());
		return mVec;
	}
}

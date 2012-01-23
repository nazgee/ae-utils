package eu.nazgee.game.physics;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.UtilsMath;

public class TrackVelocity implements ITrack {
	
	private Vector2 mVec;  
	private PhysicsConnector mBody; 
	
	public TrackVelocity(PhysicsConnector pBody) {
		mBody = pBody;
		mVec = new Vector2(1,0);
	}
	
	@Override
	public Vector2 getTrack() {
		mVec.set(mBody.getBody().getLinearVelocity());
		return mVec;
	}
}

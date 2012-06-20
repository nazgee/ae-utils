//package eu.nazgee.game.utils.physics.track;
//
//import org.andengine.extension.physics.box2d.PhysicsConnector;
//
//import com.badlogic.gdx.math.Vector2;
//
//import eu.nazgee.game.utils.track.Track;
//
//public class TrackVelocity extends Track {
//	private PhysicsConnector mBody; 
//
//	public TrackVelocity(PhysicsConnector pBody) {
//		super(1,0);
//		mBody = pBody;
//	}
//
//	@Override
//	public Vector2 getTrack() {
//		mVec.set(mBody.getBody().getLinearVelocity());
//
//		return super.getTrack();
//	}
//}

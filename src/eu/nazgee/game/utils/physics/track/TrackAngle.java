//package eu.nazgee.game.utils.physics.track;
//
//import org.andengine.extension.physics.box2d.PhysicsConnector;
//
//import com.badlogic.gdx.math.Vector2;
//
//import eu.nazgee.game.utils.misc.UtilsMath;
//import eu.nazgee.game.utils.track.Track;
//
//public class TrackAngle extends Track {
//
//	private PhysicsConnector mBody; 
//
//	public TrackAngle(PhysicsConnector pBody) {
//		super(1,0);
//		mBody = pBody;
//		setNorm(1);
//	}
//
//	@Override
//	public Vector2 getTrack() {
//		mVec.set(getNorm() * getScale(), 0);
//
//		float deg = mBody.getShape().getRotation();
//		UtilsMath.vectorRotateDeg(mVec, deg);
//		return mVec;
//	}
//}

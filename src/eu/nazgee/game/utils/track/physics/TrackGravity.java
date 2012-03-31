package eu.nazgee.game.utils.track.physics;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.track.Track;

public class TrackGravity extends Track {
	private PhysicsWorld mWorld; 

	public TrackGravity(PhysicsWorld pWorld) {
		super(1, 0);
		mWorld = pWorld;
	}

	@Override
	public Vector2 getTrack() {
		mVec.set(mWorld.getGravity());
		return super.getTrack();
	}
}

package eu.nazgee.game.primitives;

import org.andengine.engine.handler.IUpdateHandler;

import com.badlogic.gdx.math.Vector2;

import android.util.Log;
import eu.nazgee.game.physics.ITrack;
import eu.nazgee.game.utils.UtilsMath;

public class TrackingArrow extends Arrow {

	private ITrack mTracker;
	static private final String LOGTAG = "TrackingArrow";

	public TrackingArrow(float pX1, float pY1, float pX2, float pY2, ITrack pTracker) {
		super(pX1, pY1, pX2, pY2);
		setTracker(pTracker);
	}

	public ITrack getTracker() {
		return mTracker;
	}
	
	public void setTracker(ITrack pTracker) {
		mTracker = pTracker;
	}
	
	public void updateTracking(float degrees_offset) {
		if (mTracker != null) {
			Vector2 track = mTracker.getTrack();
			float deg = UtilsMath.getAngleDeg(track);
			this.setRotation(deg + degrees_offset);
		} else {
			Log.e(LOGTAG, "Can't update tracking - no tracker!");
		}
	}
}

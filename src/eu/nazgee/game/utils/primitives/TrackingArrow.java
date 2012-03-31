package eu.nazgee.game.utils.primitives;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.misc.UtilsMath;
import eu.nazgee.game.utils.track.ITrack;

public class TrackingArrow extends Arrow {

	private ITrack mTracker;
	static private final String LOGTAG = "TrackingArrow";

	public TrackingArrow(float pX1, float pY1, float pX2, float pY2, ITrack pTracker, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX1, pY1, pX2, pY2, pVertexBufferObjectManager);
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

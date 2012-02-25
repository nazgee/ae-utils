package eu.nazgee.game.utils.engine.camera;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.misc.UtilsMath;
import eu.nazgee.game.utils.physics.ITrack;

public class TrackingCamera extends Camera {
	private PhysicsConnector mChaseBody;
	private ITrack mTrack;
	private float mOffsetDeg = 0;
	private float mOffsetLen = 0;
	
	
	public TrackingCamera(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	public void setTracking(PhysicsConnector pChaseBody, ITrack pTracking, float pOffsetDeg, float pOffsetLen) {
		super.setChaseEntity(pChaseBody.getShape());
		this.mTrack = pTracking;
		this.mChaseBody = pChaseBody;
		this.mOffsetDeg = pOffsetDeg;
		this.mOffsetLen = pOffsetLen;
	}

	@Override
	public void updateChaseEntity() {
		if(mChaseBody != null) {
			// update x-y position
			super.updateChaseEntity();
			
			// update rotation
			Vector2 track = mTrack.getTrack();
			float deg = UtilsMath.getAngleDeg(track);
			this.setRotation(mOffsetDeg - deg);
		}
	}

	@Override
	public void setCenter(float pCenterX, float pCenterY) {
		// move given center in tracking direction by mOffsetLen
		if(mChaseBody != null && (mOffsetLen != 0)) {
			Vector2 track = Vector2Pool.obtain(mTrack.getTrack());
			track.nor().mul(mOffsetLen);
			pCenterX += track.x;
			pCenterY += track.y;
			Vector2Pool.recycle(track);
		}		
		super.setCenter(pCenterX, pCenterY);
	}
}

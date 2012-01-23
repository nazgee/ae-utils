package eu.nazgee.game.engine.camera;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.physics.ITrack;
import eu.nazgee.game.utils.UtilsMath;

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
		super.updateChaseEntity();
		
		if(mChaseBody != null) {
			Vector2 track = mTrack.getTrack();
			this.setRotation(mOffsetDeg - UtilsMath.getAngleDeg(track));
		}
	}

	@Override
	public void setCenter(float pCenterX, float pCenterY) {
		if(mChaseBody != null && (mOffsetLen != 0)) {
			Vector2 track = mTrack.getTrack();
			track.nor().mul(mOffsetLen);
			pCenterX += track.x;
			pCenterY += track.y;
		}		
		super.setCenter(pCenterX, pCenterY);
	}
}

package eu.nazgee.game.engine.camera;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.UtilsMath;

public class VelocityTrackingCamera extends Camera {
	protected PhysicsConnector mChaseBody;
	
	public VelocityTrackingCamera(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	public void setChaseBody(PhysicsConnector pChaseBody) {
		super.setChaseEntity(pChaseBody.getShape());
		mChaseBody = pChaseBody;
	}

	@Override
	public void updateChaseEntity() {
		super.updateChaseEntity();
		
		if(mChaseBody != null) {
			Vector2 velocity = mChaseBody.getBody().getLinearVelocity();
			float angledeg = UtilsMath.getAngleDeg(velocity);
			
			this.setRotation(360 - 90 - angledeg);
		}
	}
}

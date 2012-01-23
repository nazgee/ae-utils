package eu.nazgee.game.engine.camera;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;

public class RotationTrackingCamera extends Camera {
	protected IEntity mChaseEntityCopy;
	
	public RotationTrackingCamera(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	@Override
	public void setChaseEntity(IEntity pChaseEntity) {
		super.setChaseEntity(pChaseEntity);
		mChaseEntityCopy = pChaseEntity;
	}

	@Override
	public void updateChaseEntity() {
		super.updateChaseEntity();
		if(mChaseEntityCopy != null) {
			this.setRotation(360 - mChaseEntityCopy.getRotation());
		}
	}
}

package eu.nazgee.game.engine.camera;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.util.constants.Constants;

import android.text.GetChars;

public class AutorotateCamera extends Camera {
	protected IEntity mChaseEntityCopy;
	
	public AutorotateCamera(float pX, float pY, float pWidth, float pHeight) {
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

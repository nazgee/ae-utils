package eu.nazgee.game.utils.engine.camera;

public class SmootherLinear implements ISmoother {
	float mFactor;

	public SmootherLinear(float mFactor) {
		this.mFactor = mFactor;
	}

	@Override
	public float getCorrectionSpeed(float pDelta, float pPreviousCorrectionSpeed) {
		pDelta = Math.abs(pDelta);
		return pDelta * mFactor;
	}
}

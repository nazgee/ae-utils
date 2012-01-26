package eu.nazgee.game.engine.camera;

public class LinerarSmoother implements ISmoother {
	float mFactor;

	public LinerarSmoother(float mFactor) {
		this.mFactor = mFactor;
	}

	@Override
	public float getCorrectionSpeed(float pDelta, float pPreviousCorrectionSpeed) {
		pDelta = Math.abs(pDelta);
		return pDelta * mFactor;
	}
}

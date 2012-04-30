package eu.nazgee.game.utils.engine.camera;


public class SmootherEmpty implements ISmoother {
	@Override
	public float getCorrectionSpeed(float pDelta, float pPreviousCorrectionSpeed) {
		return 0;
	}
}

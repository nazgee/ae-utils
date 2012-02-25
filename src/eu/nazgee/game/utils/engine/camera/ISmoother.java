package eu.nazgee.game.utils.engine.camera;

public interface ISmoother {
	public float getCorrectionSpeed(float pDelta, float pPreviousCorrectionSpeed);
}

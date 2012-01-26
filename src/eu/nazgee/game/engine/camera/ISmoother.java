package eu.nazgee.game.engine.camera;

public interface ISmoother {
	public float getCorrectionSpeed(float pDelta, float pPreviousCorrectionSpeed);
}

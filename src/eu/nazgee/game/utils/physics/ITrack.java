package eu.nazgee.game.utils.physics;

import com.badlogic.gdx.math.Vector2;

public interface ITrack {
	public Vector2 getTrack();
	public float getScale();
	public void setScale(float pScale);
	public float getNorm();
	public void setNorm(float pNorm);
	public boolean isNormalized();
}

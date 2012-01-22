package eu.nazgee.game.utils;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import android.util.FloatMath;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class UtilsMath {
	
	
	static private String LOGTAG = "MathUtils"; 
	/**
	 * Tests math utils
	 * @return true if utilities are ok;
	 */
	static public boolean test() {
		Vector2 vecA = Vector2Pool.obtain(0, 0);
		

		boolean ret;
		
		vecA.set(0, 1);
		ret = testRotateAndCheck("rotate and check", 15, vecA);
		
		Vector2Pool.recycle(vecA);
		return ret;
	}
	
	static public boolean testRotateAndCheck(String testTag, float step, Vector2 vecInitial) {
	
		Vector2 vecA = Vector2Pool.obtain(0, 0);
		Vector2 vecB = Vector2Pool.obtain(0, 0);
		
		vecA.set(vecInitial);
		vecB.set(vecInitial);
		boolean retval = true;
		for (float a = step; a < 720; a += step) {
			vectorRotateDeg(vecA, step);
			float angle = normalizeAngleDeg(getAngleDeg(vecA, vecB));
			
			Log.d(LOGTAG, testTag + "; vecB=" + vecA + "; vecA=" + vecB + "; wanted_a=" + a + "; a=" + angle);
			if (Math.abs(normalizeAngleDeg(a) - angle) > 0.01f) {
				Log.d(LOGTAG, testTag + "; ERROR! vecB=" + vecA + "; vecA=" + vecB + "; wanted_a=" + a + "; a=" + angle);
				retval = false;
				break;
			}
		}
		
		Vector2Pool.recycle(vecA);
		Vector2Pool.recycle(vecB);
		
		return retval;
	}
	
	/**
	 * Returns number of geometric quadrant to which given vector belongs
	 * @param vec
	 * @return quarter number [0..3]
	 */
	static public int getQuadrant(Vector2 vec) {
		int q;
		
		if (vec.x > 0) {
			if (vec.y > 0) {
				q = 0;
			} else {
				q = 3;
			}
		} else {
			if (vec.y > 0) {
				q = 1;
			} else {
				q = 2;
			}
		}
		
		return q;
	}
	
	/**
	 * Returns the angle in degrees between two given vectors
	 * @param vecA
	 * @param vecB
	 * @return angle in degrees
	 */
	static public float getAngleDeg(Vector2 vecA, Vector2 vecB) {
		return org.andengine.util.math.MathUtils.radToDeg(getAngleRad(vecA, vecB));
	}
	
	/**
	 * Returns the angle in radians between two given vectors
	 * @param vecA
	 * @param vecB
	 * @return angle in radians
	 */
	static public float getAngleRad(Vector2 vecA, Vector2 vecB) {
		float angA = getAngleRad(vecA);
		float angB = getAngleRad(vecB);
		
		return angA - angB;
	}
	
	/**
	 * Retrunts angle of vector in degrees
	 * @param vec
	 * @return angle in degrees
	 */
	static public float getAngleDeg(Vector2 vec) {
		return org.andengine.util.math.MathUtils.radToDeg(getAngleRad(vec));
	}
	
	/**
	 * Retrunts angle of vector in radians
	 * @param vec
	 * @return angle in radians
	 */
	static public float getAngleRad(Vector2 vec) {
		float ret = (float) Math.atan2(vec.y, vec.x);
		
		return ret;
	}
	
	/**
	 * Normalizes given angle to [0..360) range
	 * @param angleDeg
	 * @return
	 */
	static public float normalizeAngleDeg(float angleDeg) {
		if(angleDeg > 0 || angleDeg <= 360f ) {
			final float pMod = angleDeg % 360f;
			if(pMod >= 0)
				return pMod;
			
			return (float) (pMod + 360);
		} else {
			return angleDeg;
		}
	}
	
	/**
	 * Normalizes given angle to [0..2PI) range
	 * @param angleRad
	 * @return
	 */
	static public float normalizeAngleRad(float angleRad) {
		if(angleRad > 0 || angleRad <= Math.PI * 2) {
			final float pMod = (float) (angleRad % (Math.PI * 2));
			if(pMod >= 0)
				return pMod;
			
			return (float) (pMod + (Math.PI * 2));
		} else {
			return angleRad;
		}
	}

	/**
	 * Rotates given vector by a given number of degrees
	 * @param vec vector to rotate
	 * @param angleDeg number of degrees that this vector should be rotated by
	 */
	static public void vectorRotateDeg(Vector2 vec, float angleDeg) {
		vectorRotateRad(vec, org.andengine.util.math.MathUtils.degToRad(angleDeg));
	}
	
	/**
	 * Rotates given vector by a given number of radians
	 * @param vec vector to rotate
	 * @param angleRad number of radians that this vector should be rotated by
	 */
	static public void vectorRotateRad(Vector2 vec, float angleRad) {
		final float sin = FloatMath.sin(angleRad);
		final float cos = FloatMath.cos(angleRad);

		final float x = vec.x * cos - vec.y * sin;
		final float y = vec.x * sin + vec.y * cos;
		
		vec.set(x, y);
	}
}

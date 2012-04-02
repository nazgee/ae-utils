package eu.nazgee.game.utils.helpers;

import org.andengine.entity.shape.IAreaShape;

public class Positioner {

	synchronized static public float getCenteredY(IAreaShape pShape) {
		float[] cnt = getCenter(pShape);
		return cnt[1];
	}

	synchronized static public float getCenteredX(IAreaShape pShape) {
		float[] cnt = getCenter(pShape);
		return cnt[0];
	}

	synchronized static public float[] getCenter(IAreaShape pShape) {
		float[] ret = new float[2]; 
		pShape.convertLocalToSceneCoordinates(pShape.getWidth()/2, pShape.getHeight()/2, ret);
		return ret;
	}

	synchronized static public void setCentered(IAreaShape pShape, float pX, float pY) {
		pShape.setPosition(pX - pShape.getWidth()/2, pY - pShape.getHeight()/2);
	}
	synchronized static public void setCenteredX(IAreaShape pShape, float pX) {
		pShape.setPosition(pX - pShape.getWidth()/2, pShape.getY());
	}
	synchronized static public void setCenteredY(IAreaShape pShape, float pY) {
		pShape.setPosition(pShape.getX(), pY - pShape.getHeight()/2);
	}

	synchronized static public void setCentered(IAreaShape pShape, IAreaShape pWhere) {
		float[] pos = getCenter(pWhere);
		setCentered(pShape, pos[0], pos[1]);
	}
	synchronized static public void setCenteredX(IAreaShape pShape, IAreaShape pWhere) {
		float[] pos = getCenter(pWhere);
		setCenteredX(pShape, pos[0]);
	}
	synchronized static public void setCenteredY(IAreaShape pShape, IAreaShape pWhere) {
		float[] pos = getCenter(pWhere);
		setCenteredY(pShape, pos[1]);
	}

	public static void applyOffsetBy(IAreaShape target, final float dX, final float dY) {
		target.setPosition(target.getX() + dX, target.getY() + dY);
	}

	public static void unapplyOffsetBy(IAreaShape target, final float dX, final float dY) {
		applyOffsetBy(target, -dX, -dY);
	}
}

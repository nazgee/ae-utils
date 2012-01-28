package eu.nazgee.game.primitives;

import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.utils.UtilsMath;


public class Arrow extends Line {
	public Arrow(float pX1, float pY1, float pX2, float pY2, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX1, pY1, pX2, pY2, pVertexBufferObjectManager);

		Vector2 head = Vector2Pool.obtain(pX2 - pX1, pY2 - pY1);
		head.mul(0.2f);
		UtilsMath.vectorRotateDeg(head, 30);
		Line h1 = new Line(0, 0, head.x, head.y, pVertexBufferObjectManager);
		UtilsMath.vectorRotateDeg(head, 300);
		Line h2 = new Line(0, 0, head.x, head.y, pVertexBufferObjectManager);
		attachChild(h1);
		attachChild(h2);
	}
}

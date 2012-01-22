package eu.nazgee.game.physics;

import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Boundry {
	
	private FixtureDef mFixtureWalls;
	private Body mTop;
	private Body mBottom;
	private Body mLeft;
	private Body mRight;
	
	public Boundry(float pX, float pY, float pW, float pH, float pOffset, PhysicsWorld pWorld, FixtureDef pFixtureWalls) {
		float off = pOffset;
		float x1 = pX;
		float y1 = pY;
		float x2 = pX + pW;
		float y2 = pY + pH;
		mFixtureWalls = pFixtureWalls;
		mTop = PhysicsFactory.createLineBody(pWorld, x1 - off, y1, x2 + off, y1, mFixtureWalls);
		mBottom = PhysicsFactory.createLineBody(pWorld, x1 - off, y2, x2 + off, y2, mFixtureWalls);
		mLeft = PhysicsFactory.createLineBody(pWorld, x1, y1 - off, x1, y2 + off, mFixtureWalls);
		mRight = PhysicsFactory.createLineBody(pWorld, x2, y1 - off, x2, y2 + off, mFixtureWalls);
	}
	
	public FixtureDef getFixture() {
		return mFixtureWalls;
	}

	public Body getTop() {
		return mTop;
	}

	public Body getBottom() {
		return mBottom;
	}

	public Body getLeft() {
		return mLeft;
	}

	public Body getRight() {
		return mRight;
	}

}

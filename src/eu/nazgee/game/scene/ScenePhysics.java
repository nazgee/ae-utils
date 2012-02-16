package eu.nazgee.game.scene;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import eu.nazgee.game.primitives.GridDouble;

public class ScenePhysics extends SceneLoadable implements ContactListener {
	private Body mGroundBody;
	private PhysicsWorld mPhysics;
	private GridDouble mDebugGrid;
	
	public ScenePhysics(final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pVertexBufferObjectManager, new Vector2(0, SensorManager.GRAVITY_EARTH), false, 0);
	}

	public ScenePhysics(final VertexBufferObjectManager pVertexBufferObjectManager, final Vector2 pGravity, final boolean pAllowSleep, int fixedStep) {
		super(pVertexBufferObjectManager);

		if (fixedStep > 0) {
			mPhysics = new FixedStepPhysicsWorld(fixedStep, pGravity.mul(0.02f), pAllowSleep);
		} else {
			mPhysics = new PhysicsWorld(pGravity.mul(0.02f), pAllowSleep);
		}
	}
	/*=========================================================================
	 * 							getters & setters
	 *=======================================================================*/
	public Body getGroundBody() {
		return mGroundBody;
	}

	public PhysicsWorld getPhysics() {
		return mPhysics;
	}
	/*=========================================================================
	 * 							from SceneLoadable
	 *=======================================================================*/
	@Override
	public Scene load(final Engine e, Context c) {
		mGroundBody = mPhysics.createBody(new BodyDef());
		registerUpdateHandler(mPhysics);

		return this;
	}

	@Override
	public void unload() {
		mPhysics.destroyBody(mGroundBody);
		unregisterUpdateHandler(mPhysics);
		reset();
	}

	@Override
	public void loadResourcesOnce(Engine e, Context c) {
	}
	/*=========================================================================
	 * 							ContactListener implementation
	 *=======================================================================*/

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	/*=========================================================================
	 * 							static
	 *=======================================================================*/
	/**
	 * Checks whether given PhysicsConnector suffered from a collision
	 * 
	 * @param pContact incident to be investigated
	 * @param pVicitim object that we want to verify
	 * @param pVicitimFixtureCategory category of pVictim fixture that should be checked for collision
	 * @param pCulpritFixtureCategory category of fixture that we should treat as collision
	 * @return true if collision happened; false otherwise
	 */
	static protected boolean hasColided(Contact pContact, PhysicsConnector pVicitim, int pVicitimFixtureCategory, int pCulpritFixtureCategory) {
		return (getCulprit(pContact, pVicitim, pVicitimFixtureCategory, pCulpritFixtureCategory) != null);
	}
	
	/**
	 * Checks whether given PhysicsConnector suffered from a collision
	 * 
	 * @param pContact incident to be investigated
	 * @param pVicitim object that we want to verify
	 * @param pVicitimFixtureCategory category of pVictim fixture that should be checked for collision
	 * @param pCulpritFixtureCategory category of fixture that we should treat as collision
	 * @return Other participant of collision; null if none was found
	 */
	static protected PhysicsConnector getCulprit(Contact pContact, PhysicsConnector pVicitim, int pVicitimFixtureCategory, int pCulpritFixtureCategory) {
		PhysicsConnector culprit = null;
		
		PhysicsConnector suspectA = (PhysicsConnector) pContact.getFixtureA().getBody().getUserData();
		PhysicsConnector suspectB = (PhysicsConnector) pContact.getFixtureB().getBody().getUserData();
		
		if ((suspectA == pVicitim) || (suspectB == pVicitim)) {
			if ((pContact.getFixtureB().getFilterData().categoryBits & pVicitimFixtureCategory) != 0
					&&  (pContact.getFixtureA().getFilterData().categoryBits & pCulpritFixtureCategory) != 0) {
				culprit = suspectA;
			} else if ((pContact.getFixtureA().getFilterData().categoryBits & pVicitimFixtureCategory) != 0
					&&  (pContact.getFixtureB().getFilterData().categoryBits & pCulpritFixtureCategory) != 0) {
				culprit = suspectB;
			}
		}
		
		return culprit;
	}

	/*=========================================================================
	 * 							helpers
	 *=======================================================================*/
	
	protected void attachDebugGrid(int pX, int pY, int pW, int pH, int pGridSize, float pSubgridDivisor, int zindex, final VertexBufferObjectManager pVertexBufferObjectManager) {
		detachDebugGrid();
		mDebugGrid = new GridDouble(pX, pY, pW, pH, pGridSize, pGridSize, pSubgridDivisor, new Color(1, 1, 1, 0.5f), new Color(0, 0, 1, 0.4f), pVertexBufferObjectManager);
		reattachDebugGrid();
		sortChildren(false);
	}
	
	protected void detachDebugGrid() {
		detachChild(mDebugGrid);
	}
	
	protected void reattachDebugGrid() {
		if (mDebugGrid != null)
			attachChild(mDebugGrid); 
	}
}

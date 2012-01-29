package eu.nazgee.game.utils.debugdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.andengine.entity.Entity;
import org.andengine.entity.shape.Shape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import eu.nazgee.game.utils.debugdraw.primitive.Ellipse;
import eu.nazgee.game.utils.debugdraw.primitive.Polyline;

public class Box2dDebugRenderer extends Entity {

	private PhysicsWorld world;

	private Queue<Body> toRemove = new LinkedList<Body>();
	private HashMap<Body, ArrayList<Shape>> bodies = new HashMap<Body, ArrayList<Shape>>();
	private HashMap<Body, Boolean> isBodyActiveMap = new HashMap<Body, Boolean>();
	private HashMap<Shape, Boolean> isSensorMap = new HashMap<Shape, Boolean>();

	private float colorAlpha = 0.75f;
	private Color dynamicBodyColor = new Color(1, 0, 0);
	private Color kinematicBodyColor = new Color(1, 1, 0);
	private Color staticBodyColor = new Color(0, 1, 1);
	private Color sensorBodyColor = new Color(0.2f, 0.2f, 1);
	private Color inactiveBodyColor = new Color(0.7f, 0.7f, 0.7f);
	private Color centroidColor = new Color(1, 0, 1);
	
	final VertexBufferObjectManager mVertexBufferObjectManager;

	public Box2dDebugRenderer(PhysicsWorld world, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this.world = world;
		mVertexBufferObjectManager = pVertexBufferObjectManager;
	}

	@Override
	public void onManagedUpdate(float pSecondsElapsed) {
		float ptm = PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

		for (Body body : bodies.keySet()) {
			isBodyActiveMap.put(body, false);
		}

		Iterator<Body> iterator = world.getBodies();
		while (iterator.hasNext()) {
			Body body = iterator.next();
			if (!bodies.containsKey(body))
				addBodyToShapeCollection(body);
			isBodyActiveMap.put(body, true);

			// update shapes
			ArrayList<Shape> shapes = bodies.get(body);
			int shapesSize = shapes.size();
			for (int i = 0; i < shapesSize; i++) {
				Shape shape = shapes.get(i);

				if (body.isAwake()) {
					switch (body.getType()) {
						case  StaticBody: {
							shape.setColor(staticBodyColor);
						} break;
						case  DynamicBody: {
							shape.setColor(dynamicBodyColor);
						} break;
						case  KinematicBody: {
							shape.setColor(kinematicBodyColor);
						} break;
					}
				}
				else if (isSensorMap.containsKey(shape))
					shape.setColor(sensorBodyColor);
				else
					shape.setColor(inactiveBodyColor);
				shape.setAlpha(colorAlpha);

				
//				float cX;
//				float cY;
//				
//				ArrayList<Fixture> f = body.getFixtureList();
//				com.badlogic.gdx.physics.box2d.Shape sh = f.get(0).getShape();
//				
//				if ((sh instanceof PolygonShape)) {
//					PolygonShape pol = (PolygonShape) sh;
//					
//					float A = Polyline.centroidArea(pol);
//					cX = Polyline.centroidX(pol, A);
//					cY = Polyline.centroidY(pol, A);
//					
//				} else if (sh instanceof CircleShape) {
//					CircleShape cir = (CircleShape) sh;
//					Vector2 c = cir.getPosition();
//					cX = c.x * ptm;
//					cY = c.y * ptm;
//				} else {
//					cX = body.getMassData().center.x * ptm;
//					cY = body.getMassData().center.y * ptm;
//				}
//
//				
//				
//				shape.setRotationCenter(cX, cY);
				shape.setRotation((float) (body.getAngle() * (180 / Math.PI)));
				shape.setPosition(body.getPosition().x * ptm,
						body.getPosition().y * ptm);
//				shape.setPosition(body.getPosition().x * ptm + cX,
//						body.getPosition().y * ptm + cY);
//				shape.setPosition(cX, cY);
			}
		}

		// remove all shapes of bodies that are no longer in the world
		for (Body body : bodies.keySet()) {
			if (isBodyActiveMap.get(body) == null
					|| isBodyActiveMap.get(body) == false) {
				ArrayList<Shape> shapes = bodies.get(body);
				for (Shape shape : shapes) {
					this.detachChild(shape);
				}
				toRemove.add(body);
			}
		}

		// remove all bodies that are no longer in the world
		for (Body body : toRemove) {
			bodies.remove(body);
		}
		toRemove.clear();
	}

	private void addBodyToShapeCollection(Body body) {
		bodies.put(body, new ArrayList<Shape>());
		isBodyActiveMap.put(body, false);

		for (Fixture fixture : body.getFixtureList()) {
			Shape debugShape = null;
			if (fixture.getShape() instanceof PolygonShape) {
				debugShape = createPolygonShape(fixture);
				Log.e("debugrenderrer", "createPolygonShape");
			} else if (fixture.getShape() instanceof CircleShape) {
				debugShape = createCircleShape(fixture);
				Log.e("debugrenderrer", "createCircleShape");
			}

			if (debugShape != null) {
				bodies.get(body).add(debugShape);
				if (fixture.isSensor())
					isSensorMap.put(debugShape, true);
				this.attachChild(debugShape);
			} else {
				Log.e("debugrenderrer", "unknown shape");
			}
		}
	}

	private Shape createCircleShape(Fixture fixture) {
		CircleShape fixtureShape = (CircleShape) fixture.getShape();
		Vector2 position = fixtureShape.getPosition();
		Ellipse ellipse = new Ellipse(position.x, position.y,
				fixtureShape.getRadius()
						* PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT, centroidColor, mVertexBufferObjectManager);
		return ellipse;
	}

	private Shape createPolygonShape(Fixture fixture) {
		PolygonShape fixtureShape = (PolygonShape) fixture.getShape();
		if (fixtureShape == null)
			return null;
		if (fixtureShape.getVertexCount()<3) { 
			ArrayList<Float> xPoints = new ArrayList<Float>();
			ArrayList<Float> yPoints = new ArrayList<Float>();
			Vector2 vertex = new Vector2();
			
			for (int i = 0; i < fixtureShape.getVertexCount(); i++) {
				fixtureShape.getVertex(i, vertex);
				Log.e("debugrenderrer", "vertex" + i + "/" + fixtureShape.getVertexCount() + "=(" + vertex.x + "," + vertex.y + ")");
				xPoints.add(vertex.x
						* PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
				yPoints.add(vertex.y
						* PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
			}
			return new Polyline(xPoints, yPoints, centroidColor, mVertexBufferObjectManager);
		} else {
			float[] xPoints = new float[fixtureShape.getVertexCount()];
			float[] yPoints = new float[fixtureShape.getVertexCount()];
			Vector2 vertex = new Vector2();
			
			for (int i = 0; i < fixtureShape.getVertexCount(); i++) {
				fixtureShape.getVertex(i, vertex);
				Log.e("debugrenderrer", "vertex" + i + "/" + fixtureShape.getVertexCount() + "=(" + vertex.x + "," + vertex.y + ")");
				xPoints[i]=(vertex.x
						* PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
				yPoints[i]=(vertex.y
						* PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
			}
			Shape poly = new org.andengine.entity.primitive.Polygon(0,0, xPoints, yPoints, mVertexBufferObjectManager);;
			poly.setColor(centroidColor);
			return poly;
		}
	}

	public void setActiveBodyColor(int red, int green, int blue) {
		dynamicBodyColor.set(red, green, blue);
	}

	public void setSensorBodyColor(int red, int green, int blue) {
		sensorBodyColor.set(red, green, blue);
	}

	public void setInactiveBodyColor(int red, int green, int blue) {
		inactiveBodyColor.set(red, green, blue);
	}

	public Color getActiveBodyColor() {
		return dynamicBodyColor;
	}

	public Color getSensorBodyColor() {
		return sensorBodyColor;
	}

	public Color getInactiveBodyColor() {
		return inactiveBodyColor;
	}

}

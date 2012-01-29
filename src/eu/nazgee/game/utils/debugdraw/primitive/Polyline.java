package eu.nazgee.game.utils.debugdraw.primitive;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import android.util.Log;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class Polyline extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float LINEWIDTH_DEFAULT = 3.0f;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Polyline.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Polyline.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	private static String LOGTAG = "Polyline";
	
	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IPolygonVertexBufferObject mPolygonVertexBufferObject;
	public int mDataVerticesNumber = -1;
	public int mTotalVerticesNumber = -1;
	public int mPolygonSize = -1;
	protected ArrayList<Float> mX;
	protected ArrayList<Float> mY;
	protected float mLineWidth;
	
	// eye-candies
	protected boolean mMarkCentroid;
	protected Color mCentroidColor;

	private float mA;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Polyline(final ArrayList<Float> pX, final ArrayList<Float> pY, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, LINEWIDTH_DEFAULT, pVertexBufferObjectManager);
	}
	
	public Polyline(final ArrayList<Float> pX, final ArrayList<Float> pY, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pLineWidth, null, pVertexBufferObjectManager);
	}
	
	public Polyline(final ArrayList<Float> pX, final ArrayList<Float> pY, Color pCentroidColor, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, LINEWIDTH_DEFAULT, pCentroidColor, pVertexBufferObjectManager);
	}

	public Polyline(final ArrayList<Float> pX, final ArrayList<Float> pY, final float pLineWidth, Color pCentroidColor, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX.get(0), pY.get(0), 100, 100, PositionColorShaderProgram.getInstance());

		Log.e(LOGTAG, "px=" + pX + "; py=" + pY);
		
		if (pX.size() != pY.size())
			throw new IllegalArgumentException("pX and pY arrays must by of equal size!");
		
		mX = new ArrayList<Float>(pX);
		mY = new ArrayList<Float>(pY);
		mLineWidth = pLineWidth;
		mMarkCentroid = pCentroidColor != null;
		mCentroidColor = pCentroidColor;
		
		mDataVerticesNumber = pX.size();
		mTotalVerticesNumber =  mDataVerticesNumber + (mMarkCentroid ? 2 : 0);
		mPolygonSize = VERTEX_SIZE * mTotalVerticesNumber;
		
		this.mPolygonVertexBufferObject = new HighPerformancePolygonVertexBufferObject(pVertexBufferObjectManager, mPolygonSize, DrawType.STATIC, true, Polyline.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

		mA = centroidArea(pX, pY);
		resetScaleCenter();
		resetRotationCenter();

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	static public float centroidX(final ArrayList<Float> pX, final ArrayList<Float> pY, float A) {
		float Cx = 0;
		for (int i = 0; i < pX.size(); i++) {
			int ip = (i+1) % pX.size();
			Cx += (pX.get(i).floatValue() + pX.get(ip).floatValue()) * vertexGeoDiff(i, pX, pY);
		}
		return Cx / (6*A);
	}
	
	static public float centroidY(final ArrayList<Float> pX, final ArrayList<Float> pY, float A) {
		float Cy = 0;
		for (int i = 0; i < pX.size(); i++) {
			int ip = (i+1) % pX.size();
			Cy += (pY.get(i).floatValue() + pY.get(ip).floatValue()) * vertexGeoDiff(i, pX, pY);
		}
		return Cy / (6*A);
	}
	
	static public float centroidArea(final ArrayList<Float> pX, final ArrayList<Float> pY) {
		float A = 0;
		for (int i = 0; i < pX.size(); i++) {
			A += vertexGeoDiff(i, pX, pY);
		}
		
		return A * 0.5f;
	}
	
	/**
	 * Counts the geometric diff of given vertices (Xn * Yn+1 - Xn+1 * Yn)
	 * @param n step of computation
	 * @param pX
	 * @param pY
	 * @return
	 */
	static private float vertexGeoDiff(int n, final ArrayList<Float> pX, final ArrayList<Float> pY) {
		int i = (n) % pX.size();
		int ip = (n+1) % pX.size();
		return pX.get(i).floatValue() * pY.get(ip).floatValue() - pX.get(ip).floatValue() * pY.get(i).floatValue(); 
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	
	

	@Override
	public IPolygonVertexBufferObject getVertexBufferObject() {
		return this.mPolygonVertexBufferObject;
	}

	@Override
	public void resetRotationCenter() {
		this.mRotationCenterX = getX();
		this.mRotationCenterY = getY();
	}

	@Override
	public void resetScaleCenter() {
		this.mScaleCenterX = getX();
		this.mScaleCenterY = getY();
	}

	@Override
	public void resetSkewCenter() {
		this.mSkewCenterX = getX();
		this.mSkewCenterY = getY();
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		pGLState.lineWidth(this.mLineWidth);
		this.mPolygonVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
//		this.mPolygonVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, mTotalVerticesNumber);
		this.mPolygonVertexBufferObject.draw(GLES20.GL_LINE_LOOP, mTotalVerticesNumber);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mPolygonVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mPolygonVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mPolygonVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPolygonVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================
		
		// ===========================================================
		// Methods
		// ===========================================================
		
		public void onUpdateColor(final Polyline pPolygon);
		public void onUpdateVertices(final Polyline pPolygon);
	}
	
	public static interface IRectangleVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Polyline pRectangle);
		public void onUpdateVertices(final Polyline pRectangle);
	}

	public static class HighPerformancePolygonVertexBufferObject extends HighPerformanceVertexBufferObject implements IPolygonVertexBufferObject {
		
		public HighPerformancePolygonVertexBufferObject(
				final VertexBufferObjectManager pVertexBufferObjectManager,
				final int vertices_in_poly,
				DrawType pDrawType, boolean pManaged,
				VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, vertices_in_poly, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}
		
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(Polyline pPolygon) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pPolygon.getColor().getPacked();

			int i;
			
			for (i = 0; i < pPolygon.mDataVerticesNumber; i++) {
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.COLOR_INDEX] = packedColor;
			}
			
			if (pPolygon.mMarkCentroid) {
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.COLOR_INDEX] = packedColor;
				i++;
				
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.COLOR_INDEX] = pPolygon.mCentroidColor.getPacked();
				i++;
			}
			
			this.setDirtyOnHardware();			
		}

		@Override
		public void onUpdateVertices(Polyline pPolygon) {
			final float[] bufferData = this.mBufferData;
					
			int i;
			for (i = 0; i < pPolygon.mDataVerticesNumber; i++) {
				Log.d(LOGTAG, "boundary_vertex" + i + "/" + pPolygon.mDataVerticesNumber + "=(" + pPolygon.mX.get(i) + "," + pPolygon.mY.get(i) + ")");
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.VERTEX_INDEX_X] = pPolygon.mX.get(i);
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.VERTEX_INDEX_Y] = pPolygon.mY.get(i);
			}
			
			if (pPolygon.mMarkCentroid) {
				Log.d(LOGTAG, "repeated_vertex" + "=(" + pPolygon.mRotationCenterX + "," + pPolygon.mRotationCenterY + ")");
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.VERTEX_INDEX_X] = pPolygon.mX.get(0);
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.VERTEX_INDEX_Y] = pPolygon.mY.get(0);
				i++;
				
				Log.d(LOGTAG, "centroid_vertex" + "=(" + pPolygon.mRotationCenterX + "," + pPolygon.mRotationCenterY + ")");
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.VERTEX_INDEX_X] = pPolygon.mRotationCenterX;
				bufferData[i * Polyline.VERTEX_SIZE + Polyline.VERTEX_INDEX_Y] = pPolygon.mRotationCenterY;
				i++;
			}

			this.setDirtyOnHardware();			
		}
		
	}
}

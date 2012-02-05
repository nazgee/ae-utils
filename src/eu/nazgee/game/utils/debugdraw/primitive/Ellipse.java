package eu.nazgee.game.utils.debugdraw.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
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
public class Ellipse extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float LINEWIDTH_DEFAULT = 3.0f;
	private static final int SEGMENTS_DEFAULT = 50;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Ellipse.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Ellipse.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	private static String LOGTAG = "Ellipse";
	
	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IEllipseVertexBufferObject mEllipseVertexBufferObject;
	public int mDataVerticesNumber = -1;
	public int mTotalVerticesNumber = -1;
	public int mEllipseSize = -1;
	protected float mLineWidth;
	
	// eye-candies
	protected boolean mMarkCentroid;
	protected Color mCentroidColor;

	private final int mSegments;
	private final float mRadiusX;
	private final float mRadiusY;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Ellipse(float pX, float pY, float radius, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, radius, radius, LINEWIDTH_DEFAULT, SEGMENTS_DEFAULT, null, pVertexBufferObjectManager);
	}
	
	public Ellipse(float pX, float pY, float radius, Color pCentroidColor, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, radius, radius, LINEWIDTH_DEFAULT, SEGMENTS_DEFAULT, pCentroidColor, pVertexBufferObjectManager);
	}
	
	public Ellipse(float pX, float pY, float radiusX, float radiusY, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, radiusX, radiusY, LINEWIDTH_DEFAULT, SEGMENTS_DEFAULT, null, pVertexBufferObjectManager);
	}
	
	public Ellipse(float pX, float pY, float radiusX, float radiusY, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, radiusX, radiusY, pLineWidth, SEGMENTS_DEFAULT, null, pVertexBufferObjectManager);
	}
	
	public Ellipse(float pX, float pY, float radius, final float pLineWidth, Color pCentroidColor, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, radius, radius, pLineWidth, SEGMENTS_DEFAULT, pCentroidColor, pVertexBufferObjectManager);
	}
	
	public Ellipse(float pX, float pY, float radiusX, float radiusY, final float pLineWidth, Color pCentroidColor, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, radiusX, radiusY, pLineWidth, SEGMENTS_DEFAULT, pCentroidColor, pVertexBufferObjectManager);
	}
	
	private Ellipse(float pX, float pY, float pRadiusX, float pRadiusY,
			float pLineWidth, int pSegments,
			Color pCentroidColor,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, 2*pRadiusX, 2*pRadiusY, PositionColorShaderProgram.getInstance());
		this.mRadiusX = pRadiusX;
		this.mRadiusY = pRadiusY;
		this.mSegments = pSegments;

		Log.e(LOGTAG, "pX=" + pX + "; pY=" + pY + "; rX=" + pRadiusX + "; rY=" + pRadiusY);
		
		if ((pRadiusX <= 0) || (pRadiusY <= 0))
			throw new IllegalArgumentException("rX and rY arrays must >0!");
		
		mLineWidth = pLineWidth;
		mCentroidColor = pCentroidColor;
		mMarkCentroid = pCentroidColor != null;
		
		mDataVerticesNumber = pSegments;
		mTotalVerticesNumber =  mDataVerticesNumber + (mMarkCentroid ? 2 : 0);
		mEllipseSize = VERTEX_SIZE * mTotalVerticesNumber;
		
		this.mEllipseVertexBufferObject = new HighPerformanceEllipseVertexBufferObject(pVertexBufferObjectManager, mEllipseSize, DrawType.STATIC, true, Ellipse.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

		this.mRotationCenterX = pX;
		this.mRotationCenterY = pX;
		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;
				
		this.onUpdateVertices();
		this.onUpdateColor();
		
		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IEllipseVertexBufferObject getVertexBufferObject() {
		return this.mEllipseVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		pGLState.lineWidth(this.mLineWidth);
		this.mEllipseVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mEllipseVertexBufferObject.draw(GLES20.GL_LINE_LOOP, mTotalVerticesNumber);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mEllipseVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mEllipseVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mEllipseVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IEllipseVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================
		
		// ===========================================================
		// Methods
		// ===========================================================
		
		public void onUpdateColor(final Ellipse pEllipse);
		public void onUpdateVertices(final Ellipse pEllipse);
	}
	
	public static interface IRectangleVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Ellipse pRectangle);
		public void onUpdateVertices(final Ellipse pRectangle);
	}

	public static class HighPerformanceEllipseVertexBufferObject extends HighPerformanceVertexBufferObject implements IEllipseVertexBufferObject {
		
		public HighPerformanceEllipseVertexBufferObject(
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
		public void onUpdateColor(Ellipse pEllipse) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pEllipse.getColor().getFloatPacked();

			int i;
			
			for (i = 0; i < pEllipse.mDataVerticesNumber; i++) {
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.COLOR_INDEX] = packedColor;
			}
			
			if (pEllipse.mMarkCentroid) {
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.COLOR_INDEX] = packedColor;
				i++;
				
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.COLOR_INDEX] = pEllipse.mCentroidColor.getFloatPacked();
				i++;
			}
			
			this.setDirtyOnHardware();			
		}

		@Override
		public void onUpdateVertices(Ellipse pEllipse) {
			final float[] bufferData = this.mBufferData;
					
			int i = 0;
			
			float x = pEllipse.mRotationCenterX;
			float y = pEllipse.mRotationCenterY;
			
			for (float a = 0; a < (Math.PI*2); a += ((Math.PI*2) / pEllipse.mSegments)) {
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.VERTEX_INDEX_X] = ((float) (Math.cos(a) * pEllipse.mRadiusX)) + x;
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.VERTEX_INDEX_Y] = ((float) (Math.sin(a) * pEllipse.mRadiusY)) + y;
				i++;
			}
			
			if (pEllipse.mMarkCentroid) {
				Log.d(LOGTAG, "repeated_vertex" + "=(" + pEllipse.mRotationCenterX + "," + pEllipse.mRotationCenterY + ")");
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.VERTEX_INDEX_X] = ((float) (Math.cos(0) * pEllipse.mRadiusX)) + x;
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.VERTEX_INDEX_Y] = ((float) (Math.sin(0) * pEllipse.mRadiusY)) + y;
				i++;
				
				Log.d(LOGTAG, "centroid_vertex" + "=(" + x + "," + y + ")");
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.VERTEX_INDEX_X] = x;
				bufferData[i * Ellipse.VERTEX_SIZE + Ellipse.VERTEX_INDEX_Y] = y;
				i++;
			}

			this.setDirtyOnHardware();			
		}
		
	}
}

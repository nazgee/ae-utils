package eu.nazgee.game.primitives;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import android.util.Log;


//public class Grid extends Rectangle {
//
//	public Grid(float pX, float pY, float pWidth, float pHeight, float sizeX, float sizeY, Color pColor) {
//		super(pX, pY, pWidth, pHeight);
//		
//		setColor(0, 0, 1, 0.0f);
//		
//		for (int i=0; i*sizeX <= pWidth; i++) {
//			float x = i*sizeX;
//			Line l = new Line(x, 0, x, pHeight);
//			l.setColor(pColor);
//			attachChild(l);
//		}
//		
//		for (int i=0; i*sizeY <= pHeight; i++) {
//			float y = i*sizeY;
//			Line l = new Line(0, y, pWidth, y);
//			l.setColor(pColor);
//			attachChild(l);
//		}
//	}
//}

public class Grid extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float LINEWIDTH_DEFAULT = 1.0f;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Grid.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Grid.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	private static String LOGTAG = "Grid";
	
	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IGridVertexBufferObject mGridVertexBufferObject;
	public int mTotalVerticesNumber;
	public final int mVerticesNumberHor;
	public final int mVerticesNumberVer;
	public final int mGridSize;
	private final float mLineWidth;
	
	// eye-candies
	protected boolean mMarkCentroid;
	protected Color mCentroidColor;

	private final float sizeX;
	private final float sizeY;

	// ===========================================================
	// Constructors
	// ===========================================================
	public Grid(float pX, float pY, float pWidth, float pHeight, float sizeX, float sizeY) {
		this(pX, pY, pWidth, pHeight, sizeX, sizeY, LINEWIDTH_DEFAULT);
	}
	
	public Grid(float pX, float pY, float pWidth, float pHeight, float sizeX, float sizeY, float pLineWidth) {
		super(pX, pY, pWidth, pHeight, PositionColorShaderProgram.getInstance());
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.mLineWidth = pLineWidth;
		
		mVerticesNumberVer =  ((int)(pWidth/sizeX) + 1) * 2;
		mVerticesNumberHor =  ((int)(pHeight/sizeY) + 1) * 2;
		mTotalVerticesNumber = mVerticesNumberVer + mVerticesNumberHor;
		mGridSize = VERTEX_SIZE * mTotalVerticesNumber;
		
		Log.e(LOGTAG, "v=" + mVerticesNumberVer + "; h=" + mVerticesNumberHor);
		
		this.mGridVertexBufferObject = new HighPerformanceGridVertexBufferObject(mGridSize, DrawType.STATIC, true, Grid.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
			
		this.onUpdateVertices();
		this.onUpdateColor();
		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IGridVertexBufferObject getVertexBufferObject() {
		return this.mGridVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		pGLState.lineWidth(this.mLineWidth);
		this.mGridVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mGridVertexBufferObject.draw(GLES20.GL_LINES, mTotalVerticesNumber);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mGridVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mGridVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mGridVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IGridVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================
		
		// ===========================================================
		// Methods
		// ===========================================================
		
		public void onUpdateColor(final Grid pGrid);
		public void onUpdateVertices(final Grid pGrid);
	}
	
	public static interface IRectangleVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Grid pRectangle);
		public void onUpdateVertices(final Grid pRectangle);
	}

	public static class HighPerformanceGridVertexBufferObject extends HighPerformanceVertexBufferObject implements IGridVertexBufferObject {
		
		public HighPerformanceGridVertexBufferObject(
				final int vertices_in_poly,
				DrawType pDrawType, boolean pManaged,
				VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(vertices_in_poly, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}
		
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(Grid pGrid) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pGrid.getColor().getPacked();

			int i;
			for (i = 0; i < pGrid.mTotalVerticesNumber; i++) {
				bufferData[i * Grid.VERTEX_SIZE + Grid.COLOR_INDEX] = packedColor;
			}
				
			this.setDirtyOnHardware();			
		}

		@Override
		public void onUpdateVertices(Grid pGrid) {
			final float[] bufferData = this.mBufferData;
					
			for (int i = 0; i < pGrid.mVerticesNumberHor; i += 2) {
				float y = i/2 * pGrid.sizeY;
				bufferData[(i + 0) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_X] = 0;
				bufferData[(i + 0) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_Y] = y;
				bufferData[(i + 1) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_X] = pGrid.getWidth();
				bufferData[(i + 1) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_Y] = y;
			}
			
			for (int i = 0; i < pGrid.mVerticesNumberVer; i += 2) {
				float x = i/2 * pGrid.sizeX;
				bufferData[(i + 0 + pGrid.mVerticesNumberHor) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_X] = x;
				bufferData[(i + 0 + pGrid.mVerticesNumberHor) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_Y] = 0;
				bufferData[(i + 1 + pGrid.mVerticesNumberHor) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_X] = x;
				bufferData[(i + 1 + pGrid.mVerticesNumberHor) * Grid.VERTEX_SIZE + Grid.VERTEX_INDEX_Y] = pGrid.getHeight();
			}
			
			this.setDirtyOnHardware();			
		}
		
	}
}
package eu.nazgee.game.primitives;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class GridDouble extends Grid {

	public GridDouble(float pX, float pY, float pWidth, float pHeight,
			float sizeX, float sizeY, float pSubdivider, Color pMainColor, Color pSubColor, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, sizeX, sizeY, pVertexBufferObjectManager);
		setColor(pMainColor);
		
		Grid sub = new Grid(0, 0, pWidth, pHeight, sizeX/pSubdivider, sizeY/pSubdivider, pVertexBufferObjectManager);
		sub.setColor(pSubColor);
		attachChild(sub);
	}
}

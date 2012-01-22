package eu.nazgee.game.primitives;

import org.andengine.util.color.Color;

public class GridDouble extends Grid {

	public GridDouble(float pX, float pY, float pWidth, float pHeight,
			float sizeX, float sizeY, float pSubdivider, Color pMainColor, Color pSubColor) {
		super(pX, pY, pWidth, pHeight, sizeX, sizeY);
		setColor(pMainColor);
		
		Grid sub = new Grid(0, 0, pWidth, pHeight, sizeX/pSubdivider, sizeY/pSubdivider);
		sub.setColor(pSubColor);
		attachChild(sub);
	}
}

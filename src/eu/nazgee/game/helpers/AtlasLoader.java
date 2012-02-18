package eu.nazgee.game.helpers;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.util.debug.Debug;

import android.util.Log;

public class AtlasLoader {
	/**
	 * Builds and then loads given BuildableBitmapTextureAtlases. Uses a default BlackPawn builder
	 * @param tm TextureManager that should be used for loading
	 * @param iAtlases list of BuildableBitmapTextureAtlases to build/load
	 * @note Throws a runtime exception if building/loading failed
	 */
	static public void buildAndLoad(BuildableBitmapTextureAtlas... iAtlases) {
		buildAndLoad(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1), iAtlases);
	}
	
	/**
	 * Builds and then loads given BuildableBitmapTextureAtlases
	 * @param pBuilder Builder that should be used for building
	 * @param tm TextureManager that should be used for loading
	 * @param iAtlases list of BuildableBitmapTextureAtlases to build/load
	 * @note Throws a runtime exception if building/loading failed
	 */
	static public void buildAndLoad(BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas> pBuilder, BuildableBitmapTextureAtlas... iAtlases) {
		for (BuildableBitmapTextureAtlas atlas : iAtlases) {
			try {
				Log.d(AtlasLoader.class.getSimpleName(), "building and loading " + atlas.toString());
				atlas.build(pBuilder);
				atlas.load();
			} catch (final TextureAtlasBuilderException e) {
				Debug.e(e);
				throw new RuntimeException("Problems with loading/building Atlas");
			}
		}
	}
}

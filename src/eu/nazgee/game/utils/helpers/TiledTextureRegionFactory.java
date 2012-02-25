package eu.nazgee.game.utils.helpers;

import java.io.IOException;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.util.Log;

public class TiledTextureRegionFactory {
	/**
	 * Loads all assets from a given directory, and returns them as TiledTextureRegion
	 * 
	 * @param ctx to be used while loading
	 * @param pBasePath Assets base path of TextureRegion factory
	 * @param pPath directory containing assets
	 * @param pAtlas atlas to be used to store assets
	 * @return
	 */
	static public TiledTextureRegion loadTiles(Context ctx, String pBasePath, String pPath, BuildableBitmapTextureAtlas pAtlas) {
		return loadTiles(ctx, pBasePath, pPath, pAtlas, 0, 0);
	}

	/**
	 * Loads given number of assets from a given directory, and returns them as TiledTextureRegion
	 * 
	 * @param ctx to be used while loading
	 * @param pBasePath Assets base path of TextureRegion factory
	 * @param pPath directory containing assets
	 * @param pAtlas atlas to be used to store assets
	 * @param pSkip how many assets should be skipped before loading the first one
	 * @param pLoad how many assets shall be loaded (value<=0 means all)
	 * @return
	 */
	static public TiledTextureRegion loadTiles(Context ctx, String pBasePath, String pPath, BuildableBitmapTextureAtlas pAtlas, int pSkip, int pLoad) {
		ITextureRegion arr[] = null;

		try {
			String[] files = ctx.getAssets().list(pBasePath + pPath);
			if (pLoad > 0) {
				arr = new ITextureRegion[Math.min(files.length - pSkip, pLoad)];
			} else {
				arr = new ITextureRegion[files.length - pSkip];
			}
			int i = 0;
			for (String file : files) {
				file = pPath + "/" + file;

				if (pSkip > 0) {
					pSkip--;
					Log.d("loadAssets", "Skipping " + file);
					continue;
				}

				Log.d("loadAssets", "Loading " + file);
				ITextureRegion tex = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pAtlas, ctx, file);
				arr[i] = tex;
				i++;

				if (pLoad>0 && i>=pLoad) {
					break;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return new TiledTextureRegion(pAtlas, arr);
	}
}

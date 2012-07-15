package eu.nazgee.game.utils.helpers;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.compressed.etc1.ETC1Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
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
	static public ITextureRegion[] loadETC1(Context ctx, final TextureManager pTextureManager, String pBasePath, String pPath) {
		return loadTexturesETC1(ctx, pTextureManager, pBasePath, pPath, 0, 0);
	}
	/**
	 * Loads all assets from a given directory, and returns them as TiledTextureRegion
	 * 
	 * @param ctx to be used while loading
	 * @param pBasePath Assets base path of TextureRegion factory
	 * @param pPath directory containing assets
	 * @param pAtlas atlas to be used to store assets
	 * @param w width of svg tile
	 * @param h height of svg tile
	 * @return
	 */
	static public TiledTextureRegion loadTilesSVG(Context ctx, String pBasePath, String pPath, BuildableBitmapTextureAtlas pAtlas, int w, int h) {
		return loadTilesSVG(ctx, pBasePath, pPath, pAtlas, 0, 0, w, h);
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

	static public ITextureRegion[] loadTexturesETC1(Context ctx, final TextureManager pTextureManager, String pBasePath, String pPath, int pSkip, int pLoad) {
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
				ITextureRegion tex = getETC1(ctx, pTextureManager, pBasePath + file);
				arr[i] = tex;
				i++;

				if (pLoad>0 && i>=pLoad) {
					break;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return arr;
	}

	static public ITextureRegion getETC1(final Context ctx, final TextureManager pTextureManager, final String pFile) {
		try {
			ETC1Texture texture = new ETC1Texture(pTextureManager, TextureOptions.BILINEAR) {
				@Override
				protected InputStream getInputStream() throws IOException {
					return ctx.getAssets().open(pFile);
				}
			};
			texture.load();

			return TextureRegionFactory.extractFromTexture(texture, 0, 0, texture.getWidth(), texture.getHeight());

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Loads given number of svg assets from a given directory, and returns them as TiledTextureRegion
	 * 
	 * @param ctx to be used while loading
	 * @param pBasePath Assets base path of TextureRegion factory
	 * @param pPath directory containing assets
	 * @param pAtlas atlas to be used to store assets
	 * @param pSkip how many assets should be skipped before loading the first one
	 * @param pLoad how many assets shall be loaded (value<=0 means all)
	 * @return
	 */
	static public TiledTextureRegion loadTilesSVG(Context ctx, String pBasePath, String pPath, BuildableBitmapTextureAtlas pAtlas, int pSkip, int pLoad, int w, int h) {
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
				ITextureRegion tex = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(pAtlas, ctx, file, w, h);
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

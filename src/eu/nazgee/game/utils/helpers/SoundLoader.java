package eu.nazgee.game.utils.helpers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

import android.content.Context;
import android.util.Log;

public class SoundLoader {
	/**
	 * Loads given music file using given context and SoundManager
	 * @param pSoundManager 
	 * @param pContext
	 * @param filename soundfile that is supposed to be loaded
	 * @return
	 */
	static public Music loadMusic(final MusicManager pMusicManager, final Context pContext, final String filename) {
		Music snd;
		try {
			snd = MusicFactory.createMusicFromAsset(pMusicManager, pContext, filename);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("SoundLoader could not load " + filename + "! ex=" + ex);
		}
		return snd;
	}
	/**
	 * Loads given sound file using given context and SoundManager
	 * @param pSoundManager 
	 * @param pContext
	 * @param filename soundfile that is supposed to be loaded
	 * @return
	 */
	static public Sound load(final SoundManager pSoundManager, final Context pContext, final String filename) {
		Sound snd;
		try {
			snd = SoundFactory.createSoundFromAsset(pSoundManager, pContext, filename);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("SoundLoader could not load " + filename + "! ex=" + ex);
		}
		return snd;
	}

	static public Sound[] loadMultiple(final SoundManager pSoundManager, final Context ctx, String pBasePath, String pPath) {
		return loadMultiple(pSoundManager, ctx, pBasePath, pPath, 0, 0);
	}

	static public Sound[] loadMultiple(final SoundManager pSoundManager, final Context ctx, String pBasePath, String pPath, int pSkip, int pLoad) {
		Sound arr[] = null;

		try {
			String[] files = ctx.getAssets().list(pBasePath + pPath);
			if (pLoad > 0) {
				arr = new Sound[Math.min(files.length - pSkip, pLoad)];
			} else {
				arr = new Sound[files.length - pSkip];
			}
			int i = 0;
			for (String file : files) {
				file = pPath + "/" + file;

				if (pSkip > 0) {
					pSkip--;
					Log.d("loadMultiple", "Skipping " + file);
					continue;
				}

				Log.d("loadMultiple", "Loading " + file);
				Sound snd = load(pSoundManager, ctx, file);
				arr[i] = snd;
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
}

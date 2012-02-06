package eu.nazgee.game.misc;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.algorithm.path.Path;
import org.andengine.util.modifier.ease.IEaseFunction;

public class AStarPathModifierTime extends AStarPathModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap) {
		this(pDuration, pPath, pMap, null, null,
				EASE_DEFAULT);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap, final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pMap, null, null, pEaseFunction);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener) {
		this(pDuration, pPath, pMap, pEntityModiferListener, null,
				EASE_DEFAULT);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IAStarPathModifierListener pPathModifierListener) {
		this(pDuration, pPath, pMap, null, pPathModifierListener,
				EASE_DEFAULT);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IAStarPathModifierListener pPathModifierListener,
			final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pMap, null, pPathModifierListener,
				pEaseFunction);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pMap, pEntityModiferListener, null,
				pEaseFunction);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IAStarPathModifierListener pPathModifierListener)
			throws IllegalArgumentException {
		this(pDuration, pPath, pMap, pEntityModiferListener,
				pPathModifierListener, EASE_DEFAULT);
	}

	public AStarPathModifierTime(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IAStarPathModifierListener pPathModifierListener,
			final IEaseFunction pEaseFunction) throws IllegalArgumentException {
		super(pDuration, pPath, pMap, pEntityModiferListener, pPathModifierListener, pEaseFunction);
	}
	
	@Override
	public IEntityModifier deepCopy()
			throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {

		return null;
	}

	protected AStarPathModifierTime(final AStarPathModifierTime pPathModifier)
			throws CloneNotSupportedException {
		super(pPathModifier);
	}

	@Override
	public AStarPathModifierTime clone() throws CloneNotSupportedException {
		return new AStarPathModifierTime(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	MoveModifier populateMoveMod(final Path pPath, final TMXTile from, final TMXTile to, final float pParam, final float pLen, final IEaseFunction pEaseFunction) {
		final float w = to.getTileWidth()/2;
		final float h = to.getTileHeight()/2;
		
		return new MoveModifier(pParam, 
				from.getTileX()+w, to.getTileX()+w,
				from.getTileY()+h, to.getTileY()+h, 
				null, pEaseFunction);
	}
}
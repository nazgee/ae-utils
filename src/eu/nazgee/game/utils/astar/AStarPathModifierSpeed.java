package eu.nazgee.game.utils.astar;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.algorithm.path.Path;
import org.andengine.util.modifier.ease.IEaseFunction;

public class AStarPathModifierSpeed extends AStarPathModifier {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap) {
		this(pSpeed, pPath, pMap, null, null,
				EASE_DEFAULT);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap, final IEaseFunction pEaseFunction) {
		this(pSpeed, pPath, pMap, null, null, pEaseFunction);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener) {
		this(pSpeed, pPath, pMap, pEntityModiferListener, null,
				EASE_DEFAULT);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap,
			final IAStarPathModifierListener pPathModifierListener) {
		this(pSpeed, pPath, pMap, null, pPathModifierListener,
				EASE_DEFAULT);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap,
			final IAStarPathModifierListener pPathModifierListener,
			final IEaseFunction pEaseFunction) {
		this(pSpeed, pPath, pMap, null, pPathModifierListener,
				pEaseFunction);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IEaseFunction pEaseFunction) {
		this(pSpeed, pPath, pMap, pEntityModiferListener, null,
				pEaseFunction);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IAStarPathModifierListener pPathModifierListener)
			throws IllegalArgumentException {
		this(pSpeed, pPath, pMap, pEntityModiferListener,
				pPathModifierListener, EASE_DEFAULT);
	}

	public AStarPathModifierSpeed(final float pSpeed, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IAStarPathModifierListener pPathModifierListener,
			final IEaseFunction pEaseFunction) throws IllegalArgumentException {
		super(pSpeed, pPath, pMap, pEntityModiferListener, pPathModifierListener, pEaseFunction);
	}
	
	@Override
	public IEntityModifier deepCopy()
			throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {

		return null;
	}

	protected AStarPathModifierSpeed(final AStarPathModifierSpeed pPathModifier)
			throws CloneNotSupportedException {
		super(pPathModifier);
	}

	@Override
	public AStarPathModifierSpeed clone() throws CloneNotSupportedException {
		return new AStarPathModifierSpeed(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Path getPath() {
		return this.mPath;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	MoveModifier populateMoveMod(final Path pPath, final TMXTile from, final TMXTile to, final float pParam, final float pLen, final IEaseFunction pEaseFunction) {
		final float w = to.getTileWidth()/2;
		final float h = to.getTileHeight()/2;

		return new MoveModifier(pLen / pParam, 
				from.getTileX()+w, to.getTileX()+w,
				from.getTileY()+h, to.getTileY()+h, 
				null, pEaseFunction);
	}
}
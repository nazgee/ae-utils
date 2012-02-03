package eu.nazgee.game.misc;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.modifier.ease.IEaseFunction;
import org.andengine.util.path.Path;

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
	MoveModifier[] populateMoveMods() {
		final MoveModifier[] moveModifiers = new MoveModifier[mPath.getLength() - 1];

		final float velocity = mDuration;

		final int modifierCount = moveModifiers.length;
		for (int i = 0; i < modifierCount; i++) {
			final float duration = getSegmentLength(i) / velocity;
			moveModifiers[i] = new MoveModifier(duration, getXCoordinates(i),
					getXCoordinates(i + 1), getYCoordinates(i),
					getYCoordinates(i + 1), null, mEaseFunction);
		}
		return moveModifiers;
	}
}
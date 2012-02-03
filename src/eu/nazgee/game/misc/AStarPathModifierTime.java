package eu.nazgee.game.misc;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.modifier.ease.IEaseFunction;
import org.andengine.util.path.Path;

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
	MoveModifier[] populateMoveMods() {
		final MoveModifier[] moveModifiers = new MoveModifier[mPath.getLength() - 1];

		final float velocity = mPathLengthInCoord / mDuration;

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
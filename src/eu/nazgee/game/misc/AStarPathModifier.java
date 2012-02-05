package eu.nazgee.game.misc;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.algorithm.path.Path;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.SequenceModifier;
import org.andengine.util.modifier.SequenceModifier.ISubSequenceModifierListener;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

abstract public class AStarPathModifier extends EntityModifier implements IAStarPathModifier {
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final IEaseFunction EASE_DEFAULT = EaseLinear.getInstance();
	// ===========================================================
	// Fields
	// ===========================================================

	protected final SequenceModifier<IEntity> mSequenceModifier;
	protected IAStarPathModifierListener mPathModifierListener;
	protected final Path mPath;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap) {
		this(pDuration, pPath, pMap, null, null,
				EASE_DEFAULT);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap, final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pMap, null, null, pEaseFunction);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener) {
		this(pDuration, pPath, pMap, pEntityModiferListener, null,
				EASE_DEFAULT);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IAStarPathModifierListener pPathModifierListener) {
		this(pDuration, pPath, pMap, null, pPathModifierListener,
				EASE_DEFAULT);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IAStarPathModifierListener pPathModifierListener,
			final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pMap, null, pPathModifierListener,
				pEaseFunction);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pMap, pEntityModiferListener, null,
				pEaseFunction);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IAStarPathModifierListener pPathModifierListener)
			throws IllegalArgumentException {
		this(pDuration, pPath, pMap, pEntityModiferListener,
				pPathModifierListener, EASE_DEFAULT);
	}

	public AStarPathModifier(final float pDuration, final Path pPath,
			final TMXTiledMap pMap,
			final IEntityModifierListener pEntityModiferListener,
			final IAStarPathModifierListener pPathModifierListener,
			final IEaseFunction pEaseFunction) throws IllegalArgumentException {
		super(pEntityModiferListener);

		final int pathSize = pPath.getLength();

		if (pathSize < 2) {
			throw new IllegalArgumentException(
					"Path needs at least 2 waypoints!");
		}

		this.mPath = pPath;
		this.mPathModifierListener = pPathModifierListener;

		final MoveModifier[] moveModifiers = populateMoveMods(pMap, pPath, pEaseFunction, pDuration);

		/*
		 * Create a new SequenceModifier and register the listeners that call
		 * through to mEntityModifierListener and mPathModifierListener.
		 */
		this.mSequenceModifier = new SequenceModifier<IEntity>(
				new ISubSequenceModifierListener<IEntity>() {
					@Override
					public void onSubSequenceStarted(
							final IModifier<IEntity> pModifier,
							final IEntity pEntity, final int pIndex) {
						if (pIndex < pathSize) {
							if (AStarPathModifier.this.mPathModifierListener != null) {
								AStarPathModifier.this.mPathModifierListener
										.onNextMove(
												AStarPathModifier.this,
												pEntity, pIndex, pPath.getDirectionToNextStep(pIndex));
							}
						}

						if (AStarPathModifier.this.mPathModifierListener != null) {
							AStarPathModifier.this.mPathModifierListener
									.onPathWaypointStarted(
											AStarPathModifier.this, pEntity,
											pIndex);
						}
					}

					@Override
					public void onSubSequenceFinished(
							final IModifier<IEntity> pEntityModifier,
							final IEntity pEntity, final int pIndex) {
						if (AStarPathModifier.this.mPathModifierListener != null) {
							AStarPathModifier.this.mPathModifierListener
									.onPathWaypointFinished(
											AStarPathModifier.this, pEntity,
											pIndex);
						}
					}
				}, new IEntityModifierListener() {
					@Override
					public void onModifierStarted(
							final IModifier<IEntity> pModifier,
							final IEntity pEntity) {
						AStarPathModifier.this.onModifierStarted(pEntity);
						if (AStarPathModifier.this.mPathModifierListener != null) {
							AStarPathModifier.this.mPathModifierListener
									.onPathStarted(AStarPathModifier.this,
											pEntity);
						}
					}

					@Override
					public void onModifierFinished(
							final IModifier<IEntity> pEntityModifier,
							final IEntity pEntity) {
						AStarPathModifier.this.onModifierFinished(pEntity);
						if (AStarPathModifier.this.mPathModifierListener != null) {
							AStarPathModifier.this.mPathModifierListener
									.onPathFinished(AStarPathModifier.this,
											pEntity);
						}
					}
				}, moveModifiers);
	}

	MoveModifier[] populateMoveMods(final TMXTiledMap pMap, final Path pPath, final IEaseFunction pEaseFunction, final float pParam) {
		int len_y = pMap.getTileWidth();
		int len_x = pMap.getTileHeight();
		int len_diag = (int) Math.sqrt(len_y * len_y + len_x * len_x);
		final MoveModifier[] moveModifiers = new MoveModifier[pPath.getLength() - 1];

		TMXTile[][] tiles = pMap.getTMXLayers().get(0).getTMXTiles();

		int x1;
		int y1;
		int x2 = pPath.getX(0);
		int y2 = pPath.getY(0);
		int len = 0;
		for (int i = 1; i <= moveModifiers.length; i++) {
			x1 = x2;
			y1 = y2;
			x2 = pPath.getX(i);
			y2 = pPath.getY(i);

			if (x1 == x2) {
				len = len_y;
			} else if (y1 == y2) {
				len = len_x;
			} else {
				len = len_diag;
			}

			TMXTile from = tiles[y1][x1];
			TMXTile to = tiles[y2][x2];

			moveModifiers[i-1] = populateMoveMod(pPath, from, to, pParam, len, pEaseFunction);
		}
		return moveModifiers;
	}

	abstract MoveModifier populateMoveMod(final Path pPath, final TMXTile from, final TMXTile to, final float pParam, final float pLen, final IEaseFunction pEaseFunction);

	@Override
	public IEntityModifier deepCopy()
			throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {

		return null;
	}

	protected AStarPathModifier(final AStarPathModifier pPathModifier)
			throws CloneNotSupportedException {
		this.mPath = new Path(pPathModifier.getPath().getLength());
		for (int i = 0; i < pPathModifier.getPath().getLength(); i++) {
			this.mPath.set(i, pPathModifier.getPath().getX(i),
					pPathModifier.getPath().getY(i));
		}
		this.mSequenceModifier = pPathModifier.mSequenceModifier.deepCopy();
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
	public boolean isFinished() {
		return this.mSequenceModifier.isFinished();
	}

	@Override
	public float getSecondsElapsed() {
		return this.mSequenceModifier.getSecondsElapsed();
	}

	@Override
	public float getDuration() {
		return this.mSequenceModifier.getDuration();
	}

	public IAStarPathModifierListener getPathModifierListener() {
		return this.mPathModifierListener;
	}

	public void setPathModifierListener(
			final IAStarPathModifierListener pPathModifierListener) {
		this.mPathModifierListener = pPathModifierListener;
	}

	@Override
	public void reset() {
		this.mSequenceModifier.reset();
	}

	@Override
	public float onUpdate(final float pSecondsElapsed, final IEntity pEntity) {
		return this.mSequenceModifier.onUpdate(pSecondsElapsed, pEntity);
	}
}

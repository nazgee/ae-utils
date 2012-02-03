package eu.nazgee.game.misc;

import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.SequenceModifier;
import org.andengine.util.modifier.SequenceModifier.ISubSequenceModifierListener;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;
import org.andengine.util.path.Path;

import android.util.FloatMath;

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

	protected int mPathLengthInCoord;

	protected final IEaseFunction mEaseFunction;

	protected final float mDuration;
	
	private final int mTileW;
	private final int mTileH;

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
		mDuration = pDuration;
		mEaseFunction = pEaseFunction;
		mTileH = pMap.getTileHeight();
		mTileW = pMap.getTileWidth();
		
		final int pathSize = pPath.getLength();

		if (pathSize < 2) {
			throw new IllegalArgumentException(
					"Path needs at least 2 waypoints!");
		}

		mPathLengthInCoord = 0;

		// Get the length of the path in coordinates
		// XXX this does not account to diagonal movements!
		for (int i = 1; i < pathSize; i++) {
			if (pPath.getX(i) != pPath.getX(i - 1)) {
				mPathLengthInCoord += mTileW;
			} else {
				mPathLengthInCoord += mTileH;
			}
		}

		this.mPath = pPath;
		this.mPathModifierListener = pPathModifierListener;

		final MoveModifier[] moveModifiers = populateMoveMods();

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
	
	abstract MoveModifier[] populateMoveMods();
	
	@Override
	public IEntityModifier deepCopy()
			throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {

		return null;
	}

	// FIXME get this from map- this is wrong, when map was moved somewhere
	protected float getXCoordinates(int pIndex) {
		return mPath.getX(pIndex) * mTileW;
	}

	// FIXME get this from map- this is wrong, when map was moved somewhere
	protected float getYCoordinates(int pIndex) {
		return mPath.getY(pIndex) * mTileH;
	}

	protected float getSegmentLength(int pIndex) {
		final int nextSegmentIndex = pIndex + 1;
		final float dx = getXCoordinates(pIndex) - getXCoordinates(nextSegmentIndex);
		final float dy = getYCoordinates(pIndex) - getYCoordinates(nextSegmentIndex);
		
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	protected AStarPathModifier(final AStarPathModifier pPathModifier)
			throws CloneNotSupportedException {
		this.mPath = new Path(pPathModifier.getPath().getLength());
		for (int i = 0; i < pPathModifier.getPath().getLength(); i++) {
			this.mPath.set(i, pPathModifier.getPath().getX(i),
					pPathModifier.getPath().getY(i));
		}
		this.mEaseFunction = pPathModifier.mEaseFunction;
		this.mDuration = pPathModifier.mDuration;
		this.mTileH = pPathModifier.mTileH;
		this.mTileW = pPathModifier.mTileW;
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

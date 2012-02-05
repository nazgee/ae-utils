package eu.nazgee.game.misc;

import org.andengine.entity.IEntity;
import org.andengine.util.algorithm.path.Direction;

public interface IAStarPathModifierListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public void onPathStarted(final IAStarPathModifier pPathModifier,
			final IEntity pEntity);

	public void onNextMove(IAStarPathModifier aStarPathModifier,
			IEntity pEntity, int pIndex, Direction pDirection);

	public void onPathWaypointStarted(
			final IAStarPathModifier pPathModifier, final IEntity pEntity,
			final int pWaypointIndex);

	public void onPathWaypointFinished(
			final IAStarPathModifier pPathModifier, final IEntity pEntity,
			final int pWaypointIndex);

	public void onPathFinished(final IAStarPathModifier pPathModifier,
			final IEntity pEntity);

}

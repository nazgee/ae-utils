package eu.nazgee.game.utils.astar;

import org.andengine.entity.IEntity;
import org.andengine.util.adt.spatial.Direction;

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

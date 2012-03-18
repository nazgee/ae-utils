package eu.nazgee.game.utils.schedule;

import java.util.LinkedList;

import org.andengine.engine.handler.IUpdateHandler;

public class Scheduler implements IScheduler, IUpdateHandler {
	LinkedList<Runnable> mScheduled = new LinkedList<Runnable>();

	public Scheduler() {
	}

	@Override
	public void schedule(Runnable rnbl) {
		synchronized (mScheduled) {
			mScheduled.add(rnbl);
		}
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		synchronized (mScheduled) {
			for (Runnable rnb : mScheduled) {
				rnb.run();
			}
			mScheduled.clear();
		}
	}

	@Override
	public void reset() {
		synchronized (mScheduled) {
			mScheduled.clear();
		}
	}
}

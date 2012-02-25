package eu.nazgee.game.utils.activity;

/**
 * Represents task that should be done in the background
 * @author nazgee
 *
 */
public interface IAsyncTasklet {
	/**
	 * Called in UI thread- should be used to sync with the rest of the App
	 */
	public void onAboutToStart();
	/**
	 * Called in background thread- main worker code should be here
	 */
	public void workToDo();
	/**
	 * Called in UI thread- should be used to sync with the rest of the App
	 */
	public void onComplete();
}

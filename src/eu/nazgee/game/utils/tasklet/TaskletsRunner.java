package eu.nazgee.game.utils.tasklet;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Class that can be used to to run IAsyncTasklets in background
 * @author nazgee
 *
 */
public class TaskletsRunner extends
		AsyncTask<IAsyncTasklet, Integer, Boolean> {

	IAsyncTasklet[] mParams;

	public TaskletsRunner(IAsyncTasklet... pParams) {
		super();
		mParams = pParams;
	}

	@Override
	protected void onPreExecute() {
		Log.d(getClass().getName(), "Starting onPreExecute");
		int count = mParams.length;
		for (int i = 0; i < count; i++) {
			mParams[i].onAboutToStart();
		}
		Log.d(getClass().getName(), "onPreExecute is complete");
	}

	@Override
	protected Boolean doInBackground(IAsyncTasklet... params) {
		Log.d(getClass().getName(), "Starting background work; " + this.toString());

		int count = params.length;
		for (int i = 0; i < count; i++) {
			params[i].workToDo();
		}

		Log.d(getClass().getName(), "All background work is complete " + this.toString());
		return true;
	}

	protected void onPostExecute(Boolean result) {
		Log.d(getClass().getName(), "Starting onPostExecute");
		int count = mParams.length;
		for (int i = 0; i < count; i++) {
			mParams[i].onComplete();
		}
		Log.d(getClass().getName(), "onPostExecute is complete");
	}
}

package eu.nazgee.game.ui.activity;

import android.os.AsyncTask;

/**
 * Class that can be used to to run IAsyncTasklets in background
 * @author nazgee
 *
 */
public class TaskletsRunner extends
		AsyncTask<IAsyncTasklet, Integer, Boolean> {

	IAsyncTasklet[] mParams;

	@Override
	protected Boolean doInBackground(IAsyncTasklet... params) {
		this.mParams = params;
		int count = params.length;
		for (int i = 0; i < count; i++) {
			params[i].workToDo();
		}
		
		return true;
	}

	protected void onPostExecute(Boolean result) {
		int count = this.mParams.length;
		for (int i = 0; i < count; i++) {
			this.mParams[i].onComplete();
		}
	}
}

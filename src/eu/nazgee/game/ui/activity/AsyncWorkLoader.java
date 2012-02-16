package eu.nazgee.game.ui.activity;

import android.os.AsyncTask;

public class AsyncWorkLoader extends
		AsyncTask<IAsyncWorkCallback, Integer, Boolean> {

	IAsyncWorkCallback[] _params;

	@Override
	protected Boolean doInBackground(IAsyncWorkCallback... params) {
		this._params = params;
		int count = params.length;
		for (int i = 0; i < count; i++) {
			params[i].workToDo();
		}
		
		return true;
	}

	protected void onPostExecute(Boolean result) {
		int count = this._params.length;
		for (int i = 0; i < count; i++) {
			this._params[i].onComplete();
		}
	}
}

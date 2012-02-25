package eu.nazgee.game.utils.misc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class AppRater {
	public static void app_launched(Context ctx, String pAppName, String pPckgName, Drawable pIcon, int pDaysUntilPrompt, int pLaunchesUntilPrompt) {
		SharedPreferences prefs = ctx.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		// Wait at least n days before opening
		if (launch_count >= pLaunchesUntilPrompt) {
			if (System.currentTimeMillis() >= date_firstLaunch
					+ (pDaysUntilPrompt * 24 * 60 * 60 * 1000)) {
				showRateDialog(ctx, editor, pAppName, pPckgName, pIcon);
			}
		}

		editor.commit();
	}

	public static void showRateDialog(final Context ctx,
			final SharedPreferences.Editor editor,
			final String pAppName, final String pPckgName, final Drawable pIcon) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setTitle("Can you rate this app, please?")
				.setMessage(
						"Could you give "
								+ pAppName
								+ " some stars? I'd love to hear how you like it!")
				.setCancelable(false)
				.setIcon(pIcon)
				.setPositiveButton("Rate " + pAppName,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ctx.startActivity(new Intent(
										Intent.ACTION_VIEW, Uri
												.parse("market://details?id="
														+ pPckgName)));
								if (editor != null) {
									editor.putBoolean("dontshowagain", true);
									editor.commit();
								}
								dialog.dismiss();
							}
						})
				.setNeutralButton("Later",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						})
				.setNegativeButton("No, thanks",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								editor.putBoolean("dontshowagain", true);
								editor.commit();
							}
						});

		final Dialog dialog = builder.create();
		dialog.show();
	}
}
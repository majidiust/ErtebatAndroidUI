package org.ertebat.ui.dialogs;

import org.ertebat.R;
import org.ertebat.ui.BaseActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * A custom dialog used to display message when waiting for the completion of an
 * action
 * 
 * @author Morteza Sabetraftar
 * 
 */
public class WaitingDialog extends Dialog {
	/**
	 * Public constructor
	 * 
	 * @param context
	 *            Context of the activity that is creating the dialog
	 * @param dialogId
	 *            ID by which the dialog is recognized
	 * @param timeout
	 *            Number of milliseconds before the dialog is closed
	 * @param message
	 *            The message to display
	 */
	public WaitingDialog(Context context, int dialogId, long timeout, String message) {
		super(context, dialogId, timeout, message, R.layout.dialog_waiting);
		LOG_TAG = "WaitingDialog";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView text = (TextView) findViewById(R.id.txtWaitingDialogMessage);
		text.setTypeface(BaseActivity.FontRoya);
		text.setText(mMessage);

		setCancelable(false);

		startTimeout();
	}
}

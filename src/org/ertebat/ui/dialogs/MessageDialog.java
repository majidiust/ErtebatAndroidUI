package org.ertebat.ui.dialogs;

import org.ertebat.R;
import org.ertebat.ui.BaseActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple dialog to present the user with a given message. User will need to
 * acknowledge the message before the dialog is closed
 * 
 * @author Morteza Sabetraftar
 * 
 */
public class MessageDialog extends Dialog {
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
	public MessageDialog(Context context, int dialogId, long timeout, String message) {
		super(context, dialogId, timeout, message, R.style.CustomDialog);

		LOG_TAG = "MessageDialog";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView text = (TextView) findViewById(R.id.txtMessageDialogMessage);
		text.setTypeface(BaseActivity.FontRoya);
		text.setText(mMessage);

		Button button = (Button) findViewById(R.id.btnMessageDialogAcknowledge);
		button.setTypeface(BaseActivity.FontKoodak);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

		setCancelable(false);

		startTimeout();
	}
}

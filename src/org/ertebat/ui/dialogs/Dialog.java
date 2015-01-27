package org.ertebat.ui.dialogs;

import org.ertebat.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

/**
 * A custom and generic dialog class that parents other specific dialogs. The
 * class cannot be instantiated and is only extended by other dialog classes.
 * 
 * @author Morteza Sabetraftar
 * 
 */
public class Dialog extends android.app.Dialog {
	/**
	 * The timeout value to use when creating a dialog without a timeout
	 */
	public static final long TIMEOUT_NONE = 0;

	/**
	 * The tag to use when submitting log messages
	 */
	protected String LOG_TAG = "Dialog";

	/**
	 * ID of the dialog, used to identify the dialog upon closing
	 */
	protected int mDialogId;
	/**
	 * Resource ID of the layout to use with the dialog
	 */
	protected int mViewId;
	/**
	 * Dialog timeout in milliseconds after which the dialog is closed and the
	 * timeout event is fired
	 */
	protected long mTimeout;
	/**
	 * The message to display in the dialog
	 */
	protected String mMessage;
	/**
	 * Handler to facilitate execution of code on the UI thread by other threads
	 */
	protected Handler mHandler;
	/**
	 * The listener responsible for handling the events fired by the dialog
	 */
	protected DialogListener mListener;

	/**
	 * Protected constructor to make it accessible only to child classes
	 * 
	 * @param context
	 *            Context of the activity that is creating the dialog
	 * @param dialogId
	 *            ID by which the dialog is recognized
	 * @param timeout
	 *            Number of milliseconds before the dialog is closed
	 * @param message
	 *            The message to display
	 * @param viewId
	 *            Resource ID of the layout to use with the dialog
	 */
	protected Dialog(Context context, int dialogId, long timeout, String message, int viewId) {
		super(context, R.style.CustomDialog);

		mDialogId = dialogId;
		mViewId = viewId;
		mTimeout = timeout;
		mMessage = message;
		mHandler = new Handler();
		mListener = (DialogListener) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mViewId);

		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected void onStop() {
		Log.d(LOG_TAG, "Dialog " + mDialogId + " canceling timeout!");
		mHandler.removeCallbacksAndMessages(null);

		super.onStop();
	}

	/**
	 * If a timeout is supplied to the dialog, this will ensure that dialog is
	 * closed when it times out
	 */
	protected void startTimeout() {
		if (mTimeout != TIMEOUT_NONE) {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Log.d(LOG_TAG, "Dialog " + mDialogId + " timed out!");
					mListener.onDialogTimedOut(mDialogId);
					dismiss();
				}
			}, mTimeout);
		}
	}

	/**
	 * Defines the methods to be implemented by any class that displays and
	 * handles a dialog.
	 * 
	 * @author Morteza Sabetraftar
	 * 
	 */
	public interface DialogListener {
		/**
		 * Handles the time-out event of the dialog
		 * 
		 * @param dialogId
		 *            ID of the dialog that has timed out
		 */
		public void onDialogTimedOut(int dialogId);
	}
}

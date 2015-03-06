package org.ertebat.ui;

import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.events.NgnInviteEventTypes;
import org.ertebat.R;
import org.ertebat.pdb.DatabaseUtility;
import org.ertebat.schema.FriendSchema;
import org.ertebat.schema.MessageSchema;
import org.ertebat.schema.RoomSchema;
import org.ertebat.schema.SessionStore;
import org.ertebat.transport.INGN;
import org.ertebat.transport.ITransport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragment extends Fragment implements ITransport, INGN {

	protected static String LOG_TAG = "";
	// protected BroadcastReceiver mSipBroadCastRecv;
	// protected NgnEngine mEngine;
	// protected INgnConfigurationService mConfigurationService;
	// protected INgnSipService mSipService;
	// protected NgnAVSession mAVSession;
	// protected INgnHistoryService mHistoryService;
	// protected CallActivityStatus mCurrentStatus =
	// CallActivityStatus.CAS_Idle;
	protected Activity Me = null;
	protected String mClassName;
	protected static int mLastCommand = 0;

	protected Handler mHandler;
	protected ProgressDialog mDialog;
	protected Context This = null;
	protected BaseActivity mBaseActivity = null;
	protected DatabaseUtility mDatabase;
	protected View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mHandler = new Handler();
		This = container.getContext();

		mBaseActivity = (BaseActivity) getActivity();
		((BaseActivity) getActivity()).registerToTransportListeners(this);
		((BaseActivity) getActivity()).registerToNGNListeners(this);
		mDatabase = mBaseActivity.mDatabase;
		
		return null;
	}

	protected void ShowToast(final String message) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(This, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void showAlert(final String message) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(This);
				builder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// do things
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	protected void ShowDialog(final String title, final String message) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mDialog = ProgressDialog.show(This, title, message, true);
				TextView tvMessage = (TextView) mDialog.findViewById(android.R.id.message);
				tvMessage.setGravity(Gravity.LEFT);
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((BaseActivity) getActivity()).unRegisterFromTransportListeners(this);

	}

	protected void CloseDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	@Override
	public void onConnectedToServer() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				onServerConnectionChanged(true);		
			}
		});
	}

	@Override
	public void onDisconnctedFromServer() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				onServerConnectionChanged(false);		
			}
		});
	}

	@Override
	public void onNewFriend(FriendSchema fs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewRoom(RoomSchema rs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewMessage(MessageSchema ms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCurrentProfileResult(String username, String userId, String firstName, String lastName,
			String mobile, String email) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthorizationRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthorized() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserProfile(String firstName, String lastName, String uid, String userName, String picUrl,
			String email) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomAdded(String roomName, String roomId, String roomDesc, String roomLogo, String roomType,
			String members) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMembersAddedToRoom(String roomId, String memberId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void notifyAddedByFriend(String invitedBy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyAddedToRoom(String invitedBy, String roomId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetCallState(NgnInviteEventTypes callState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnIncommingCall(NgnInviteEventArgs args) {
		// TODO Auto-generated method stub

	}

	public void onServerConnectionChanged(boolean isConnected) {
		mRootView.setBackgroundResource(isConnected ? R.color.main_bg : R.color.main_bg_disconnected);
	}

	@Override
	public void onFriendConnectivityStatusChanged(String friendId,
			String connectivityStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFriendTyping(String friendId, String roomId) {
		// TODO Auto-generated method stub
		
	}
}

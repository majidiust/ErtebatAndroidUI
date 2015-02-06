package org.ertebat.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ertebat.R;
import org.ertebat.schema.MessageSchema;
import org.ertebat.schema.RoomSchema;
import org.ertebat.schema.SessionStore;
import org.ertebat.transport.ITransport;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public class ChatActivity extends BaseActivity {
	private ListView mListViewMessages;
	private List<ChatMessage> mDataSet;
	private ChatMessageAdapter mAdapter;

	private Button mBtnSendMessage;
	private Button mBtnBarMore;
	private Button mBtnBarAddPicture;
	private Button mBtnBarAddContact;
	private EditText mEditMessageContent;
	private RelativeLayout mLayoutBarExtension;
	private TextView mTextContactTyping;
	private String mRoomId = "";
	private String mOtherParty = "";

	private Object mAddMessageLock = new Object();
	private int maxWidth = 1280;
	private int maxHeight = 720;

	ReentrantLock lock = new ReentrantLock();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		getActionBar().hide();
		TAG = "ChatActivity";

		String content, messageId, from, date, fromId;
		try {
			Bundle b = getIntent().getExtras();
			mRoomId = b.getString("roomId");
			mOtherParty = b.getString("otherParty");
			String origin = b.getString("origin");
			SessionStore.mSessionStore.mCurrentRoomId = mRoomId;

			if (origin.equals("notification")) {
				content = b.getString("message");
				messageId = b.getString("messageId");
				from = b.getString("from");
				date = b.getString("date");
				fromId = b.getString("fromId");
				showToast(messageId + " : " + from + " : " + mRoomId);
				SessionStore.mSessionStore.addMessageToRoom(new MessageSchema(messageId, fromId, from, mRoomId, date,
						date, content));
			}
		} catch (Exception ex) {
			Log.d(TAG, ex.getMessage());
		}

		// //////////////////////////////////////ListView Setup
		// ////////////////////////////////////////

		mListViewMessages = (ListView) findViewById(R.id.listViewChatMessages);

		mDataSet = new ArrayList<ChatMessage>();
		mAdapter = new ChatMessageAdapter(This, BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact),
				mDataSet);
		mListViewMessages.setAdapter(mAdapter);

		// TODO: @Majid, this is for UI test. remember to remove it later
		// loadSampleData();
		loadHistory();

		mTextContactTyping = (TextView) findViewById(R.id.txtChatContactTyping);
		mTextContactTyping.setTypeface(FontKoodak);

		// ////////////////////////////////////// Bottom Bar Setup
		// /////////////////////////////////////////

		setupBottomBar();
		onContactTypingChanged(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkMessages();
	}

	@Override
	protected void onMultiChoiceDialogResult(String dialogTitle, String callback, int selectedIndex) {
		try {
			Method method = ChatActivity.class.getDeclaredMethod(callback, int.class);
			method.invoke(this, selectedIndex);
		} catch (Exception ex) {
			Log.d(TAG, ex.getMessage());
		}
	}

	private void onMessageDelivered(String roomId, int index) {
		try{
			if(roomId.equals(mRoomId)){
				mDataSet.get(index).IsDelivered = true;
				mAdapter.notifyDataSetChanged();
			}
		}
		catch(Exception ex){
			logCatDebug(ex.getMessage());
		}
	}

	private void onContactTypingChanged(final boolean isTyping) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				try{
					mTextContactTyping.setVisibility(isTyping ? View.VISIBLE : View.GONE);
				}catch(Exception ex){
					logCatDebug(ex.getMessage());
				}
			}
		});
		
	}

	private void onPictureInsertionMethodSelected(int selectedIndex) {
		Log.d(TAG, "selected picture insertion method: " + selectedIndex);
		PictureInsertionMethod method = PictureInsertionMethod.values()[selectedIndex];

		if (method == PictureInsertionMethod.FromGallery) {
			Intent pickPhoto = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(pickPhoto, DIALOG_PICTURE_GALLERY);
		} else {
			Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePicture, DIALOG_TAKE_PICTURE);
		}
	}

	private void onAdditionalContactSelected(int selectedIndex) {
		String selectedContactId = mContacts.get(selectedIndex).ContactPhone;

		// TODO: @Majid, do whatever you want to the ID
	}

	private void setupBottomBar() {
		mEditMessageContent = (EditText) findViewById(R.id.editChatBottomMessage);
		mEditMessageContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//	if(lock.tryLock()){
				announceTyping(mRoomId);
				//	new Thread(new Runnable() {
				//	@Override
				//				public void run() {
				//				try{
				//				Thread.sleep(1000);
				//					}
				//					catch(Exception ex)
				//					{
				//						logCatDebug(ex.getMessage());
				//					}
				//					finally{
				//						lock.unlock();
				//					}
				//				}
				//			}).start();
				//		}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		mBtnSendMessage = (Button) findViewById(R.id.btnChatBottomSend);
		mBtnSendMessage.setTypeface(FontKoodak);
		mBtnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mEditMessageContent.getText().toString().equals("")) {

					//we add it to the data set and show it not delivered, after we send it via restfull we mark it as delivered
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
					String currentDateandTime = sdf.format(new Date());
					int location = addLocalMessage(mEditMessageContent.getText().toString(), currentDateandTime);

					sendTextMessageToServer(location, mRoomId, "Now", "..", mEditMessageContent.getText().toString());
					mEditMessageContent.setText("");
				}
			}
		});

		mLayoutBarExtension = (RelativeLayout) findViewById(R.id.layoutChatBottomExtensionBar);

		mBtnBarMore = (Button) findViewById(R.id.btnChatBottomMore);
		mBtnBarMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLayoutBarExtension.getVisibility() != View.VISIBLE) {
					mLayoutBarExtension.setVisibility(View.VISIBLE);
				} else {
					mLayoutBarExtension.setVisibility(View.GONE);
				}
			}
		});

		mBtnBarAddPicture = (Button) findViewById(R.id.btnChatBottomAddPic);
		mBtnBarAddPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String[] choices = new String[] { PictureInsertionMethod.FromGallery.toString(),
						PictureInsertionMethod.FromCamera.toString() };
				showMultiChoiceDialog(choices, "onPictureInsertionMethodSelected", "نحوه انتخاب تصویر");
			}
		});

		mBtnBarAddContact = (Button) findViewById(R.id.btnChatBottomAddContact);
		mBtnBarAddContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMultiChoiceDialog(getContactNames(), "onAdditionalContactSelected", "دعوت از دوستان");
			}
		});

		mLayoutBarExtension.setVisibility(View.GONE);
	}

	private int addLocalMessage(String text, String time){
		ChatMessage message = new ChatMessage();
		message.IsSenderSelf = true;
		message.MessageText = text;
		message.ReceptionTime = time;
		message.Type = ChatMessageType.Text;
		mDataSet.add(message);
		mAdapter.notifyDataSetChanged();
		return mDataSet.size() -1;
	}

	private void loadSampleData() {
		mDataSet.clear();

		ChatMessage message = new ChatMessage();
		message.IsSenderSelf = true;
		message.MessageText = "سلام آقا وحید!";
		message.ReceptionTime = "10:19";
		message.Type = ChatMessageType.Text;
		mDataSet.add(message);

		message = new ChatMessage();
		message.IsSenderSelf = true;
		message.MessageText = "حالتون خوبه؟ من دیروز سعی کردم تماس بگیرم گوشیتون خاموش بود";
		message.ReceptionTime = "10:19";
		message.Type = ChatMessageType.Text;
		mDataSet.add(message);

		message = new ChatMessage();
		message.IsSenderSelf = false;
		message.MessageText = "سلام! خیلی ممنون، شما خوبید انشالله؟ بله گوشیم مونده بود منزل یکی از دوستان، می بخشید";
		message.ReceptionTime = "10:20";
		message.Type = ChatMessageType.Text;
		mDataSet.add(message);

		mAdapter.notifyDataSetChanged();
	}

	protected void loadHistory() {
		try {
			RoomSchema room = SessionStore.mSessionStore.getRoomById(mRoomId);
			if (room != null) {
				Vector<MessageSchema> messages = room.getAllMessages();
				for (int i = 0; i < messages.size(); i++) {
					onNewMessage(messages.get(i));
				}
			}
		} catch (Exception ex) {
			logCatDebug((ex.getMessage()));
		}
	}

	protected void checkMessages() {
		loadHistory();
	}

	private void createPictureMessage(boolean self, Bitmap picture) {
		ChatMessage message = new ChatMessage();
		message.IsSenderSelf = true;
		message.MessagePicture = picture;
		Calendar calendar = GregorianCalendar.getInstance();
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		message.ReceptionTime = hour + ":" + minute;
		message.Type = ChatMessageType.Picture;
		addMessage(message);
		mEditMessageContent.setText("");
	}

	private void addMessage(ChatMessage message) {
		mDataSet.add(message);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		SessionStore.mSessionStore.mCurrentRoomId = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		int width;
		switch (requestCode) {
		case DIALOG_PICTURE_GALLERY:
			final Uri selectedImage = data.getData();
			width = (int) getResources().getDimension(R.dimen.chat_message_item_picture_width);
			uploadImageToTheServer(selectedImage, mRoomId);
			break;
		case DIALOG_TAKE_PICTURE:
			Uri takenImage = data.getData();
			width = (int) getResources().getDimension(R.dimen.chat_message_item_picture_width);
			uploadImageToTheServer(takenImage, mRoomId);
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private boolean findMessageInRoom(MessageSchema ms) {
		for (ChatMessage cm : mDataSet) {
			if (cm.MessageId.equals(ms.mId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onNewMessage(final MessageSchema ms) {
		synchronized (mAddMessageLock) {
			if (ms.mTo.equals(mRoomId) && !findMessageInRoom(ms)) {
				final ChatMessage message = new ChatMessage();
				if (ms.mType.equals("Text")) {
					message.Type = ChatMessageType.Text;
				} else if (ms.mType.equals("Picture")) {
					message.Type = ChatMessageType.Picture;
				}
				message.IsSenderSelf = false;
				message.MessageId = ms.mId;
				message.SenderID = ms.mFromId;
				message.SenderUserName = ms.mFromUserName;
				if (ms.mFromUserName.equals(mCurrentUserProfile.m_userName)) {
					message.IsSenderSelf = true;
				}

				message.MessageText = ms.mBody;
				message.ReceptionTime = ms.mDate;
				mDataSet.add(message);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		}
	}

	@Override
	public void onFriendTyping(String friendId, String roomId) {
		//showToast("FriendId : " + friendId + " : " + roomId);
		if(roomId.equals(mRoomId) && !mCurrentUserProfile.m_uuid.equals(friendId)){
			onContactTypingChanged(true);
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					onContactTypingChanged(false);
				}
			}, 5000);
		}
	}
}

package org.ertebat.ui;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import org.ertebat.R;
import org.ertebat.schema.SettingSchema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private final String TAG = "ChatMessageAdapter";

    private Context mContext;
    private Bitmap mContactPicture;
    private List<ChatMessage> mDataSet;

    private Target mPictureTarget;

    public ChatMessageAdapter(Context context, Bitmap contactPic, List<ChatMessage> objects) {
        super(context, R.id.txtChatMessageListItemContent, objects);

        mContext = context;
        mContactPicture = contactPic;
        mDataSet = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatMessage message = mDataSet.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = null;
        TextView text;
        ImageView imageDelivery;

        if (message.Type == ChatMessageType.Text) {
            itemView = inflater.inflate(message.IsSenderSelf ? R.layout.data_list_item_chat_message_right
                    : R.layout.data_list_item_chat_message_left, null, true);

            text = (TextView) (itemView.findViewById(R.id.txtChatMessageListItemContent));
            text.setTypeface(BaseActivity.FontRoya);
            text.setText(message.MessageText);

        } else if (message.Type == ChatMessageType.Picture) {
            GifAnimationDrawable dr = null;
            itemView = inflater.inflate(message.IsSenderSelf ? R.layout.data_list_item_chat_message_picture_right
                    : R.layout.data_list_item_chat_message_picture_left, null, true);
            final ImageView image = (ImageView) itemView.findViewById(R.id.imgChatMessageListItemContent);

            mPictureTarget = new Target() {

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    image.setImageDrawable(placeHolderDrawable);
                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                    try {
                        File pictureFileDirectory = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getPath()
                                + "/" + BaseActivity.PICTURES_DIRECTORY_NAME);
                        pictureFileDirectory.mkdirs();
                        File pictureFile = new File(pictureFileDirectory, message.MessageId);
                        FileOutputStream output = new FileOutputStream(pictureFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

                        image.setImageBitmap(bitmap);
                    } catch (FileNotFoundException ex) {
                        if (ex.getMessage() != null) {
                            Log.d(TAG, ex.getMessage());
                        }
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    image.setImageDrawable(errorDrawable);
                }
            };

            try {
                dr = (new GifAnimationDrawable(getContext().getResources().openRawResource(R.drawable.ajax_loader)));
                dr.setVisible(true, true);
                int width = (int) getContext().getResources().getDimension(R.dimen.chat_message_item_picture_width);
                Picasso.with(getContext())
                        .load(SettingSchema.mBaseRestUrl + "uploaded/entities/" + message.MessageText)
                        .resize(width, width).centerInside().placeholder(dr).into(mPictureTarget);
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        text = (TextView) (itemView.findViewById(R.id.txtChatMessageListItemDateTime));
        text.setText(message.ReceptionTime);

        if (!message.IsSenderSelf) {
            ImageView image = (ImageView) (itemView.findViewById(R.id.imgChatMessageListItemPicture));
            image.setImageBitmap(mContactPicture);
            try {
                ImageView senderImage = (ImageView) itemView.findViewById(R.id.imgChatMessageListItemPicture);
                int width = (int) getContext().getResources().getDimension(
                        org.ertebat.R.dimen.chat_message_item_thumb_size);
                Picasso.with(getContext())
                        .load(SettingSchema.mBaseRestUrl + "uploaded/profiles/" + message.SenderID + ".png")
                        .resize(width, width).centerInside()
                        .placeholder(org.ertebat.R.drawable.ic_default_user_picture)
                        .error(org.ertebat.R.drawable.ic_default_user_picture).into(image);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else { // set the delivery icon
            imageDelivery = (ImageView) itemView.findViewById(R.id.imageChatMessageListItemDeliveryStatus);
            imageDelivery.setBackgroundResource(message.IsDelivered ? R.drawable.ic_message_delivered
                    : R.drawable.ic_message_pending);
        }

        return itemView;
    }
}

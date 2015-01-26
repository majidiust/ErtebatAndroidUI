package org.ertebat.ui;

import android.graphics.Bitmap;

public class ChatMessage {
    public boolean IsSenderSelf = false;
    public ChatMessageType Type;
    public String SenderID;
    public String ReceptionDate;
    public String ReceptionTime;
    public String MessageText;
    public Bitmap MessagePicture;
    public String MessageId;
    public String SenderUserName;
    public boolean IsDelivered = false;
}

package org.ertebat.schema;

public class FriendSchema {
	public enum FriendState{
		FS_Online,
		FS_Offline,
		FS_Idle
	}
	    public String m_friendId;
	    public String m_friendUserName;
	    public String m_state;
	    public FriendSchema(String friendId, String friendUsername, String state){
	    	m_friendId = friendId;
	    	m_friendUserName = friendUsername;
	    	m_state = state;
	    }
	    public void setFriensConnectivity(FriendState connectivityStatus){
	    	m_connectivityState = connectivityStatus;
	    }
	    public FriendState m_connectivityState;
}

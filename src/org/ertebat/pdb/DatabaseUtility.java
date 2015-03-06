package org.ertebat.pdb;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ertebat.pdb.DaoMaster.DevOpenHelper;
import org.ertebat.schema.FriendSchema;
import org.ertebat.schema.MessageSchema;
import org.ertebat.schema.ProfileSchema;
import org.ertebat.schema.RoomSchema;

import de.greenrobot.dao.query.QueryBuilder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUtility {
	private String TAG = "DatabaseUtility";
	private SQLiteDatabase db;
	private Context context;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private Cursor cursor;
	public DatabaseUtility(Context context){
		this.context = context;
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "ertebat-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		Log.d(TAG, "Created databse");
	}
	
	public ProfileEntity getProfileSchema(){
		Log.d(TAG,"Get user profile locally");
		ProfileEntityDao profileDao = daoSession.getProfileEntityDao();
		QueryBuilder<ProfileEntity> qb = profileDao.queryBuilder();
		List<ProfileEntity> profiles = qb.list();
		Log.d(TAG,"profile size is : " + profiles.size());
		if(profiles.size() > 0)
			return profiles.get(0);
		else return null;
	}
	
	public ProfileEntity addProfileSchema(ProfileSchema ps){
		try{
			ProfileEntity profile = new ProfileEntity();
			profile.setEmail(ps.m_email);
			profile.setFirstName(ps.m_firstName);
			profile.setLastName(ps.m_lastName);
			profile.setPictureURL(ps.m_pictureUrl);
			profile.setToken(ps.m_token);
			profile.setUserName(ps.m_userName);
			profile.setUuid(ps.m_uuid);
			ProfileEntityDao profileDao = daoSession.getProfileEntityDao();
			profileDao.insert(profile);
			Log.d(TAG, "saved profile:" + profile.toString());
			return profile;
		}
		catch(Exception ex){
			Log.d(TAG, ex.getMessage());
			return null;
		}
	}
	
	public boolean isExistRoom(RoomSchema rs){
		try{
			Log.d(TAG, "search for room id : " + rs.mId);
			RoomEntityDao roomDao = daoSession.getRoomEntityDao();
			QueryBuilder<RoomEntity> qb = roomDao.queryBuilder();
			List<RoomEntity> result = qb.where(RoomEntityDao.Properties.RoomId.eq(rs.mId)).list();
			if(result.size() > 0 )
				return true;
			return false;
		}
		catch(Exception ex){
			Log.d(TAG, ex.getMessage());
			return false;
		}
	}
	
	public boolean isExistFriend(FriendSchema fs){
		try{
			Log.d(TAG, "search for friend id : " + fs.m_friendId + " : " + fs.m_friendUserName);
			FriendEntityDao friendDao = daoSession.getFriendEntityDao();
			QueryBuilder<FriendEntity> qb = friendDao.queryBuilder();
			List<FriendEntity> result = qb.where(FriendEntityDao.Properties.FriendId.eq(fs.m_friendId)).list();
			if(result.size() > 0)
				return false;
			return false;
		}
		catch(Exception ex){
			Log.d(TAG, ex.getMessage());
			return false;
		}
	}
	
	public boolean isExistMessage(MessageSchema ms){
		try{
			Log.d(TAG, "search for message id : " + ms.mId);
			MessageEntityDao messageDao = daoSession.getMessageEntityDao();
			QueryBuilder<MessageEntity> qb = messageDao.queryBuilder();
			List<MessageEntity> result = qb.where(MessageEntityDao.Properties.MessageId.eq(ms.mId)).list();
			if(result.size() > 0)
				return false;
			return false;
		}
		catch(Exception ex){
			Log.d(TAG, ex.getMessage());
			return false;
		}
	}
	
	public List<MessageEntity> getRoomMessages(RoomSchema rs){
		try{
			Log.d(TAG, "get messages for the room : " + rs.mId);
			MessageEntityDao messageDao = daoSession.getMessageEntityDao();
			QueryBuilder<MessageEntity> qb = messageDao.queryBuilder();
			List<MessageEntity> result = qb.where(MessageEntityDao.Properties.To.eq(rs.mId)).list();
			return result;
		}
		catch(Exception ex){
			Log.d(TAG, ex.getMessage());
			return null;
		}
	}
	
	public List<String> getRoomMembers(RoomSchema rs){
		try{
			Log.d(TAG, "get members of the room");
			RoomsMemberEntityDao roomMembersDao = daoSession.getRoomsMemberEntityDao();
			QueryBuilder<RoomsMemberEntity> qb = roomMembersDao.queryBuilder();
			List<RoomsMemberEntity> result = qb.where(RoomsMemberEntityDao.Properties.RoomId.eq(rs.mId)).list();
			List<String> listOfMembersId = new ArrayList<String>();
			for(RoomsMemberEntity rme: result){
				listOfMembersId.add(rme.getMemberId());
			}
			return listOfMembersId;
		}
		catch(Exception ex){
			Log.d(TAG, ex.getMessage());
			return null;
		}
	}
}

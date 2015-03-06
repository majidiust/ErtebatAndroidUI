package org.ertebat.pdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.ertebat.pdb.RoomsMemberEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ROOMS_MEMBER_ENTITY.
*/
public class RoomsMemberEntityDao extends AbstractDao<RoomsMemberEntity, Long> {

    public static final String TABLENAME = "ROOMS_MEMBER_ENTITY";

    /**
     * Properties of entity RoomsMemberEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MemberEntityId = new Property(1, Long.class, "memberEntityId", false, "MEMBER_ENTITY_ID");
        public final static Property RoomEntityId = new Property(2, Long.class, "roomEntityId", false, "ROOM_ENTITY_ID");
        public final static Property MemberId = new Property(3, String.class, "memberId", false, "MEMBER_ID");
        public final static Property RoomId = new Property(4, String.class, "roomId", false, "ROOM_ID");
        public final static Property IsActive = new Property(5, Boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public RoomsMemberEntityDao(DaoConfig config) {
        super(config);
    }
    
    public RoomsMemberEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ROOMS_MEMBER_ENTITY' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'MEMBER_ENTITY_ID' INTEGER," + // 1: memberEntityId
                "'ROOM_ENTITY_ID' INTEGER," + // 2: roomEntityId
                "'MEMBER_ID' TEXT," + // 3: memberId
                "'ROOM_ID' TEXT," + // 4: roomId
                "'IS_ACTIVE' INTEGER);"); // 5: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ROOMS_MEMBER_ENTITY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RoomsMemberEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long memberEntityId = entity.getMemberEntityId();
        if (memberEntityId != null) {
            stmt.bindLong(2, memberEntityId);
        }
 
        Long roomEntityId = entity.getRoomEntityId();
        if (roomEntityId != null) {
            stmt.bindLong(3, roomEntityId);
        }
 
        String memberId = entity.getMemberId();
        if (memberId != null) {
            stmt.bindString(4, memberId);
        }
 
        String roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindString(5, roomId);
        }
 
        Boolean isActive = entity.getIsActive();
        if (isActive != null) {
            stmt.bindLong(6, isActive ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RoomsMemberEntity readEntity(Cursor cursor, int offset) {
        RoomsMemberEntity entity = new RoomsMemberEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // memberEntityId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // roomEntityId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // memberId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // roomId
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RoomsMemberEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMemberEntityId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRoomEntityId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setMemberId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRoomId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsActive(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RoomsMemberEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RoomsMemberEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
package org.ertebat.pdb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ROOMS_MEMBER_ENTITY.
 */
public class RoomsMemberEntity {

    private Long id;
    private Long memberEntityId;
    private Long roomEntityId;
    private String memberId;
    private String roomId;
    private Boolean isActive;

    public RoomsMemberEntity() {
    }

    public RoomsMemberEntity(Long id) {
        this.id = id;
    }

    public RoomsMemberEntity(Long id, Long memberEntityId, Long roomEntityId, String memberId, String roomId, Boolean isActive) {
        this.id = id;
        this.memberEntityId = memberEntityId;
        this.roomEntityId = roomEntityId;
        this.memberId = memberId;
        this.roomId = roomId;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberEntityId() {
        return memberEntityId;
    }

    public void setMemberEntityId(Long memberEntityId) {
        this.memberEntityId = memberEntityId;
    }

    public Long getRoomEntityId() {
        return roomEntityId;
    }

    public void setRoomEntityId(Long roomEntityId) {
        this.roomEntityId = roomEntityId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
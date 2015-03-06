package org.ertebat.pdb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MEMBER_ENTITY.
 */
public class MemberEntity {

    private Long id;
    private String memberId;
    private String userName;
    private String state;

    public MemberEntity() {
    }

    public MemberEntity(Long id) {
        this.id = id;
    }

    public MemberEntity(Long id, String memberId, String userName, String state) {
        this.id = id;
        this.memberId = memberId;
        this.userName = userName;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
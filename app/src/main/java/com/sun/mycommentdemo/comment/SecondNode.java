package com.sun.mycommentdemo.comment;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;

/**
 * 第二个节点SecondNode，里面没有子节点了
 */
public class SecondNode extends BaseNode {

    private String beReplyAvatar;
    private String beReplyId;
    private String beReplyNickName;
    private String content;
    private Integer id;
    private Boolean isLike =false;
    private Integer likeCount;
    private String ownerId;
    private Integer parentId;
    private Integer replyCount;
    private String time;
    private String userAvatar;
    private String userId;
    private String userNickName;

    public SecondNode() {
    }

    /**
     * 重写此方法，返回子节点
     */
    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }


    public String getBeReplyAvatar() {
        return beReplyAvatar;
    }

    public void setBeReplyAvatar(String beReplyAvatar) {
        this.beReplyAvatar = beReplyAvatar;
    }

    public String getBeReplyId() {
        return beReplyId;
    }

    public void setBeReplyId(String beReplyId) {
        this.beReplyId = beReplyId;
    }

    public String getBeReplyNickName() {
        return beReplyNickName;
    }

    public void setBeReplyNickName(String beReplyNickName) {
        this.beReplyNickName = beReplyNickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}
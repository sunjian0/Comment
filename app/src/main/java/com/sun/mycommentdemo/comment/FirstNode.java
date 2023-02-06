package com.sun.mycommentdemo.comment;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一个节点FirstNode，里面放子节点SecondNode
 */
public class FirstNode extends BaseNode {

    private List<BaseNode> childNode;
    private int childNextPage = 1;

    private String content;
    private Integer id;
    private Boolean isLike = false;
    private Integer likeCount;
    private String ownerId;
    private Integer parentId;
    private Integer replyCount;
    private String time;
    private String userAvatar;
    private String userId;
    private String userNickName;
    private Integer commentCount;
    private Integer allowComments;
    private Integer totalPage;

    private Boolean isExpand = false;

    private Integer addCount = 0;
    private Integer reduce =0;

    public Boolean getExpand() {
        return isExpand;
    }

    public void setExpand(Boolean expand) {
        isExpand = expand;
    }


    public FirstNode() {
        // 默认不展开
//        setExpanded(false);
        this.childNode = new ArrayList<>();
//        if (this.replyCount != null && this.replyCount > 0) {
//            childNode.add(new SecondNode(1));
//        }
    }

//    public FirstNode(List<BaseNode> childNode) {
//        this.childNode = childNode;
//        // 默认不展开
////        setExpanded(false);
//    }


    public int getChildNextPage() {
        return childNextPage;
    }

    public void setChildNextPage(int childNextPage) {
        this.childNextPage = childNextPage;
    }

    public Integer getReduce() {
        return reduce;
    }

    public void setReduce(Integer reduce) {
        this.reduce = reduce;
    }

    public void setReduceOne() {
        this.reduce = this.reduce + 1;
    }

    public Integer getAddCount() {
        return addCount;
    }

    public void setAddCount(Integer addCount) {
        this.addCount = addCount;
    }

    public void setAddCountOne() {
        this.addCount = this.addCount + 1;
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

//    public void addReplyCount() {
//        this.replyCount = this.replyCount+1;
//    }

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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(Integer allowComments) {
        this.allowComments = allowComments;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void addChild(List<BaseNode> childNode) {
        this.childNode.addAll(childNode);
    }

    /**
     * 重写此方法，返回子节点
     */
    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        //有二级列表时 自动添加一条 Foot
        if (this.replyCount != null && this.replyCount > 0 && childNode.size() == 0) {
            childNode.add(new FooterNode(this.replyCount));
            return childNode;
        }
        return childNode;
    }

}

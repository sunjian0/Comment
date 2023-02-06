package com.sun.mycommentdemo.comment;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.entity.node.NodeFooterImp;

import java.util.List;

public class FooterNode extends BaseExpandNode implements NodeFooterImp {

    private int replyCount;

    private int isShow = 0;   //  0全部未展开 1展开了一部分 2全部展开

    public FooterNode(int replyCount) {
        this.replyCount = replyCount;
//        this.setExpanded(isShow);
//        this.isShow = isShow;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int isShow() {
        return isShow;
    }

    public void setShow(int show) {
        isShow = show;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }

    @Nullable
    @Override
    public BaseNode getFooterNode() {
        return null;
    }
}

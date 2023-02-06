package com.sun.mycommentdemo.comment;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sun.mycommentdemo.R;

public class FooterNodeProvider extends BaseNodeProvider  {

//    private OnClickLoadMoreChildCallBack onClickLoadMoreChildCallBack;
//
//    public FooterNodeProvider(OnClickLoadMoreChildCallBack onClickLoadMoreChildCallBack) {
//        this.onClickLoadMoreChildCallBack = onClickLoadMoreChildCallBack;
//    }

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.more_commend_layout;
    }

    @Override
    public void convert( BaseViewHolder helper, BaseNode data) {
        FooterNode entity = (FooterNode) data;

        if (entity.isShow() == 0) {
            helper.setVisible(R.id.more_layoutOpen, true);
            helper.setVisible(R.id.more_layoutClose, false);

            int parentPosition = getAdapter().findParentNode(data);
            FirstNode firstNode = (FirstNode) getAdapter().getData().get(parentPosition);
            //此处 getChildNode().size()-1 为减去 Footer布局
            int replyNumber = (firstNode.getReplyCount()+firstNode.getAddCount()-firstNode.getReduce()) - (firstNode.getChildNode().size()-1);
            helper.setText(R.id.more_tvMore, "展开" + replyNumber + "条回复");
            helper.setText(R.id.more_tvClose, "收起");
        } else if (entity.isShow() == 1) {
            helper.setVisible(R.id.more_layoutOpen, true);
            helper.setVisible(R.id.more_layoutClose, true);
            helper.setText(R.id.more_tvMore, "展开更多回复");
            helper.setText(R.id.more_tvClose, "收起");
        } else {
            helper.setVisible(R.id.more_layoutOpen, false);
            helper.setVisible(R.id.more_layoutClose, true);
            helper.setText(R.id.more_tvMore, "展开更多回复");
            helper.setText(R.id.more_tvClose, "收起");
        }

    }

}

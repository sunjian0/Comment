package com.sun.mycommentdemo.comment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sun.mycommentdemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Sun
 * @Date 2022/12/15
 * @Description 评论列表弹窗
 *
 * 此处可以使用 BottomSheetDialog  也可以用 BottomSheetDialogFragment 根据需求 怎么方便怎么来 就行 
 */
public class CommendMsgDialog extends BottomSheetDialog {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private CommendAdapter mAdapter;

    private List<BaseNode> data = new ArrayList<>();
    private int commentCount;

    private TextView tvCommentCount;
    private FrameLayout layoutList;
    private RelativeLayout layoutNetError, layoutClose;
    private ImageView ivRetry;

    //输入弹窗
    private CommendInputDialog inputDialog;
    //删除弹窗
    private CommendDeleteDialog deleteDialog;

    private OnListListener onListListener;

    public CommendMsgDialog(@NonNull Context context, OnListListener onListListener) {
        super(context, R.style.CustomBottomSheetDialogTheme);
        setContentView(R.layout.my_commend_msg_dialog);
        this.mContext = context;
        this.onListListener = onListListener;
        initView();
    }

    private void initView() {
        tvCommentCount = findViewById(R.id.tvCommentNumber);
        layoutList = findViewById(R.id.layout_list);
        layoutNetError = findViewById(R.id.layout_netError);
        layoutClose = findViewById(R.id.layout_close);
        //下方输入框 布局
        findViewById(R.id.mTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showSoft(1, -1, null, null, null, -1);
            }
        });
        //关闭评论弹窗
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //网络失败时 重试按钮
        findViewById(R.id.ivRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onListListener != null) {
                    onListListener.retryFirstPage();
                }
            }
        });

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//加载更多

        mAdapter = new CommendAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // 打开或关闭加载更多功能（默认为true）
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
//        mAdapter.getLoadMoreModule().setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (onListListener != null) {
                    onListListener.getNextPage();
                }
            }
        });

        //设置空布局
        mAdapter.setEmptyView(R.layout.view_comment_empty);
        // 设置新的数据方法

        //子布局监听
        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        mAdapter.addChildClickViewIds(R.id.first_tvCommentContent, R.id.first_imgAvatar,
                R.id.first_tvCommentNickname, R.id.first_tvReply, R.id.first_rlLike,
                R.id.second_imgAvatar, R.id.second_tvCommentNickname, R.id.second_tvCommentNickname2, R.id.second_tvCommentContent,
                R.id.second_tvReply, R.id.second_rlLike
                , R.id.more_layoutOpen, R.id.more_layoutClose);
        // 设置子控件点击监听
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

                BaseNodeAdapter adapter1 = (BaseNodeAdapter) adapter;
                int parentPosition = ((BaseNodeAdapter) adapter).findParentNode(position);
                // 通过 position 和 adapter.getData();  去获取数据

                if (view.getId() == R.id.first_tvCommentContent) {

//                    ExpandTextView tv=view.findViewById(R.id.first_tvCommentContent);
//                    if (tv.getSelectionStart() == -1 && tv.getSelectionEnd() == -1) {
//
//                    }

                    //一级列表 点击评论内容快捷回复
                        FirstNode parentNode = (FirstNode) mAdapter.getData().get(position);
                        showSoft(2, parentNode.getId(), parentNode.getUserId(), parentNode.getUserNickName(), parentNode.getUserAvatar(), position);
                } else if (view.getId() == R.id.first_imgAvatar) {
                    //点击 一级列表 评论用户头像
                    FirstNode firstNode = (FirstNode) mAdapter.getData().get(position);
                    userClick(firstNode.getUserId());
                } else if (view.getId() == R.id.first_tvCommentNickname) {
                    //点击 一级列表 评论用户昵称
                    FirstNode firstNode = (FirstNode) mAdapter.getData().get(position);
                    userClick(firstNode.getUserId());
                } else if (view.getId() == R.id.first_tvReply) {
                    //一级列表 回复按钮
                        FirstNode parentNode = (FirstNode) mAdapter.getData().get(position);
                        showSoft(2, parentNode.getId(), parentNode.getUserId(), parentNode.getUserNickName(), parentNode.getUserAvatar(), position);
                } else if (view.getId() == R.id.first_rlLike) {
                    //一级列表 点赞
                    FirstNode parentNode = (FirstNode) mAdapter.getData().get(position);
                    if (onListListener != null) {
                        onListListener.giveLikes(1, parentPosition, position, parentNode.getId(), parentNode.getIsLike() ? "down" : "up");
                    }
                } else if (view.getId() == R.id.second_imgAvatar) {
                    //点击 二级列表 评论用户头像
                    SecondNode secondNode = (SecondNode) adapter1.getData().get(position);
                    userClick(secondNode.getUserId());
                } else if (view.getId() == R.id.second_tvCommentNickname) {
                    //点击 二级列表 评论用户昵称
                    SecondNode secondNode = (SecondNode) adapter1.getData().get(position);
                    userClick(secondNode.getUserId());
                } else if (view.getId() == R.id.second_tvCommentNickname2) {
                    //点击 二级列表 被评论用户昵称
                    SecondNode secondNode = (SecondNode) adapter1.getData().get(position);
                    userClick(secondNode.getBeReplyId());
                } else if (view.getId() == R.id.second_tvCommentContent) {
                    //二级列表 点击评论内容快捷回复
                        FirstNode parentNode = (FirstNode) mAdapter.getData().get(parentPosition);
                        SecondNode secondNode = (SecondNode) adapter1.getData().get(position);
                        showSoft(2, parentNode.getId(), secondNode.getUserId(), secondNode.getUserNickName(), secondNode.getUserAvatar(), parentPosition);
                } else if (view.getId() == R.id.second_tvReply) {
                    //二级列表 回复按钮
                        FirstNode parentNode = (FirstNode) mAdapter.getData().get(parentPosition);
                        SecondNode secondNode = (SecondNode) adapter1.getData().get(position);
                        showSoft(2, parentNode.getId(), secondNode.getUserId(), secondNode.getUserNickName(), secondNode.getUserAvatar(), parentPosition);
                } else if (view.getId() == R.id.second_rlLike) {
                    //二级列表 点赞
                    SecondNode secondNode = (SecondNode) adapter1.getData().get(position);
                    if (onListListener != null) {
                        onListListener.giveLikes(2, parentPosition, position, secondNode.getId(), secondNode.getIsLike() ? "down" : "up");
                    }
                } else if (view.getId() == R.id.more_layoutOpen) {
                    //展开更多
                    FirstNode firstNode = (FirstNode) mAdapter.getData().get(parentPosition);
                    int page = firstNode.getChildNextPage();
                    if (onListListener != null) {
                        onListListener.getSecondList(firstNode.getId(), page, parentPosition);
                    }
                } else if (view.getId() == R.id.more_layoutClose) {
                    //收起
                    FirstNode firstNode = (FirstNode) mAdapter.getData().get(parentPosition);
                    if (firstNode.getAddCount() > 0) {
                        firstNode.setReplyCount(firstNode.getReplyCount() + firstNode.getAddCount() - firstNode.getReduce());
                        firstNode.setAddCount(0);
                        firstNode.setReduce(0);
                    }
                    firstNode.setChildNextPage(1);
                    List<BaseNode> childNode = new ArrayList<>();
                    childNode.add(new FooterNode(firstNode.getReplyCount()));
                    mAdapter.nodeReplaceChildData(firstNode, childNode);
                }
            }
        });

        //长按监听
        mAdapter.addChildLongClickViewIds(R.id.first_root,
                R.id.first_tvCommentContent, R.id.first_imgAvatar, R.id.first_tvCommentNickname, R.id.first_tvReply, R.id.first_rlLike,
                R.id.second_root,
                R.id.second_imgAvatar, R.id.second_tvCommentNickname, R.id.second_tvCommentNickname2, R.id.second_tvCommentContent, R.id.second_tvReply, R.id.second_rlLike);
        mAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if ((view.getId() == R.id.first_root) || (view.getId() == R.id.first_tvCommentContent) || (view.getId() == R.id.first_imgAvatar) ||
                        (view.getId() == R.id.first_tvCommentNickname) || (view.getId() == R.id.first_tvReply) || (view.getId() == R.id.first_rlLike)) {
//                    Toast.makeText(getContext(), "长按了 一级列表 ", Toast.LENGTH_SHORT).show();
                    // TODO 删除评论弹窗判断
                    FirstNode firstNode = (FirstNode) mAdapter.getData().get(position);
                    showDelete(1, position, -1, firstNode.getId());
                } else if ((view.getId() == R.id.second_root) || (view.getId() == R.id.second_imgAvatar) || (view.getId() == R.id.second_tvCommentNickname) || (view.getId() == R.id.second_tvCommentNickname2)
                        || (view.getId() == R.id.second_tvCommentContent) || (view.getId() == R.id.second_tvReply) || (view.getId() == R.id.second_rlLike)) {
//                    Toast.makeText(getContext(), "长按了 二级列表 ", Toast.LENGTH_SHORT).show();
                    // TODO 删除评论弹窗判断
                    int parentPosition = ((BaseNodeAdapter) adapter).findParentNode(position);
                    SecondNode secondNode = (SecondNode) mAdapter.getData().get(position);
                    showDelete(2, position, parentPosition, secondNode.getId());
                }
                return true;
            }
        });
    }

    /**
     * 设置列表 第一页
     */
    public void setList(List<FirstNode> list) {
        try {
            if (data != null && list != null) {
                data.addAll(list);
            }

            if (mAdapter!= null && data!= null){

                mAdapter.setList(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置列表 第一页以后
     */
    public void setNextList(List<FirstNode> list) {
        mAdapter.addData(list);
    }

    //设置弹窗标题:  xx条评论
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        tvCommentCount.setText(this.commentCount + "条评论");
    }

    /**
     * 设置 弹窗样式  列表、评论区关闭、网络获取失败
     */
    public void setCommentType(int commentType) {
        // 0正常  1关闭 2网络失败
        if (commentType == 1) {
            layoutList.setVisibility(View.GONE);
            layoutNetError.setVisibility(View.GONE);
            layoutClose.setVisibility(View.VISIBLE);
        } else if (commentType == 2) {
            layoutList.setVisibility(View.GONE);
            layoutNetError.setVisibility(View.VISIBLE);
            layoutClose.setVisibility(View.GONE);
        } else {
            layoutList.setVisibility(View.VISIBLE);
            layoutNetError.setVisibility(View.GONE);
            layoutClose.setVisibility(View.GONE);
        }
    }

    public void setLoadMoreComplete() {
        mAdapter.getLoadMoreModule().loadMoreComplete();
    }

    public void setLoadMoreEnd() {
        mAdapter.getLoadMoreModule().loadMoreEnd();
    }

    public void setLoadMoreFail() {
        mAdapter.getLoadMoreModule().loadMoreFail();
    }

    /**
     * 设置 二级列表数据
     */
    public void setSecondList(int parentPosition, List<SecondNode> list) {
        //去重   getChildNode().size()-1最后一位是foot
        for (int i = 0; i < mAdapter.getData().get(parentPosition).getChildNode().size() - 1; i++) {
            SecondNode secondNode = (SecondNode) mAdapter.getData().get(parentPosition).getChildNode().get(i);
            for (int j = 0; j < list.size(); j++) {
                if (secondNode.getId().equals(list.get(j).getId())) {
                    list.remove(j);
                }
            }
        }
        //添加数据
        mAdapter.nodeAddData(mAdapter.getData().get(parentPosition), mAdapter.getData().get(parentPosition).getChildNode().size() - 1, list);
        //更新父节点保存的 子页数
        FirstNode firstNode = (FirstNode) mAdapter.getData().get(parentPosition);
        firstNode.setChildNextPage(firstNode.getChildNextPage() + 1);
        mAdapter.setData(parentPosition, firstNode);
        //判断 foot状态 更新
        FooterNode dataNew = (FooterNode) mAdapter.getData().get(parentPosition).getChildNode().get(mAdapter.getData().get(parentPosition).getChildNode().size() - 1);
        if (((firstNode.getChildNode().size() - 1) > 0) && ((firstNode.getChildNode().size() - 1) < (firstNode.getReplyCount() + firstNode.getAddCount() - firstNode.getReduce()))) {
            dataNew.setShow(1);
            mAdapter.nodeSetData(mAdapter.getData().get(parentPosition), mAdapter.getData().get(parentPosition).getChildNode().size() - 1, dataNew);
        } else if ((firstNode.getChildNode().size() - 1) >= (firstNode.getReplyCount() + firstNode.getAddCount() - firstNode.getReduce())) {
            dataNew.setShow(2);
            mAdapter.nodeSetData(mAdapter.getData().get(parentPosition), (mAdapter.getData().get(parentPosition).getChildNode().size() - 1), dataNew);
        }
    }

    /**
     * 评论成功 列表增加一条 一级评论
     */
    public void setCommentOne(int id, String userId, String userNickName, String userAvatar, String content, String ownerId) {
        boolean ifLoadMoreEnd = mAdapter.getData().size() == 0;

        FirstNode firstNode = new FirstNode();
        firstNode.setId(id);
        firstNode.setContent(content);
        firstNode.setUserId(userId);
        firstNode.setUserNickName(userNickName);
        firstNode.setUserAvatar(userAvatar);
        firstNode.setIsLike(false);
        firstNode.setOwnerId(ownerId);
        firstNode.setReplyCount(0);
        firstNode.setLikeCount(0);
        firstNode.setTime("刚刚");
        mAdapter.addData(0, firstNode);
        mRecyclerView.scrollToPosition(0);

        this.commentCount = this.commentCount + 1;
        tvCommentCount.setText(this.commentCount + "条评论");

        //如果添加单条一级列表时 还没有数据 列表关闭 loadMore
        if (ifLoadMoreEnd) {
            mAdapter.getLoadMoreModule().loadMoreEnd();
        }
    }

    /**
     * 回复评论成功 列表增加一条 二级评论
     */
    public void setCommentTwo(int id, String userId, String userNickName, String userAvatar, String content, String ownerId, String beReplyId, String beReplyNickName, String beReplyAvatar, int parentPosition) {
        SecondNode secondNode = new SecondNode();
        secondNode.setId(id);
        secondNode.setUserId(userId);
        secondNode.setUserNickName(userNickName);
        secondNode.setUserAvatar(userAvatar);
        secondNode.setContent(content);
        secondNode.setOwnerId(ownerId);
        secondNode.setBeReplyId(beReplyId);
        secondNode.setBeReplyNickName(beReplyNickName);
        secondNode.setBeReplyAvatar(beReplyAvatar);
        secondNode.setIsLike(false);
        secondNode.setLikeCount(0);

        //判断一级列表数据 添加到具体位置
        if (mAdapter.getData().get(parentPosition).getChildNode().size() == 0) {
            mAdapter.nodeAddData(mAdapter.getData().get(parentPosition), secondNode);
//            FooterNode footerNode = new FooterNode(1);
//            footerNode.setShow(2);
//            mAdapter.nodeAddData(mAdapter.getData().get(parentPosition), footerNode);
            //更改一级列表数据
            FirstNode firstNode = (FirstNode) mAdapter.getData().get(parentPosition);
            firstNode.setAddCountOne();
            mAdapter.setData(parentPosition, firstNode);
        } else {
            //有子节点时 判断子节点最后一位type
            BaseNode lastItem = mAdapter.getData().get(parentPosition).getChildNode().get(mAdapter.getData().get(parentPosition).getChildNode().size() - 1);
            if (lastItem instanceof SecondNode) {
                mAdapter.nodeAddData(mAdapter.getData().get(parentPosition), mAdapter.getData().get(parentPosition).getChildNode().size(), secondNode);
            } else if (lastItem instanceof FooterNode) {
                mAdapter.nodeAddData(mAdapter.getData().get(parentPosition), mAdapter.getData().get(parentPosition).getChildNode().size() - 1, secondNode);
            }

            //更改一级列表数据
            FirstNode firstNode = (FirstNode) mAdapter.getData().get(parentPosition);
            firstNode.setAddCountOne();
            mAdapter.setData(parentPosition, firstNode);

//            //改变 foot展开状态
//            FooterNode dataNew = (FooterNode) mAdapter.getData().get(parentPosition).getChildNode().get(mAdapter.getData().get(parentPosition).getChildNode().size() - 1);
//            dataNew.setShow(1);
//            mAdapter.nodeSetData(mAdapter.getData().get(parentPosition), mAdapter.getData().get(parentPosition).getChildNode().size() - 1, dataNew);
        }
        this.commentCount = this.commentCount + 1;
        tvCommentCount.setText(this.commentCount + "条评论");
    }

    /**
     * 修改点赞状态
     */
    public void setLike(int type, int parentPosition, int position, String upOrDown) {
        if (type == 1) {
            //一级列表
            FirstNode firstNode = (FirstNode) mAdapter.getData().get(position);
            if (upOrDown.equals("up")) {
                firstNode.setIsLike(true);
                firstNode.setLikeCount(firstNode.getLikeCount() + 1);
            } else {
                firstNode.setIsLike(false);
                firstNode.setLikeCount(firstNode.getLikeCount() - 1);
            }
            mAdapter.setData(position, firstNode);
        } else if (type == 2) {
            //二级列表
            SecondNode secondNode = (SecondNode) mAdapter.getData().get(position);
            if (upOrDown.equals("up")) {
                secondNode.setIsLike(true);
                secondNode.setLikeCount(secondNode.getLikeCount() + 1);
            } else {
                secondNode.setIsLike(false);
                secondNode.setLikeCount(secondNode.getLikeCount() - 1);
            }
            mAdapter.setData(position, secondNode);
        }
    }

    /**
     * 删除一条评论
     */
    public void setDelete(int type, int position, int parentPosition, int id) {
        //此处目前 不管是一级还是二级 直接删掉/子节点直接这样删会产生问题 修改
//        mAdapter.removeAt(position);

        // type==1删除一级评论  type==2删除二级评论
        if (type == 1) {
            FirstNode firstNode = (FirstNode) mAdapter.getData().get(position);
            int replyCount = firstNode.getReplyCount();
            mAdapter.removeAt(position);

            this.commentCount = this.commentCount - replyCount - 1;
            tvCommentCount.setText(this.commentCount + "条评论");
        } else {
            FirstNode firstNode = (FirstNode) mAdapter.getData().get(parentPosition);
            firstNode.setReduceOne();
            mAdapter.setData(parentPosition, firstNode);

            int pos = -1;
            for (int i = 0; i < mAdapter.getData().get(parentPosition).getChildNode().size() - 1; i++) {
                SecondNode secondNode = (SecondNode) mAdapter.getData().get(parentPosition).getChildNode().get(i);
                if (secondNode.getId().equals(id)) {
                    pos = i;
                }
            }
            if (pos != -1) {
                mAdapter.nodeRemoveData(mAdapter.getData().get(parentPosition), pos);
            } else {
                mAdapter.removeAt(position);
            }

            this.commentCount = this.commentCount - 1;
            tvCommentCount.setText(this.commentCount + "条评论");
        }
    }

    /**
     * 输入弹窗
     */
    private void showSoft(int type, int id, String beReplyId, String beReplyNickName, String beReplyAvatar, int parentPosition) {
        try {
            if (inputDialog == null) {
                inputDialog = new CommendInputDialog(mContext, R.style.dialog_center);
            }
            if (type == 1) {
                inputDialog.setHint(mContext.getResources().getString(R.string.comment_input_hint));
            } else {
                inputDialog.setHint("回复 " + beReplyNickName);
            }
            inputDialog.setMaxNumber(200);
            inputDialog.showKeyboard();

            inputDialog.show();
            inputDialog.setmOnTextSendListener(new CommendInputDialog.OnTextSendListener() {
                @Override
                public void onInputTextString(String msg) {
                    //  LogUtils.d("msg===========" + msg);
                }

                @Override
                public void onClickSend(String message) {
                    String comment = message.replace(" ", "");
                    if (!TextUtils.isEmpty(comment)) {
//                        Toast.makeText(mContext, comment, Toast.LENGTH_SHORT).show();
                        if (onListListener != null) {
                            onListListener.comment(comment, id, beReplyId, beReplyNickName, beReplyAvatar, parentPosition);
                        }
                    } else {
                        Toast.makeText(mContext, "请输入聊天内容", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除提示弹窗
     */
    private void showDelete(int type, int position, int parentPosition, int id) {
        if (deleteDialog == null) {
            deleteDialog = new CommendDeleteDialog(mContext, R.style.dialog_center);
        }
        deleteDialog.show();
        deleteDialog.setOnDeleteListener(new CommendDeleteDialog.OnDeleteListener() {
            @Override
            public void onClickDelete() {
                if (onListListener != null) {
                    onListListener.delComment(type, position, parentPosition, id);
                }
                deleteDialog.dismiss();
            }
        });
    }

    private void userClick(String userId){
        try {
            JSONObject object = new JSONObject();
            object.put("type","comment");
            object.put("userId", userId);
            //调用跳转方法 跳转到用户等页面
            Log.i("VideoPlayModule", "onClick:----------->"+object.toString()+"");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("VideoPlayModule", "捕获异常--onClick:----------->"+e.getMessage().toString()+"");
        }
    }

    public interface OnListListener {
        void getNextPage();

        void retryFirstPage();

        void getSecondList(int id, int page, int parentPosition);

        void comment(String content, int id, String beReplyId, String beReplyNickName, String beReplyAvatar, int parentPosition);

        void giveLikes(int type, int parentPosition, int position, int id, String upOrDown);

        void delComment(int type, int position, int parentPosition, int id);
    }
}

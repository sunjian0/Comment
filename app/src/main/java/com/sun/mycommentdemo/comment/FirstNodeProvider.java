package com.sun.mycommentdemo.comment;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sun.mycommentdemo.R;


public class FirstNodeProvider extends BaseNodeProvider {

//    private OnCommendItemClickCallBack onCommendItemClickCallBack;
//    public FirstNodeProvider(OnCommendItemClickCallBack onCommendItemClickCallBack) {
//        this.onCommendItemClickCallBack = onCommendItemClickCallBack;
//    }

    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_commend_layout;
    }

    @Override
    public void convert( BaseViewHolder helper,  BaseNode data) {
        // 数据类型需要自己强转
        FirstNode entity = (FirstNode) data;

        Glide.with(getContext()).load(((FirstNode) data).getUserAvatar()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into((ImageView) helper.getView(R.id.first_imgAvatar));
        helper.setText(R.id.first_tvCommentNickname, entity.getUserNickName());

        //TODO 获取当前登录用户id 判断显示标签，此处使用假数据
        String userId = "123";
        if (userId.equals(entity.getUserId())){
            //当前用户 发的评论
            helper.setVisible(R.id.first_ivLabelAuthor, false);
            helper.setVisible(R.id.first_ivLabelMy, true);
        }else {
            if (entity.getOwnerId().equals(entity.getUserId())){
                //作者 发的评论
                helper.setVisible(R.id.first_ivLabelAuthor, true);
                helper.setVisible(R.id.first_ivLabelMy, false);
            }else {
                helper.setVisible(R.id.first_ivLabelAuthor, false);
                helper.setVisible(R.id.first_ivLabelMy, false);
            }
        }

        //使用 ExpandTextView
        ExpandTextView textView = helper.getView(R.id.first_tvCommentContent);
        int width = getContext().getResources().getDisplayMetrics().widthPixels - dp2px(getContext(), 68f);
        textView.initWidth(width);
        textView.setMaxLines(3);
        textView.setHasAnimation(true);
//        textView.setCloseInNewLine(true);
        textView.setOpenSuffixColor(getContext().getResources().getColor(R.color.color_999999));
        textView.setCloseSuffixColor(getContext().getResources().getColor(R.color.color_999999));
        if (entity.getContent()!=null){
            textView.setOriginalText(entity.getContent());
        } else {
            textView.setOriginalText("");
        }
        //解决ClickableSpan中点击后List中item的长按冲突的问题
        textView.setMovementMethod(new LinkMovementClickMethod());

        helper.setText(R.id.first_tvTime, entity.getTime());
        helper.setText(R.id.first_tvCommentLike, String.valueOf(entity.getLikeCount()));

        ImageView icLike = helper.getView(R.id.first_imageLike);
        if (entity.getIsLike()==null){
            icLike.setSelected(false);
        }else {
            icLike.setSelected(entity.getIsLike());
        }

    }

    //暂时使用 BaseQuickAdapter的 Item 内子View的点击事件
//    @Override
//    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
//
////        if (view.getId() == R.id.first_tvCommentContent) {
////            Toast.makeText(getContext(), "点击了 评论内容 ", Toast.LENGTH_SHORT).show();
////        } else if (view.getId() == R.id.first_imgAvatar) {
////            Toast.makeText(getContext(), "点击了 评论头像 ", Toast.LENGTH_SHORT).show();
////        } else if (view.getId() == R.id.first_tvCommentNickname) {
////            Toast.makeText(getContext(), "点击了 评论人名 ", Toast.LENGTH_SHORT).show();
////        } else if (view.getId() == R.id.first_tvReply) {
////            Toast.makeText(getContext(), "点击了 回复按钮 ", Toast.LENGTH_SHORT).show();
////        } else if (view.getId() == R.id.first_rlLike) {
////            Toast.makeText(getContext(), "点击了 点赞 ", Toast.LENGTH_SHORT).show();
////        }
////        getAdapter().expandOrCollapse(position);
////        int par = getAdapter().findParentNode(position);
////
////        Toast.makeText(getContext(), "点击了 FirstNodeProvider " + position + "位置 / 父位置"+par, Toast.LENGTH_SHORT).show();
////        this.onCommendItemClickCallBack.onItemClick(data, position);
//    }

    private int dp2px(Context context, float dpValue) {
        int res = 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        if (dpValue < 0)
            res = -(int) (-dpValue * scale + 0.5f);
        else
            res = (int) (dpValue * scale + 0.5f);
        return res;
    }


}

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

public class SecondNodeProvider extends BaseNodeProvider {

//    private OnCommendItemClickCallBack onCommendItemClickCallBack;
//    public SecondNodeProvider(OnCommendItemClickCallBack onCommendItemClickCallBack) {
//        this.onCommendItemClickCallBack = onCommendItemClickCallBack;
//    }

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.child_commend_layout;
    }

    @Override
    public void convert( BaseViewHolder helper, BaseNode data) {
// 数据类型需要自己强转
        SecondNode entity = (SecondNode) data;
//        helper.setText(R.id.tvCommentNickname, entity.getTitle());

        Glide.with(getContext()).load(entity.getUserAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop())).into((ImageView) helper.getView(R.id.second_imgAvatar));
        helper.setText(R.id.second_tvCommentNickname, entity.getUserNickName());

        //TODO 获取当前登录用户id 判断显示标签，此处使用假数据
        String userId = "123";
        if (userId.equals(entity.getUserId())){
            //当前用户 发的评论
            helper.setGone(R.id.second_ivLabelAuthor, true);
            helper.setGone(R.id.second_ivLabelMy, false);
        }else {
            if (entity.getOwnerId().equals(entity.getUserId())){
                //作者 发的评论
                helper.setGone(R.id.second_ivLabelAuthor, false);
                helper.setGone(R.id.second_ivLabelMy, true);
            }else {
                helper.setGone(R.id.second_ivLabelAuthor, true);
                helper.setGone(R.id.second_ivLabelMy, true);
            }
        }

        //使用 ExpandTextView
        ExpandTextView textView = helper.getView(R.id.second_tvCommentContent);
        int width = getContext().getResources().getDisplayMetrics().widthPixels - dp2px(getContext(), 100f);
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

        helper.setText(R.id.second_tvTime, entity.getTime());
        helper.setText(R.id.second_tvCommentLike, entity.getLikeCount()+"");

        ImageView icLike = helper.getView(R.id.second_imageLike);
        icLike.setSelected(false);
        if (entity.getIsLike()==null){
            icLike.setSelected(false);
        }else {
            icLike.setSelected(entity.getIsLike());
        }

        helper.setText(R.id.second_tvCommentNickname2, entity.getBeReplyNickName());
    }

//    @Override
//    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
//        int parentPosition = getAdapter().findParentNode(data);
//        Toast.makeText(getContext(), "点击了 SecondNodeProvider " + position + "位置 / 父位置" + parentPosition, Toast.LENGTH_SHORT).show();
//
//
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

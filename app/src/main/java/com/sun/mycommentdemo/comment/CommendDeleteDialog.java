package com.sun.mycommentdemo.comment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sun.mycommentdemo.R;

/**
 * @author Sun
 * @Date 2022/12/15
 * @Description 评论输入弹窗
 */
public class CommendDeleteDialog extends BottomSheetDialog {
    private Context mContext;

    private OnDeleteListener mOnDeleteListener;

    private TextView tvDelete, tvCancel;

    public CommendDeleteDialog(@NonNull Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.commend_delete_dialog);
        this.mContext = context;
        initView();
    }


    private void initView() {
        tvDelete = findViewById(R.id.tvDelete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDeleteListener!=null){
                    mOnDeleteListener.onClickDelete();
                }
            }
        });
        tvCancel = findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void onClickDelete();
    }
}
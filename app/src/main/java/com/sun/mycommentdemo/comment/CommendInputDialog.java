package com.sun.mycommentdemo.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.sun.mycommentdemo.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author Sun
 * @Date 2022/12/15
 * @Description 评论输入弹窗
 */
public class CommendInputDialog extends AppCompatDialog implements View.OnClickListener {
    private Context mContext;
    private InputMethodManager imm;
    private ScrollViewEditText messageTextView;
    private ImageView confirmBtn;
    private KeyboardLayout rlDlg;
    private int mLastDiff = 0;

    private int maxNumber = 200;

    private TextView tvIcon1, tvIcon2, tvIcon3, tvIcon4, tvIcon5, tvIcon6, tvIcon7, tvIcon8;

    public interface OnTextSendListener {
        void onInputTextString(String msg);

        void onClickSend(String message);
    }

    private OnTextSendListener mOnTextSendListener;

    public CommendInputDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init();
        setLayout();
    }

    /**
     * 最大输入字数  默认200
     */
    @SuppressLint("SetTextI18n")
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        messageTextView.setFilters(new InputFilter[]{new MyLengthFilter(this.maxNumber, mContext)});
    }

    /**
     * 设置输入提示文字
     */
    public void setHint(String text) {
        messageTextView.setHint(text);
    }

    /*设置输入的文字*/
    public void setText(String text) {
        messageTextView.setText(text);
    }


    public void setTextInit() {
        rlDlg.mHasInit = false;
    }

    private void init() {
        setContentView(R.layout.commend_input_dialog);

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);

        messageTextView = findViewById(R.id.et_input_message);
        tvIcon1 = findViewById(R.id.tv_icon1);
        tvIcon2 = findViewById(R.id.tv_icon2);
        tvIcon3 = findViewById(R.id.tv_icon3);
        tvIcon4 = findViewById(R.id.tv_icon4);
        tvIcon5 = findViewById(R.id.tv_icon5);
        tvIcon6 = findViewById(R.id.tv_icon6);
        tvIcon7 = findViewById(R.id.tv_icon7);
        tvIcon8 = findViewById(R.id.tv_icon8);
        tvIcon1.setOnClickListener(this);
        tvIcon2.setOnClickListener(this);
        tvIcon3.setOnClickListener(this);
        tvIcon4.setOnClickListener(this);
        tvIcon5.setOnClickListener(this);
        tvIcon6.setOnClickListener(this);
        tvIcon7.setOnClickListener(this);
        tvIcon8.setOnClickListener(this);

        LinearLayout rldlgview = findViewById(R.id.ll_inputdlg_view);
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Editable editable = messageTextView.getText();
//                int len = editable.length();
//
//                if (len > maxNumber) {
////                    Toast.makeText(mContext, "最大可输入" + maxNumber + "个字符", Toast.LENGTH_SHORT);
//                    Toast.makeText(mContext, "评论的内容太多了，分成多条评论来发吧~", Toast.LENGTH_LONG).show();
//                    int selEndIndex = Selection.getSelectionEnd(editable);
//                    String str = editable.toString();
//                    //截取新字符串
//                    String newStr = str.substring(0, maxNumber);
//                    messageTextView.setText(newStr);
//                    editable = messageTextView.getText();
//
//                    //新字符串的长度
//                    int newLen = editable.length();
//                    //旧光标位置超过字符串长度
//                    if (selEndIndex > newLen) {
//                        selEndIndex = editable.length();
//                    }
//                    //设置新光标所在的位置
//                    Selection.setSelection(editable, selEndIndex);
//
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

//                if (editable.length() == 0) {
//                    confirmBtn.setTextColor(mContext.getResources().getColor(R.color.color_ccccccc));
//                } else {
//                    confirmBtn.setTextColor(mContext.getResources().getColor(R.color.color_ff666666));
//                }
            }
        });


        confirmBtn = (ImageView) findViewById(R.id.confrim_btn);

        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);


        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > maxNumber) {
                            Toast.makeText(mContext, "评论的内容太多了，分成多条评论来发吧~", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        if (messageTextView.getText().length() > 0) {
//                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                            dismiss();
                        } else {
                            Toast.makeText(mContext, "请输入文字", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTextSendListener.onClickSend(messageTextView.getText().toString().trim());
                imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                messageTextView.setText("");
                dismiss();
            }
        });

        rlDlg = findViewById(R.id.rl_outside_view);
        findViewById(R.id.rl_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTextSendListener.onInputTextString(messageTextView.getText().toString().trim());
                imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                messageTextView.setText("");
                dismiss();

            }
        });
        rlDlg.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
                        //    Toast.makeText(getApplicationContext(), "软键盘隐藏", Toast.LENGTH_SHORT).show();
                        //   Log.e("KeyboardLayout","KeyboardLayout----软键盘隐藏");
                        mOnTextSendListener.onInputTextString(messageTextView.getText().toString().trim());
                        messageTextView.setText("");
                        if (isShowing()) {
                            dismiss();
                        }
                        break;
                    case KeyboardLayout.KEYBOARD_STATE_SHOW:
                        //   Toast.makeText(getApplicationContext(), "软键盘弹起", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

//        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//
//                if (heightDifference <= 0 && mLastDiff > 0) {
//                    dismiss();
//                }
//                mLastDiff = heightDifference;
//            }
//        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                dismiss();
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    dismiss();
                }
                return false;
            }
        });

    }

    /**
     * @"😀",@"😬",@"😁",@"😂",@"😃",@"😄",@"😅",@"😆",
     */
    @Override
    public void onClick(View v) {
        if ((messageTextView.getText().length()+2) > maxNumber) {
            Toast.makeText(mContext, "评论的内容太多了，分成多条评论来发吧~", Toast.LENGTH_SHORT).show();
        }else {
            int index = messageTextView.getSelectionStart();
            Editable editable = messageTextView.getText();
            if (v.getId() == R.id.tv_icon1) {
                editable.insert(index, "\uD83D\uDE00");
            } else if (v.getId() == R.id.tv_icon2) {
                editable.insert(index, "\uD83D\uDE2C");
            } else if (v.getId() == R.id.tv_icon3) {
                editable.insert(index, "\uD83D\uDE01");
            } else if (v.getId() == R.id.tv_icon4) {
                editable.insert(index, "\uD83D\uDE02");
            } else if (v.getId() == R.id.tv_icon5) {
                editable.insert(index, "\uD83D\uDE03");
            } else if (v.getId() == R.id.tv_icon6) {
                editable.insert(index, "\uD83D\uDE04");
            } else if (v.getId() == R.id.tv_icon7) {
                editable.insert(index, "\uD83D\uDE05");
            } else if (v.getId() == R.id.tv_icon8) {
                editable.insert(index, "\uD83D\uDE06");
            }
        }
    }

    public void showKeyboard() {
        if (messageTextView != null) {
            //设置可获得焦点
            messageTextView.setFocusable(true);
            messageTextView.setFocusableInTouchMode(true);
            //请求获得焦点
            messageTextView.requestFocus();
            //调用系统输入法
//            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
//            imm.showSoftInput(messageTextView, 0);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(messageTextView, InputMethodManager.HIDE_NOT_ALWAYS);


        }
    }


    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
        rlDlg.setmHasInit(false);
        rlDlg.setmHasKeybord(false);
    }

    @Override
    public void show() {
        super.show();
    }


    class MyLengthFilter implements InputFilter {

        private final int mMax;
        private Context context;

        public MyLengthFilter(int max, Context context) {
            mMax = max;
            this.context = context;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                //这里，用来给用户提示
//                Toast.makeText(context, "字数不能超过" + mMax, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "评论的内容太多了，分成多条评论来发吧~", Toast.LENGTH_LONG).show();
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        /**
         * @return the maximum length enforced by this input filter
         */
        public int getMax() {
            return mMax;
        }
    }
}


package com.sun.mycommentdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sun.mycommentdemo.comment.CommendMsgDialog;
import com.sun.mycommentdemo.comment.FirstNode;
import com.sun.mycommentdemo.comment.SecondNode;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvOpen;
    private CommendMsgDialog mCommendMsgDialog;

    private boolean needNet = true;  //用于判断 之前是否已经获取过部分数据。已经获取过直接打开评论列表，未获取过则先获取数据
    private int pageNum = 1;       //当前获取页数
    private int totalPage = 3 ;    //TODO 总页数 需接口获取
    private int commentCount = 15 ; //TODO 总评论数 需获取

    /**
     * TODO 此处CommendMsgDialog 各种方法逻辑为根据我的本地项目拆分出来的，简单修改了一下，让他可以运行起来。需要根据自己的需求重构
     *      这里的两层列表接口 都是通过 页数去获取数据，这个页需要根据具体业务逻辑来修改。
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOpen =findViewById(R.id.main_tvOpen);
        tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });

        mCommendMsgDialog = new CommendMsgDialog(this, new CommendMsgDialog.OnListListener() {
            @Override
            public void getNextPage() {
//                getNextList();
                getFirstList();
            }

            @Override
            public void retryFirstPage() {
//                getRetryFirstPage();
                getFirstList();
            }

            @Override
            public void getSecondList(int id, int page, int parentPosition) {
                getCommentSecondList(id, page, parentPosition);
            }

            @Override
            public void comment(String content, int id, String beReplyId, String beReplyNickName, String beReplyAvatar, int parentPosition) {
                //评论
                commentVideo(content, id, beReplyId, beReplyNickName, beReplyAvatar, parentPosition);
            }

            @Override
            public void giveLikes(int type, int parentPosition, int position, int id, String upOrDown) {
                changeLike(type, parentPosition, position, id, upOrDown);
            }

            @Override
            public void delComment(int type,int position,int parentPosition,int id) {
                deleteComment(type,position,parentPosition,id);
            }
        });
        Window window = mCommendMsgDialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
    }

    //评论UI
    private void showCommentDialog() {
        if (needNet) {
            getFirstList();
        } else {
            mCommendMsgDialog.show();
        }

    }

    private void getFirstList() {
        //通过 videoId、pageNum 获取一级列表
        String firstListData = "[{\n" +
                "\"content\":\"她喜欢听歌时看评论，她说打动人的不是歌声，是歌中描述情景让人触动。她很脆弱，却也狠心。现在分开了，但是想你却已经成为一种习惯，强忍住不去打扰你，怕朋友也没得做。我网易里的歌全是你推荐的，现在的我整天都带着耳机，我会在你给我推荐歌上评论，希望能被顶上去，希望能被你看到，可可我想你\",\n" +
                "\"id\":1,\n" +
                "\"isLike\":true,\n" +
                "\"likeCount\":10,\n" +
                "\"ownerId\":\"123\",\n" +
                "\"parentId\":11,\n" +
                "\"replyCount\":20,\n" +
                "\"time\":\"58分钟前\",\n" +
                "\"userAvatar\":\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202105%2F10%2F20210510174256_4b4d0.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673056726&t=972eb583d0bd863b9eb980a5620f1d44\",\n" +
                "\"userId\":\"123\",\n" +
                "\"userNickName\":\"小可爱\",\n" +
                "\"commentCount\":11000,\n" +
                "\"allowComments\":1,\n" +
                "\"totalPage\":30\n" +
                "},\n" +
                "{\n" +
                "\"content\":\"在没有命运的现实世界，一切相遇都只是必然的结果，没有命中注定。你要永远相信，在某个选择里，你选择的可能爱的就不是现在的这个人，所以，在另一个选择里，这个人爱的不是你\",\n" +
                "\"id\":2,\n" +
                "\"isLike\":false,\n" +
                "\"likeCount\":0,\n" +
                "\"ownerId\":\"123\",\n" +
                "\"parentId\":11,\n" +
                "\"replyCount\":0,\n" +
                "\"time\":\"59分钟前\",\n" +
                "\"userAvatar\":\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202105%2F30%2F20210530110411_a06e5.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673056761&t=f4fc7c441be1852bfda67f42669107dd\",\n" +
                "\"userId\":\"5\",\n" +
                "\"userNickName\":\"喵喵喵\",\n" +
                "\"commentCount\":12,\n" +
                "\"allowComments\":1,\n" +
                "\"totalPage\":30\n" +
                "},\n" +
                "{\n" +
                "\"content\":\"没有拒绝 哪来的成功！你就天天表白，表白100个人，总有一个瞎了眼的。\",\n" +
                "\"id\":3,\n" +
                "\"isLike\":false,\n" +
                "\"likeCount\":100,\n" +
                "\"ownerId\":\"123\",\n" +
                "\"parentId\":11,\n" +
                "\"replyCount\":10,\n" +
                "\"time\":\"1小时前\",\n" +
                "\"userAvatar\":\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202012%2F26%2F20201226223704_3f25a.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673056777&t=5be46b4bfcf1f2068c28c59d8736c116\",\n" +
                "\"userId\":\"5\",\n" +
                "\"userNickName\":\"恶魔妈妈买面膜\",\n" +
                "\"commentCount\":12,\n" +
                "\"allowComments\":1,\n" +
                "\"totalPage\":30\n" +
                "}\n" +
                ",\n" +
                "{\n" +
                "\"content\":\"我还要长大，还要长大，艰难卓绝雨雪，几番留恋深渊，几经苦苦挣扎，几度从头站立。决不平输。决不泄气。\",\n" +
                "\"id\":4,\n" +
                "\"isLike\":false,\n" +
                "\"likeCount\":100,\n" +
                "\"ownerId\":\"123\",\n" +
                "\"parentId\":11,\n" +
                "\"replyCount\":10,\n" +
                "\"time\":\"1小时前\",\n" +
                "\"userAvatar\":\"https://img1.baidu.com/it/u=700513597,1909703999&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1671037200&t=62766b7d322a7288094a24a62a13f6a3\",\n" +
                "\"userId\":\"5\",\n" +
                "\"userNickName\":\"蔡徐坤情侣号\",\n" +
                "\"commentCount\":12,\n" +
                "\"allowComments\":1,\n" +
                "\"totalPage\":30\n" +
                "}\n" +
                ",\n" +
                "{\n" +
                "\"content\":\"或许，我们不是爱得不够深，只是伤得太彻底；不是来得太突然，只是走得太匆忙；不是命运太过无情，只是我好像走错了你的世界。\",\n" +
                "\"id\":5,\n" +
                "\"isLike\":false,\n" +
                "\"likeCount\":100,\n" +
                "\"ownerId\":\"123\",\n" +
                "\"parentId\":11,\n" +
                "\"replyCount\":10,\n" +
                "\"time\":\"1小时前\",\n" +
                "\"userAvatar\":\"https://img1.baidu.com/it/u=453253244,3693084626&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1671037200&t=7de3411868e4bb07d07dfd085b2c0854\",\n" +
                "\"userId\":\"5\",\n" +
                "\"userNickName\":\"美女\",\n" +
                "\"commentCount\":12,\n" +
                "\"allowComments\":1,\n" +
                "\"totalPage\":30\n" +
                "}]";
        List<FirstNode> firstList = JSONObject.parseArray(firstListData, FirstNode.class);

        if (pageNum == 1) {
            mCommendMsgDialog.setList(firstList);
            if (!mCommendMsgDialog.isShowing()) {
                mCommendMsgDialog.show();
            }
            needNet = false;
        } else {
            mCommendMsgDialog.setNextList(firstList);
        }
        if (totalPage > pageNum) {
            pageNum = pageNum + 1;
            mCommendMsgDialog.setLoadMoreComplete();
        } else {
            mCommendMsgDialog.setLoadMoreEnd();
        }

        //设置评论数量到Dialog
        mCommendMsgDialog.setCommentCount(commentCount);

        //TODO 设置 弹窗样式  列表、评论区关闭、网络获取失败，此处使用假数据
        mCommendMsgDialog.setCommentType(0);
        //TODO 没有数据时失败，使用setCommentType判断显示。已经获取过数据以后失败，调用此方法
//        mCommendMsgDialog.setLoadMoreFail();

    }

    private void getCommentSecondList(int id, int page, int parentPosition) {

        String secondListData ="[\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"beReplyAvatar\":\"https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg\",\n" +
                "\t\t\t\t\"beReplyId\":\"1560150216416563200\",\n" +
                "\t\t\t\t\"beReplyNickName\":\"影响\",\n" +
                "\t\t\t\t\"content\":\"咦\",\n" +
                "\t\t\t\t\"id\":11371,\n" +
                "\t\t\t\t\"isLike\":null,\n" +
                "\t\t\t\t\"likeCount\":0,\n" +
                "\t\t\t\t\"ownerId\":\"1270547962099015680\",\n" +
                "\t\t\t\t\"parentId\":11363,\n" +
                "\t\t\t\t\"replyCount\":null,\n" +
                "\t\t\t\t\"time\":\"2022-12-22\",\n" +
                "\t\t\t\t\"userAvatar\":\"https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg\",\n" +
                "\t\t\t\t\"userId\":\"1560150216416563200\",\n" +
                "\t\t\t\t\"userNickName\":\"影响\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"beReplyAvatar\":\"https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg\",\n" +
                "\t\t\t\t\"beReplyId\":\"1560150216416563200\",\n" +
                "\t\t\t\t\"beReplyNickName\":\"影响\",\n" +
                "\t\t\t\t\"content\":\"嗯2\",\n" +
                "\t\t\t\t\"id\":11370,\n" +
                "\t\t\t\t\"isLike\":null,\n" +
                "\t\t\t\t\"likeCount\":0,\n" +
                "\t\t\t\t\"ownerId\":\"1270547962099015680\",\n" +
                "\t\t\t\t\"parentId\":11363,\n" +
                "\t\t\t\t\"replyCount\":null,\n" +
                "\t\t\t\t\"time\":\"2022-12-22\",\n" +
                "\t\t\t\t\"userAvatar\":\"https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg\",\n" +
                "\t\t\t\t\"userId\":\"1560150216416563200\",\n" +
                "\t\t\t\t\"userNickName\":\"影响\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"beReplyAvatar\":\"https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg\",\n" +
                "\t\t\t\t\"beReplyId\":\"1560150216416563200\",\n" +
                "\t\t\t\t\"beReplyNickName\":\"影响\",\n" +
                "\t\t\t\t\"content\":\"嗯\",\n" +
                "\t\t\t\t\"id\":11369,\n" +
                "\t\t\t\t\"isLike\":null,\n" +
                "\t\t\t\t\"likeCount\":0,\n" +
                "\t\t\t\t\"ownerId\":\"1270547962099015680\",\n" +
                "\t\t\t\t\"parentId\":11363,\n" +
                "\t\t\t\t\"replyCount\":null,\n" +
                "\t\t\t\t\"time\":\"2022-12-22\",\n" +
                "\t\t\t\t\"userAvatar\":\"https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg\",\n" +
                "\t\t\t\t\"userId\":\"1560150216416563200\",\n" +
                "\t\t\t\t\"userNickName\":\"影响\"\n" +
                "\t\t\t}\n" +
                "\t\t]";

        List<SecondNode> secondNodes = JSONObject.parseArray(secondListData, SecondNode.class);
        mCommendMsgDialog.setSecondList(parentPosition, secondNodes);

    }

    private void commentVideo(String content, int id, String beReplyId, String beReplyNickName, String beReplyAvatar, int parentPosition) {
        String userId = "123";
        String userNickName = "影响";
        String userAvatar = "https://shys-image.oss-accelerate.aliyuncs.com/avatar/GyN6CBwjNNTFeWssQD8CxFGJR8a4DkQP.jpg";

        //评论成功
        if (parentPosition == -1) {
            //评论成功 添加一级列表
            mCommendMsgDialog.setCommentOne(12, userId, userNickName, userAvatar, content, "1270547962099015680");
        } else {
            //评论成功 添加二级列表
            mCommendMsgDialog.setCommentTwo(13, userId, userNickName, userAvatar, content, "1270547962099015680", beReplyId, beReplyNickName, beReplyAvatar, parentPosition);
        }

        commentCount = commentCount + 1;
    }

    private void changeLike(int type, int parentPosition, int position, int id, String upOrDown) {
        mCommendMsgDialog.setLike(type, parentPosition, position, upOrDown);
    }

    private void deleteComment(int type,int position,int parentPosition,int id) {
        mCommendMsgDialog.setDelete(type,position,parentPosition,id);

        commentCount = commentCount - 1;
    }

}
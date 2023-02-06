package com.sun.mycommentdemo.comment;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.module.LoadMoreModule;

import java.util.List;

public class CommendAdapter extends BaseNodeAdapter implements LoadMoreModule {

    public CommendAdapter() {
        super();
        // 注册Provider，总共有如下三种方式

        // 需要占满一行的，使用此方法（例如section）
        addFullSpanNodeProvider(new FirstNodeProvider());
        // 普通的item provider
        addNodeProvider(new SecondNodeProvider());
        // 脚布局的 provider
        addFooterNodeProvider(new FooterNodeProvider());
    }

    /**
     * 自行根据数据、位置等信息，返回 item 类型
     */
    @Override
    protected int getItemType(List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof FirstNode) {
            return 0;
        } else if (node instanceof SecondNode) {
            return 1;
        } else if (node instanceof FooterNode) {
            return 2;
        }
        return -1;
    }

    @Override
    public BaseLoadMoreModule addLoadMoreModule(BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseLoadMoreModule(baseQuickAdapter);
    }
}

package com.bhex.wallet.bh_main.my.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.SettingAdapter;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gongdongyang
 * 2020-3-12 15:48:18
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R2.id.recycler_setting)
    RecyclerView recycler_setting;

    private SettingAdapter mSettingAdapter;

    private List<MyItem> mItems;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addEvent() {
        mItems = MyHelper.getSettingItems(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_setting.setLayoutManager(layoutManager);
        //mMyAdapter.setHasStableIds(true);

        mSettingAdapter = new SettingAdapter(R.layout.item_my,mItems);

        recycler_setting.setAdapter(mSettingAdapter);

        mSettingAdapter.setOnItemClickListener((adapter, view, position) -> {
            clickItemAction(adapter, view, position);
        });
    }

    /**
     * 点击item事件
     * @param adapater
     * @param parent
     * @param position
     */
    private void clickItemAction(BaseQuickAdapter adapater, View parent,int position) {
        switch (position){
            case 0:
                ARouterUtil.startActivity(ARouterConfig.MINE_MAIN_PAGE);
        }
    }


}

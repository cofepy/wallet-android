package com.bhex.wallet.bh_main.my.ui.activity;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.SettingAdapter;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.event.CurrencyEvent;
import com.bhex.wallet.common.event.LanguageEvent;
import com.bhex.wallet.common.event.NightEvent;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.utils.SafeUilts;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-3-12 15:48:18
 * 设置
 */
public class SettingActivity extends BaseActivity{

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
        mItems = MyHelper.getSettingItems(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_setting.setLayoutManager(layoutManager);

        mSettingAdapter = new SettingAdapter(mItems);

        recycler_setting.setAdapter(mSettingAdapter);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,16),0,
                ColorUtil.getColor(this,R.color.global_divider_color));

        recycler_setting.addItemDecoration(ItemDecoration);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void addEvent() {

        mSettingAdapter.setOnItemClickListener((adapter, view, position) -> {
            clickItemAction(adapter, view, position);
        });

        mSettingAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(position == 2) {
                CheckedTextView ck = (CheckedTextView) view;
                ck.toggle();
                if(ck.isChecked()){
                    MMKVManager.getInstance().setSelectNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    MMKVManager.getInstance().setSelectNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                this.getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                NavigateUtil.startActivity(this,SettingActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                EventBus.getDefault().post(new NightEvent());
            } else if(position==3){
                CheckedTextView ck = (CheckedTextView) view;
                if(!ck.isChecked()){
                    if(SafeUilts.isFinger(this)){
                        MMKVManager.getInstance().mmkv().encode(BHConstants.FINGER_PWD_KEY,true);
                        ck.toggle();
                        ToastUtils.showToast(getResources().getString(R.string.set_finger_ok));
                    }
                }else {
                    MMKVManager.getInstance().mmkv().remove(BHConstants.FINGER_PWD_KEY);
                    ck.toggle();
                }
            }
        });


    }

    /**
     * 点击item事件
     * @param adapater
     * @param parent
     * @param position
     */
    private void clickItemAction(BaseQuickAdapter adapater, View parent,int position) {
        MyItem myItem = mItems.get(position);
        switch (position){
            case 0:
                ARouter.getInstance().build(ARouterConfig.MY_LANGUAE_SET_PAGE).withString("title",myItem.title).navigation();
                break;
            case 1:
                ARouter.getInstance().build(ARouterConfig.MY_Rate_setting).withString("title",myItem.title).navigation();
                break;
            case 4:
                ARouter.getInstance().build(ARouterConfig.MY_Security_Setting).withString("title",myItem.title).navigation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeLanguage(LanguageEvent language){
        /*mItems = MyHelper.getSettingItems(this);
        mSettingAdapter.getData().clear();
        mSettingAdapter.addData(mItems);*/
        recreate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCurrency(CurrencyEvent currency){
        mItems = MyHelper.getSettingItems(this);
        mSettingAdapter.getData().clear();
        mSettingAdapter.addData(mItems);

    }



    @Override
    protected void onResume() {
        super.onResume();
        mItems = MyHelper.getSettingItems(this);
        mSettingAdapter.getData().clear();
        mSettingAdapter.addData(mItems);
    }
}

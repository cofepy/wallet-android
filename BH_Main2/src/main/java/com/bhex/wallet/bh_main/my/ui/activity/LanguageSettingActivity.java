package com.bhex.wallet.bh_main.my.ui.activity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.ProgressDialogObserver;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.LanguageAdapter;
import com.bhex.wallet.bh_main.my.model.LanguageEntity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.event.LanguageEvent;
import com.bhex.wallet.common.utils.LanguageConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

@Route(path= ARouterConfig.MY_LANGUAE_SET_PAGE)
public class LanguageSettingActivity extends BaseActivity {


    @BindView(R2.id.rcv_language_set)
    RecyclerView rcvLanguageSet;

    LanguageAdapter mLanguageAdapter;
    
    private List<LanguageEntity> mLanguageList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_language_setting;
    }

    @Override
    protected void initView() {

        initData();

        mLanguageAdapter = new LanguageAdapter(R.layout.item_language,mLanguageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcvLanguageSet.setAdapter(mLanguageAdapter);
        rcvLanguageSet.setLayoutManager(layoutManager);

        RecycleViewDivider divider = new RecycleViewDivider(
                this, 1, 2,
                getResources().getColor(R.color.line_color));

        rcvLanguageSet.addItemDecoration(divider);
    }

    /**
     * 初始化语言数据
     */
    private void initData() {
        Locale locale = LocalManageUtil.getSetLanguageLocale(this);

        int  selectIndex = LocalManageUtil.getSetLanguageLocaleIndex(this);

        LogUtils.d("LanguageSettingActivity==>:",locale.getLanguage()+"=locale="+selectIndex);
        //LogUtils.d("LanguageSettingActivity","locale:"+locale.getLanguage());
        mLanguageList = new ArrayList<>();
        String []langArray = getResources().getStringArray(R.array.app_language_type);

        for (int i = 0; i < langArray.length; i++) {
            LanguageEntity language = new LanguageEntity();
            language.setFullName(langArray[i]);
            if((i+1)==selectIndex){
                language.setSelected(true);
            }else{
                language.setSelected(false);
            }

            language.setId(i+1);
            language.setShortName(LanguageConstants.SHORT_NAMES[i]);
            mLanguageList.add(language);
        }
    }

    @Override
    protected void addEvent() {

        mLanguageAdapter.setOnItemClickListener((adapter, view, position) -> {
            changeLanguage(position);
        });
    }

    /**
     * 语言设置
     * @param position
     */
    private void changeLanguage(int position){
        Observable.just(Integer.valueOf(position))
                //.compose(RxSchedulersHelper.io_main())
                .map(integer -> {
                    LanguageEntity languageEntity = mLanguageList.get(integer);
                    LogUtils.d("LanguageSettingActivity==>:","id==>:"+languageEntity.getId());
                    LocalManageUtil.saveSelectLanguage(LanguageSettingActivity.this, languageEntity.getId());
                    EventBus.getDefault().post(new LanguageEvent());
                    return Boolean.valueOf(true);
                }).delay(1200L, TimeUnit.MILLISECONDS).
                subscribe(new ProgressDialogObserver<Boolean>(LanguageSettingActivity.this,getString(R.string.http_loading)){
            @Override
            public void onSuccess(Boolean aBoolean) {
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
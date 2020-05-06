package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.TrusteeshipPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * gdy
 * 2020-3-4 18:15:01
 * 创建托管单元`
 */
@Route(path = ARouterConfig.TRUSTEESHIP_MNEMONIC_FRIST)
public class TrusteeshipActivity extends BaseCacheActivity<TrusteeshipPresenter> {

    WalletViewModel walletViewModel;

    @BindView(R2.id.btn_next)
    AppCompatButton btn_next;

    @BindView(R2.id.inp_wallet_name)
    InputView inp_wallet_name;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.tv_wallet_name_count)
    AppCompatTextView tv_wallet_name_count;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this);
    }

    @Override
    protected void initView() {

        LogUtils.d("TrusteeshipActivity===>:","way=="+BHUserManager.getInstance().getTmpBhWallet().getWay());
        mPresenter.setToolBarTitle();
        inp_wallet_name.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        //inp_wallet_name.getEditText().setText("Bluehelix Wallet");
    }

    @Override
    protected void addEvent() {
        //bhWalletDao = AppDataBase.getInstance(BaseApplication.getInstance()).bhWalletDao();
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        //walletViewModel.setmContext(this);

        inp_wallet_name.addTextWatch(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if(!TextUtils.isEmpty(inp_wallet_name.getInputString())){
                    btn_next.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
                    btn_next.setEnabled(true);
                }else{
                    btn_next.setBackgroundResource(R.drawable.btn_gray_e7ecf4);
                    btn_next.setEnabled(false);
                }
                int count = inp_wallet_name.getInputString().length();
                tv_wallet_name_count.setText(String.format(getString(R.string.pwd_index), count));

            }
        });

        inp_wallet_name.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtils.d("TrusteeshipActivit===","keyCode==="+KeyEvent.keyCodeToString(keyCode));
                return false;
            }
        });


    }


    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_next){
            //设置钱包用户名
            BHUserManager.getInstance().getTmpBhWallet()
                    .setName(inp_wallet_name.getInputString());
            NavitateUtil.startActivity(this,TrusteeshipSecActivity.class);
        }
    }

    /**
     * 生成助记词
     */
    /*private void generateMnemonic(String name, String pwd) {
        walletViewModel.generateMnemonic(name, pwd);

        walletViewModel.mutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if ("1".equals(status)) {
                    ToastUtils.showToast("创建成功");
                    NavitateUtil.startActivity(TrusteeshipActivity.this, TrusteeshipSuccessActivity.class);
                    MMKVManager.getInstance().mmkv().encode(BHConstants.FRIST_BOOT, true);
                } else {
                    ToastUtils.showToast("创建失败");
                }
            }
        });
    }*/


}

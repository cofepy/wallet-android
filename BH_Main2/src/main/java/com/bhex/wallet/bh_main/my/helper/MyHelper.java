package com.bhex.wallet.bh_main.my.helper;

import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.network.utils.PackageUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.enums.CURRENCY_TYPE;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MMKVManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 11:23
 */
public class MyHelper {

    public static List<MyItem> getAllItems(Context context){

        List<MyItem> myItems = new ArrayList<>();

        String [] res = context.getResources().getStringArray(R.array.my_list_item);
        MyItem item = null;
        for (int i = 0; i < res.length; i++) {
            if (i == 6) {
                item = new MyItem(i,res[i], false, PackageUtils.getVersionName(context));
            } else {
                item = new MyItem(i,res[i], true, "");
            }
            myItems.add(item);
        }

        if(BHUserManager.getInstance().getCurrentBhWallet().getWay()!= MAKE_WALLET_TYPE.创建助记词.getWay()
            || BHUserManager.getInstance().getCurrentBhWallet().isBackup==1){
            myItems.remove(0);
        }

        return myItems;
    }

    /**
     * 地址掩码
     */
    public static void proccessAddress(AppCompatTextView tv_address, String address){
        StringBuffer buf = new StringBuffer("");
        if(!TextUtils.isEmpty(address)){
            buf.append(address.substring(0,10))
                    .append("***")
                    .append(address.substring(address.length()-10,address.length()));
            tv_address.setText(buf.toString());
        }

    }


    public static List<MyItem> getSettingItems(Context context){

        List<MyItem> myItems = new ArrayList<>();

        int  selectIndex = LocalManageUtil.getSetLanguageLocaleIndex(context);

        String []langArray = context.getResources().getStringArray(R.array.app_language_type);

        String [] res = context.getResources().getStringArray(R.array.set_list_item);
        for (int i = 0; i < res.length; i++) {
            MyItem item = new MyItem(i,res[i], true, "");
            myItems.add(item);
        }

        myItems.get(0).rightTxt = langArray[selectIndex-1];

        //语言
        CURRENCY_TYPE.initCurrency(context);
        String currency_name = MMKVManager.getInstance().mmkv().decodeString(BHConstants.CURRENCY_USED,CURRENCY_TYPE.USD.shortName);

        myItems.get(1).rightTxt = CURRENCY_TYPE.getValue(currency_name).name+"("+CURRENCY_TYPE.getValue(currency_name)+")";
        return myItems;
    }
}

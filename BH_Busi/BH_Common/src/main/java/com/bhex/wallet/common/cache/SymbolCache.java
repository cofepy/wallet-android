package com.bhex.wallet.common.cache;

import android.text.TextUtils;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.cache.stategy.IStrategy;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.model.BHToken;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 17:57
 */
public class SymbolCache extends BaseCache {

    private static final String TAG = SymbolCache.class.getSimpleName();

    public static final String CACHE_KEY = "SymbolCache";


    private Map<String, BHToken> symbolMap = new ConcurrentHashMap();

    private static volatile SymbolCache _instance = new SymbolCache();

    private SymbolCache(){

    }

    public static SymbolCache getInstance(){
        return _instance;
    }

    @Override
    public synchronized void beginLoadCache() {
        loadSymbol();
    }

    private synchronized void loadSymbol(){
        Type type = (new TypeToken<JsonObject>() {}).getType();

        BHttpApi.getService(BHttpApiInterface.class).loadSymbol(1,1000)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_KEY, type,getCacheStrategy()))
                .map(new CacheResult.MapFunc())
                .subscribe(new BHBaseObserver<JsonObject>(false) {
                    @Override
                    protected void onSuccess(JsonObject jsonObject) {
                        if(!JsonUtils.isHasMember(jsonObject,"items")){
                            return;
                        }
                        List<BHToken> coinList = JsonUtils.getListFromJson(jsonObject.toString(),"items", BHToken.class);
                        if(ToolUtils.checkListIsEmpty(coinList)){
                            return;
                        }
                        symbolMap.clear();
                        //缓存所有的token
                        StringBuffer sb = new StringBuffer();
                        for(BHToken item:coinList){
                            symbolMap.put(item.symbol,item);
                            sb.append(item.symbol).append("_");
                        }
                        if(!TextUtils.isEmpty(sb)){
                            MMKVManager.getInstance().mmkv().encode(BHConstants.SYMBOL_DEFAULT_KEY,sb.toString());
                        }
                    }


                    @Override
                    protected void onFailure(int code, String errorMsg) {
                        super.onFailure(code, errorMsg);
                    }
                });
    }

    public synchronized BHToken getBHToken(String symbol){
        return symbolMap.get(symbol);
    }

    public synchronized List<BHToken> loadTokenByChain(String chain){
        List<BHToken> list = new ArrayList();
        if(ToolUtils.checkMapEmpty(symbolMap)){
            return null;
        }

        for (Map.Entry<String,BHToken> item : symbolMap.entrySet()){
            if(item.getValue().chain.equalsIgnoreCase(chain)){
                list.add(item.getValue());
            }
        }
        return list;
    }

    public synchronized int getDecimals(String symbol){
        if(symbolMap.get(symbol)!=null){
            return symbolMap.get(symbol).decimals;
        }else{
            return BHConstants.BHT_DEFAULT_DECIMAL;
        }
    }

}
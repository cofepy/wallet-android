package com.bhex.wallet.common.api;

import com.bhex.network.base.NetworkApi;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.DateUtil;
import com.bhex.wallet.common.interceptor.AuthInterceptor;

import java.io.IOException;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/9
 * Time: 23:34
 */
public class BHttpApi extends NetworkApi {

    private static volatile BHttpApi INSTANCE;

    public static BHttpApi getInstance(){

        if(INSTANCE==null){
            synchronized (BHttpApi.class){
                if(INSTANCE==null){
                    INSTANCE = new BHttpApi();
                }
            }
        }

        return INSTANCE;
    }


    @Override
    protected Interceptor getInterceptor() {
        /*return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String timeStr = DateUtil.getTimeStr();
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Source", "source");
                builder.addHeader("Date", timeStr);
                return chain.proceed(builder.build());
            }
        };*/
        return new AuthInterceptor();
    }


    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(T response) throws Exception {
                /*if (response instanceof BaseResponse && ((BaseResponse) response).code != 0) {
                    ExceptionHandler.ServerException exception = new ExceptionHandler.ServerException();
                    exception.code = ((BaseResponse) response).code;
                    exception.message = ((BaseResponse) response).message != null ? ((BaseResponse) response).message : "";
                    throw exception;
                }*/
                return response;
            }
        };
    }
    public static  <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    public String getFormal() {
        //return "http://public-chain-mainnet-631149863.ap-northeast-1.elb.amazonaws.com:26657/";
        return BHConstants.API_BASE_URL;
    }

    @Override
    public String getTest() {
        //return "http://public-chain-mainnet-631149863.ap-northeast-1.elb.amazonaws.com:26657/";
        return BHConstants.API_BASE_URL;
    }
}

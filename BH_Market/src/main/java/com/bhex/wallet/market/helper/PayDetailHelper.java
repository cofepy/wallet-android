package com.bhex.wallet.market.helper;

import android.content.Context;
import android.text.TextUtils;

import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.model.H5Sign;
import com.bhex.wallet.market.model.PayDetailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongdongyang
 * 2020-9-23 20:44:00
 */
public class PayDetailHelper {

    public static List<PayDetailItem> loadPayItemByType(Context context, H5Sign h5Sign) {
        try{
            List<PayDetailItem> list = null;
            if (h5Sign.type.equals(TRANSCATION_BUSI_TYPE.添加流动性.getType())) {
                list = make_添加流动性_list(context, h5Sign);
            }else if(h5Sign.type.equals(TRANSCATION_BUSI_TYPE.兑换_输入确定.getType())){
                list = make_兑换_输入确定_list(context, h5Sign);
            }else if(h5Sign.type.equals(TRANSCATION_BUSI_TYPE.移除流动性.getType())){
                list = make_移除流动性_list(context, h5Sign);
            }
            return list;
        }catch (Exception e){
            return null;
        }
        
    }

    public static List<PayDetailItem> make_添加流动性_list(Context context, H5Sign h5Sign) {
        List<PayDetailItem> list = new ArrayList<>();
        PayDetailItem item0 = new PayDetailItem(context.getString(R.string.pay_info), TRANSCATION_BUSI_TYPE.getValue(h5Sign.type));
        list.add(item0);
        流动性_Entitiy v_流动性_Entitiy = JsonUtils.fromJson(h5Sign.value.toString(), 流动性_Entitiy.class);

        BHToken bhToken_a = CacheCenter.getInstance().getSymbolCache().getBHToken(v_流动性_Entitiy.token_a.toLowerCase());
        if (RegexUtil.checkNumeric(v_流动性_Entitiy.min_token_a_amount)) {
            double min_token_a_amount = NumberUtil.divide(v_流动性_Entitiy.min_token_a_amount, Math.pow(10, bhToken_a.decimals) + "",bhToken_a.decimals);
            PayDetailItem item1 = new PayDetailItem(context.getString(R.string.pay_token), NumberUtil.toPlainString(min_token_a_amount) + "  " + v_流动性_Entitiy.token_a.toUpperCase());
            list.add(item1);
        }

        BHToken bhToken_b = CacheCenter.getInstance().getSymbolCache().getBHToken(v_流动性_Entitiy.token_b.toLowerCase());

        if (RegexUtil.checkNumeric(v_流动性_Entitiy.min_token_b_amount)) {
            double min_token_b_amount = NumberUtil.divide(v_流动性_Entitiy.min_token_b_amount, Math.pow(10, bhToken_b.decimals) + "",bhToken_b.decimals);
            PayDetailItem item2 = new PayDetailItem(context.getString(R.string.pay_token), NumberUtil.toPlainString(min_token_b_amount) + "  " + v_流动性_Entitiy.token_b.toUpperCase());
            list.add(item2);
        }

        PayDetailItem item3 = new PayDetailItem(context.getString(R.string.payment_address), v_流动性_Entitiy.from);
        list.add(item3);

        PayDetailItem item4 = new PayDetailItem(context.getString(R.string.gas_fee), BHConstants.BHT_DEFAULT_FEE + BHConstants.BHT_TOKEN.toUpperCase());
        list.add(item4);
        return list;
    }



    public static class 流动性_Entitiy {

        /**
         * from : HBCioVa2xVPuXhseov7KkMMNdV2GwnEuEopd
         * expired_at : -1
         * token_a : hbc
         * token_b : sal
         * min_token_a_amount : 25000000000000000000
         * min_token_b_amount : 10000000000000000
         */

        public String from;
        public int expired_at;
        public String token_a;
        public String token_b;
        public String min_token_a_amount;
        public String min_token_b_amount;


    }

    public static List<PayDetailItem> make_兑换_输入确定_list(Context context, H5Sign h5Sign) {
        List<PayDetailItem> list = new ArrayList<>();
        PayDetailItem item0 = new PayDetailItem(context.getString(R.string.pay_info), TRANSCATION_BUSI_TYPE.getValue(h5Sign.type));
        list.add(item0);

        兑换_Entitiy v_兑换_Entitiy = JsonUtils.fromJson(h5Sign.value.toString(), 兑换_Entitiy.class);
        BHToken bhToken_a = CacheCenter.getInstance().getSymbolCache().getBHToken(v_兑换_Entitiy.swap_path.get(0).toLowerCase());

        double amount_in = NumberUtil.divide(v_兑换_Entitiy.amount_in, Math.pow(10, bhToken_a.decimals) + "",bhToken_a.decimals);
        String amount_int_info = NumberUtil.toPlainString(amount_in).concat(v_兑换_Entitiy.swap_path.get(0).toUpperCase());
        PayDetailItem item1 = new PayDetailItem(context.getString(R.string.payment_amount),amount_int_info);
        list.add(item1);
        
        PayDetailItem item2 = new PayDetailItem(context.getString(R.string.payment_address), v_兑换_Entitiy.from);
        list.add(item2);
        
        PayDetailItem item3 = new PayDetailItem(context.getString(R.string.gas_fee), BHConstants.BHT_DEFAULT_FEE + BHConstants.BHT_TOKEN.toUpperCase());
        list.add(item3);
        return list;
    }

    public static class 兑换_Entitiy{

        /**
         * from : HBCXoBsyoTeK5X74yVqMesRQNrc1moMw1AkT
         * referer : HBCXoBsyoTeK5X74yVqMesRQNrc1moMw1AkT
         * receiver : HBCXoBsyoTeK5X74yVqMesRQNrc1moMw1AkT
         * expired_at : 1200
         * amount_int : 10000000000000000000000
         * min_amount_out : 44529642218077.905
         * swap_path : ["hbc","dot"]
         */

        public String from;
        public String referer;
        public String receiver;
        public int expired_at;
        public String amount_in;
        public String min_amount_out;
        public List<String> swap_path;

    }

    /**
     * 移除流动性
     * @param context
     * @param h5Sign
     * @return
     */
    public static List<PayDetailItem> make_移除流动性_list(Context context, H5Sign h5Sign) {
        List<PayDetailItem> list = new ArrayList<>();
        PayDetailItem item0 = new PayDetailItem(context.getString(R.string.pay_info), TRANSCATION_BUSI_TYPE.getValue(h5Sign.type));
        list.add(item0);

        移除流动性_Entitiy v_移除流动性_Entitiy = JsonUtils.fromJson(h5Sign.value.toString(), 移除流动性_Entitiy.class);

        PayDetailItem item2 = new PayDetailItem(context.getString(R.string.payment_address), v_移除流动性_Entitiy.from);
        list.add(item2);

        PayDetailItem item3 = new PayDetailItem(context.getString(R.string.gas_fee), BHConstants.BHT_DEFAULT_FEE + BHConstants.BHT_TOKEN.toUpperCase());
        list.add(item3);
        return list;
    }


    public static class 移除流动性_Entitiy {


        /**
         * from : HBCPCkU7Hhi45YwaMZ1jg1LxCse3gojFnTqb
         * expired_at : -1
         * token_a : dot
         * token_b : hbc
         * liquidity : 167266584349216.5
         */
        public String from;
        public String expired_at;
        public String token_a;
        public String token_b;
        public String liquidity;


    }


    public static class 撤单_Entitiy{

        /**
         * from : HBCPCkU7Hhi45YwaMZ1jg1LxCse3gojFnTqb
         * order_ids :
         */

        public String from;
        public List<String> order_ids;
    }


    //判断是否弹出支付详情
    public static boolean isShowPayDetail(String tx_type){
        boolean flag = true;
        if(TextUtils.isEmpty(tx_type)){
            return false;
        }
        if(tx_type.equals(TRANSCATION_BUSI_TYPE.撤单.getType())||tx_type.equals(TRANSCATION_BUSI_TYPE.撤单.getType())
                || tx_type.equals(TRANSCATION_BUSI_TYPE.限价单兑换.getType())){
            return false;
        }
        return flag;
    }

    //判断是否有订单
    public static boolean isHasOrders(H5Sign h5Sign) {
        boolean flag = true;

        if(h5Sign.type.equals(TRANSCATION_BUSI_TYPE.撤单.getType())){
            撤单_Entitiy v_撤单_Entitiy = JsonUtils.fromJson(h5Sign.value.toString(), 撤单_Entitiy.class);
            if(TextUtils.isEmpty(v_撤单_Entitiy.order_ids.toString())|| ToolUtils.checkListIsEmpty(v_撤单_Entitiy.order_ids)){
                return false;
            }
        }
        return flag;
    }
}

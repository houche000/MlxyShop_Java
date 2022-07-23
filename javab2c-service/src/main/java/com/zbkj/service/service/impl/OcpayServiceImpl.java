package com.zbkj.service.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.request.OcpayTestRequest;
import com.zbkj.common.response.OcpayWithdrawResponse;
import com.zbkj.common.utils.CommonUtil;
import com.zbkj.common.utils.HttpClientHelper;
import com.zbkj.common.utils.SHAUtil;
import com.zbkj.service.service.OcpayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author XiaoDequan
 * @description Ocpay 支付业务逻辑
 * @date 2022-07-11
 */
@Service
public class OcpayServiceImpl implements OcpayService {

    @Value("${ocpay.apiurl}")
    private String apiurl;

    @Value("${ocpay.apikey}")
    private String apikey;

    @Value("${ocpay.apisecret}")
    private String apisecret;

    @Value("${ocpay.description}")
    private String description;

    //用户银行卡提现，eg1：提现金额,eg2:提现银行卡所属银行,eg3：银行卡户名,eg4：银行卡账号
    @Override
    public OcpayWithdrawResponse withdraw(Integer id,BigDecimal extractPrice, String bankName, String bankAccountName, String bankAccountNum) throws IOException {

        //格式化金额输出
        String amount = exchangePriceToStr(extractPrice);

        //封装体现参数，转json传参
        HashMap<String, String> withdrawParams = new HashMap<>();
        withdrawParams.put("key",apikey);
        withdrawParams.put("amount",amount);
        String refNo=UUID.randomUUID().toString().replace("-", "");
        //拼接
        refNo=id+"_"+refNo;
        withdrawParams.put("ref_no", refNo);
        withdrawParams.put("description",description);
        withdrawParams.put("bank",bankName);
        withdrawParams.put("bank_account_name",bankAccountName);
        withdrawParams.put("bank_account_number",bankAccountNum);
        String hash = new SHAUtil().SHA256(apikey + refNo + amount + apisecret);
        withdrawParams.put("hash",hash);
        String paramJson = JSONUtil.toJsonStr(withdrawParams);

        //执行提现API
//         String resultJson="{\"status\":false,\"message\":\"Mismatch hash\"}";
        String resultJson = HttpClientHelper.sendPost(apiurl, paramJson);

        //根据API返回不同的状态类型，封装结果集
        boolean isJson = CommonUtil.isJSON2(resultJson);
        OcpayWithdrawResponse withdrawResponse = new OcpayWithdrawResponse();
        if (isJson){
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            Boolean status = jsonObject.getBoolean("status");
            Map data=new HashMap();
            if (status){
                data = jsonObject.getObject("data", Map.class);
            }else {
                String message = jsonObject.getString("message");
                data.put("message",message);
            }
            withdrawResponse.setStatus(status);
            withdrawResponse.setReturnData(data);
        }else {
            withdrawResponse.setStatus(false);
            Map returnData = new HashMap<>();
            returnData.put("message","访问支付受限You don't have access to the url you where trying to reach");
            withdrawResponse.setReturnData(returnData);
        }
        return withdrawResponse;
    }

    //格式化金额输出
    public String  exchangePriceToStr(BigDecimal price) {
        String priceStr =price.toString();
        if (price.compareTo(new BigDecimal(1))>0){
            //大于1
            priceStr= price.multiply(new BigDecimal(100)).toPlainString();
            if (priceStr.indexOf(".")>0){
                priceStr = priceStr.substring(0, priceStr.indexOf("."));
            }
        }else {
            priceStr=priceStr.substring(0,priceStr.indexOf(".")+3);
            priceStr = priceStr.replace(".", "");
        }
        return priceStr;
    }

    //测试
//    @Override
//    public OcpayWithdrawResponse executeWithdraw(OcpayTestRequest ocpay) {
//        try {
//            OcpayWithdrawResponse withdraw = withdraw(11,ocpay.getExtractPrice(), ocpay.getBankName(), ocpay.getBankAccountName(), ocpay.getBankAccountNum());
//            return withdraw;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new OcpayWithdrawResponse();
//        }
//    }

}

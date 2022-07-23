package com.zbkj.service.service;

import com.zbkj.common.request.OcpayTestRequest;
import com.zbkj.common.response.OcpayWithdrawResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author XiaoDequan
 * @description Ocpay 支付业务逻辑
 * @date 2022-07-11
 */
public interface OcpayService {

    //用户银行卡提现，eg1：提现金额,eg2:提现银行卡所属银行,eg3：银行卡户名,eg4：银行卡账号
    public OcpayWithdrawResponse withdraw(Integer id,BigDecimal amount, String bankName, String bankAccountName, String bankAccountNum) throws IOException;

    //测试模拟
//    public OcpayWithdrawResponse executeWithdraw(OcpayTestRequest ocpay);
}

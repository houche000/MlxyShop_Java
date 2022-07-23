package com.zbkj.admin.controller;

import com.zbkj.common.request.OcpayTestRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.OcpayWithdrawResponse;
import com.zbkj.service.service.OcpayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/ocpay")
@Api(tags = "Ocpay支付模块")
public class OcpayController {

    @Autowired
    private OcpayService ocpayService;

    /**
     * 测试用户提现
     */
//    @ApiOperation(value = "测试用户提现 ")
//    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
//    public CommonResult<OcpayWithdrawResponse> withdraw(@RequestBody OcpayTestRequest ocpay){
//        OcpayWithdrawResponse withdrawResponse = ocpayService.executeWithdraw(ocpay);
//        if (withdrawResponse==null){
//            return CommonResult.failed();
//        }
//       return CommonResult.success(withdrawResponse);
//    }
}

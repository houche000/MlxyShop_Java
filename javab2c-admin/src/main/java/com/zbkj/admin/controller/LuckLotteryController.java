package com.zbkj.admin.controller;


import com.zbkj.common.model.luck.LuckLottery;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.LuckLotteryService;
import com.zbkj.service.service.SystemAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/admin/luck/luckLottery")
@Api(tags = "抽奖活动表") //配合swagger使用
public class LuckLotteryController {

    @Autowired
    LuckLotteryService luckLotteryService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 查询抽奖活动详情
     *
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:luckLottery:info')")
    @ApiOperation(value = "查询抽奖活动详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<LuckLottery> info(@RequestParam(value = "id") Integer id) {
        LuckLottery luckLottery = luckLotteryService.getById(id);
        return CommonResult.success(luckLottery);
    }

    /**
     * 更新抽奖活动
     */
    @PreAuthorize("hasAuthority('admin:luckLottery:update')")
    @ApiOperation(value = "更新抽奖活动")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<Boolean> update(LuckLottery luckLottery) {
        luckLottery.setImage(systemAttachmentService.clearPrefix(luckLottery.getImage()));
        luckLottery.setDesc(systemAttachmentService.clearPrefix(luckLottery.getDesc()));
        luckLottery.setContent(systemAttachmentService.clearPrefix(luckLottery.getContent()));
        if (luckLottery.getId() == null) {
            luckLottery.setId(1);
            luckLottery.setAddTime(System.currentTimeMillis());
            luckLotteryService.save(luckLottery);
        } else {
            luckLotteryService.updateById(luckLottery);
        }
        return CommonResult.success(true);
    }
}

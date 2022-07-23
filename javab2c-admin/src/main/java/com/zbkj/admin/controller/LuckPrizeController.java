package com.zbkj.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.model.luck.LuckPrize;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.LuckPrizeService;
import com.zbkj.service.service.SystemAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/admin/luck/luckPrize")
@Api(tags = "抽奖奖品表") //配合swagger使用
public class LuckPrizeController {

    @Autowired
    private LuckPrizeService luckPrizeService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 查询抽奖活动奖品列表
     */
    @PreAuthorize("hasAuthority('admin:luckPrize:list')")
    @ApiOperation(value = "查询抽奖活动奖品列表") //配合swagger使用
    @RequestMapping(value = "/list/{lotteryId}", method = RequestMethod.GET)
    public CommonResult<List<LuckPrize>> getList(@PathVariable Integer lotteryId) {
        LambdaQueryWrapper<LuckPrize> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LuckPrize::getLotteryId, lotteryId);
        queryWrapper.eq(LuckPrize::getIsDel, false);
        List<LuckPrize> list = luckPrizeService.list(queryWrapper);
        return CommonResult.success(list);
    }

    /**
     * 更新抽奖活动奖品
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "更新抽奖活动")
    @PreAuthorize("hasAuthority('admin:luckPrize:update')")
    @ApiOperation(value = "更新抽奖活动奖品")
    @RequestMapping(value = "/update/{lotteryId}", method = RequestMethod.POST)
    public CommonResult<Boolean> update(@PathVariable Integer lotteryId, @RequestBody List<LuckPrize> luckPrizeList) {
        // 设置更新时间
        long timeMillis = System.currentTimeMillis();
        for (LuckPrize prize : luckPrizeList) {
            prize.setAddTime(timeMillis);
        }
        for (LuckPrize prize : luckPrizeList) {
            prize.setImage(systemAttachmentService.clearPrefix(prize.getImage()));
        }
        luckPrizeService.update(new LambdaUpdateWrapper<LuckPrize>().eq(LuckPrize::getLotteryId, lotteryId).set(LuckPrize::getIsDel, true));
        luckPrizeService.saveBatch(luckPrizeList);
        return CommonResult.success(true);

    }
}

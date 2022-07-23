package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.finance.UserExtractInfo;

/**
 * UserExtractService 接口









 *
 * @author mwxmmy
 */
public interface UserExtractInfoService extends IService<UserExtractInfo> {

    UserExtractInfo getUserExtractInfoByUserId(Integer userId);
}

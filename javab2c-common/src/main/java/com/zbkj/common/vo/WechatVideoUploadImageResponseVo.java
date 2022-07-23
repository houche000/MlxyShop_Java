package com.zbkj.common.vo;

import lombok.Data;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class WechatVideoUploadImageResponseVo extends BaseResultResponseVo {

    private imageInfo img_info;

    @Data
    class imageInfo{
        private String media_id;
    }
}

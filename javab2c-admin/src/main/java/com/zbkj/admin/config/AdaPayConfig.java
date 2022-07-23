package com.zbkj.admin.config;

import com.huifu.adapay.Adapay;
import com.huifu.adapay.model.MerConfig;
import com.zbkj.service.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 支付宝配置
 */
@Component
public class AdaPayConfig implements ApplicationRunner {


    public static final String ADA_API_KEY = "ada_api_key";
    public static final String ADA_MERCHANT_KEY = "ada_merchant_key";
    public static final String ADA_APPID = "ada_appid";
    public static final String ADA_MOCK_API_KEY = "ada_mock_api_key";
    // 私钥
    public static final String PRIVATE_KEY = "ada_private_key";
    public static final String ADA_CALLBACK_URL = "ada_callback_url";
    /**
     * 1-沙盒环境，2-正式环境
     */
    public static final String ADA_PROD_MODE = "ada_prod_mode";
    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public void run(ApplicationArguments args) {
        MerConfig merConfig = new MerConfig();
        String modelValue = systemConfigService.getValueByKey(ADA_PROD_MODE);
        if ("1".equals(modelValue)) {
            Adapay.prodMode = false;
        } else {
            Adapay.prodMode = true;
        }
        merConfig.setApiKey(systemConfigService.getValueByKey(ADA_API_KEY));
        merConfig.setApiMockKey(systemConfigService.getValueByKey(ADA_MOCK_API_KEY));
        merConfig.setRSAPrivateKey(systemConfigService.getValueByKey(PRIVATE_KEY));
        try {
            Adapay.initWithMerConfig(merConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

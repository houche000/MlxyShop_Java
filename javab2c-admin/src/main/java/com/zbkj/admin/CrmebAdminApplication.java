package com.zbkj.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 程序主入口
 */
@EnableAsync //开启异步调用
@EnableSwagger2
@Configuration
@EnableTransactionManagement
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //去掉数据源
//@ComponentScan(basePackages={"com.utils",
//        "com.zbkj.crmeb",
//        "com.exception",
//        "com.common",
//        "com.aop"}) //扫描utils包和父包
//@MapperScan(basePackages = {"com.zbkj.crmeb.*.dao", "com.zbkj.crmeb.*.*.dao"})
@ComponentScan(basePackages = {"com.zbkj"})
@MapperScan(basePackages = {"com.zbkj.**.dao"})
@EnableScheduling
public class CrmebAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmebAdminApplication.class, args);
    }

}

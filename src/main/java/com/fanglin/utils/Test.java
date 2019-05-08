package com.fanglin.utils;

import com.fanglin.tkmapper.generator.AutoGenerator;
import com.fanglin.tkmapper.generator.config.DataSourceConfig;
import com.fanglin.tkmapper.generator.config.GlobalConfig;
import com.fanglin.tkmapper.generator.config.PackageConfig;
import com.fanglin.tkmapper.generator.config.StrategyConfig;
import com.fanglin.tkmapper.generator.config.rules.DateType;

import java.util.Date;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/19 17:20
 **/
public class Test {
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator generator = new AutoGenerator();
        // 策略配置
        StrategyConfig strategy = new StrategyConfig()
            .setInclude("refund_history");
        generator.setStrategy(strategy);
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig()
            .setFileOverride(true)
            .setAuthor("彭方林")
            .setSwagger2(true)
            .setDateType(DateType.ONLY_DATE);
        generator.setGlobalConfig(globalConfig);
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.test");
        generator.setPackageConfig(packageConfig);
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
            .setUrl("jdbc:mysql://127.0.0.1:3306/test")
            .setDriverName("com.mysql.cj.jdbc.Driver")
            .setUsername("root")
            .setPassword("123456");
        generator.setDataSource(dataSourceConfig);
        generator.execute();
    }
}

package com.microservice.redpackage.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author 上官炳强
 * @description
 * @since 2018-09-18 / 11:39:24
 */
@Configuration
@PropertySource(value = "classpath:common-config/jdbc.properties")
public class JdbcConfig {

    @Autowired
    private Environment environment;

    /**
     * C3P0数据库连接池配置
     *
     * @return 连接池实例
     * @throws PropertyVetoException 初始化异常
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(environment.getProperty("database.url"));
        dataSource.setDriverClass(environment.getProperty("database.driver"));
        dataSource.setPassword(environment.getProperty("database.password"));
        dataSource.setUser(environment.getProperty("database.username"));
        return dataSource;
    }

}

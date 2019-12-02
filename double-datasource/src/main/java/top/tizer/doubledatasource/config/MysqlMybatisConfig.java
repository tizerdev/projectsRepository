package top.tizer.doubledatasource.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
* @Moudle MysqlMybatisConfig.java
*
* @author joker
*
* @email tizerdev@foxmail.com
*
* Created by 2019/11/21 16:56
*
* @description mysql数据库配置
**/
@Configuration
@MapperScan(basePackages = {"top.tizer.doubledatasource.mapper.mysql"},sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlMybatisConfig {

    @Value("${spring.datasource.mysql.filters}")
    private String filters;

    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.mysql.username}")
    private String username;

    @Value("${spring.datasource.mysql.password}")
    private String password;

    @Value("${spring.datasource.mysql.url}")
    private String url;


    @Value("${spring.datasource.mysql.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.mysql.max-active}")
    private int maxActive;
    @Value("${spring.datasource.mysql.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.mysql.max-wait}")
    private long maxWait;
    @Value("${spring.datasource.mysql.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.mysql.min-evictable-idle-time-millis}")
    private long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.mysql.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource.mysql.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.mysql.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.mysql.test-on-return}")
    private boolean testOnReturn;


    @Bean(name = "mysqlDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() throws SQLException{
        DruidDataSource druid = new DruidDataSource();

        druid.setFilters(filters);
        druid.setDriverClassName(driverClassName);
        druid.setUrl(url);
        druid.setUsername(username);
        druid.setPassword(password);

        druid.setInitialSize(initialSize);
        //最大連接池的數量
        druid.setMaxActive(maxActive);
        //最小连接池数量
        druid.setMinIdle(minIdle);
        //获取连接时最大等待时间，单位毫秒
        druid.setMaxWait(maxWait);
        //间隔多久进行一次检测，检测需要关闭的空闲连接
        druid.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //一个连接在池中最小生存时间
        druid.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        //用来检测连接是否有效的sql
        druid.setValidationQuery(validationQuery);
        //建议配置为true，不影响性能，并且保证安全性。
        druid.setTestWhileIdle(testWhileIdle);
        //申请连接时执行validationQuery 检测连接是否有效
        druid.setTestOnBorrow(testOnBorrow);
        druid.setTestOnReturn(testOnReturn);

        return druid;

    }

    @Bean(name = "mysqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource mysqlDataSource)
        throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(mysqlDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:top/tizer/doubledatasource/mapper/mysql/*/*Mapper.xml"));
            return bean.getObject();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Bean(name = "mysqlSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory mysqlSqlSessionFactory){
        SqlSessionTemplate template = new SqlSessionTemplate(mysqlSqlSessionFactory);
        return template;
    }
}

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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = {"top.tizer.doubledatasource.mapper.oracle"},sqlSessionFactoryRef = "oracleSqlSessionFactory")
public class OracleMybatisConfig {

    @Value("${spring.datasource.oracle.filters}")
    private String filters;

    @Value("${spring.datasource.oracle.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.oracle.username}")
    private String username;

    @Value("${spring.datasource.oracle.password}")
    private String password;

    @Value("${spring.datasource.oracle.url}")
    private String url;


    @Value("${spring.datasource.oracle.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.oracle.max-active}")
    private int maxActive;
    @Value("${spring.datasource.oracle.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.oracle.max-wait}")
    private long maxWait;
    @Value("${spring.datasource.oracle.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.oracle.min-evictable-idle-time-millis}")
    private long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.oracle.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource.oracle.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.oracle.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.oracle.test-on-return}")
    private boolean testOnReturn;

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() throws SQLException {
        DruidDataSource druid = new DruidDataSource();

        druid.setFilters(filters);
        druid.setDriverClassName(driverClassName);
        druid.setUsername(username);
        druid.setPassword(password);
        druid.setUrl(url);

        druid.setTestWhileIdle(testWhileIdle);
        druid.setInitialSize(initialSize);
        druid.setMaxActive(maxActive);
        druid.setMinIdle(minIdle);
        druid.setMaxWait(maxWait);
        druid.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druid.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druid.setValidationQuery(validationQuery);
        druid.setTestOnReturn(testOnReturn);
        druid.setTestOnBorrow(testOnBorrow);

        return druid;
    }

    @Bean(name = "oracleSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("oracleDataSource") DataSource oracleDataSource)
            throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(oracleDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:top/tizer/doubledatasource/mapper/oracle/*/*Mapper.xml"));
            return bean.getObject();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Bean(name = "oracleSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("oracleSqlSessionFactory") SqlSessionFactory oracleSqlSessionFactory){
        SqlSessionTemplate template = new SqlSessionTemplate(oracleSqlSessionFactory);
        return template;
    }

}

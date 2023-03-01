package kr.co.pulmuone.v1.comm.config;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "slaveCjFrontSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.v1.comm.mappers.slaveCjFront"
        }
)
public class SlaveCjFrontDatabaseConfig {
    @Value("${spring.datasource.hikari.slave-cj-front.mybatis.config-location}")
    private String mybatisConfigLocation;

    @Bean(name = "slaveCjFrontDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-cj-front")
    public DataSource slaveCjFrontDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveCjFrontSqlSessionFactory")
    public SqlSessionFactory slaveCjFrontSqlSessionFactory(@Qualifier("slaveCjFrontDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);

        Resource[] arrResource = resolver.getResources("classpath*:/persistence/mappers/slaveCjFront/**/*.xml");
        sqlSessionFactoryBean.setMapperLocations(arrResource);
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(mybatisConfigLocation));
        sqlSessionFactoryBean.setTypeHandlersPackage("kr.co.pulmuone.v1.comm.base.mybatis.hanlder");

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slaveCjFrontSqlSessionTemplate")
    public SqlSessionTemplate slaveCjFrontSqlSessionTemplate(
            @Qualifier("slaveCjFrontSqlSessionFactory") SqlSessionFactory slaveCjFrontSqlSessionFactory) {
        return new SqlSessionTemplate(slaveCjFrontSqlSessionFactory);
    }

    @Bean(name = "slaveCjFrontTransactionManager")
    public DataSourceTransactionManager slaveCjFrontTransactionManager(
            @Qualifier("slaveCjFrontDataSource") DataSource slaveCjFrontDataSource) {
        return new DataSourceTransactionManager(slaveCjFrontDataSource);
    }
}

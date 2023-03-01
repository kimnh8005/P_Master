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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(
        annotationClass = Mapper.class
        , sqlSessionFactoryRef = "masterSqlSessionFactory"
        , basePackages = {
                "kr.co.pulmuone.v1.comm.mapper"
        }
)
public class MasterDatabaseConfig {

    @Value("${spring.datasource.hikari.master.mybatis.config-location}")
    private String mybatisConfigLocation;

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource masterDataScource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);

        Resource[] arrResource = resolver.getResources("classpath*:/persistence/**/mapper/**/*.xml");
        sqlSessionFactoryBean.setMapperLocations(arrResource);
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(mybatisConfigLocation));
        sqlSessionFactoryBean.setTypeHandlersPackage("kr.co.pulmuone.v1.comm.base.mybatis.hanlder");

        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
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
        sqlSessionFactoryRef = "slavePovSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.v1.comm.mappers.slavePov"
        }
)
public class SlavePovDatabaseConfig {
    @Value("${spring.datasource.hikari.slave-pov.mybatis.config-location}")
    private String mybatisConfigLocation;

    @Bean(name = "slavePovDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-pov")
    public DataSource slavePovDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slavePovSqlSessionFactory")
    public SqlSessionFactory slavePovSqlSessionFactory(@Qualifier("slavePovDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);

        Resource[] arrResource = resolver.getResources("classpath*:/persistence/mappers/slavePov/**/*.xml");
        sqlSessionFactoryBean.setMapperLocations(arrResource);
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(mybatisConfigLocation));
        sqlSessionFactoryBean.setTypeHandlersPackage("kr.co.pulmuone.v1.comm.base.mybatis.hanlder");

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slavePovSqlSessionTemplate")
    public SqlSessionTemplate slavePovSqlSessionTemplate(
            @Qualifier("slavePovSqlSessionFactory") SqlSessionFactory slavePovSqlSessionFactory) {
        return new SqlSessionTemplate(slavePovSqlSessionFactory);
    }

    @Bean(name = "slavePovTransactionManager")
    public DataSourceTransactionManager slavePovTransactionManager(
            @Qualifier("slavePovDataSource") DataSource slavePovDataSource) {
        return new DataSourceTransactionManager(slavePovDataSource);
    }
}

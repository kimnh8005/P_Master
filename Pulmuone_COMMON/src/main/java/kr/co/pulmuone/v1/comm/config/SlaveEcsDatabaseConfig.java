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
        sqlSessionFactoryRef = "slaveEcsSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.v1.comm.mappers.slaveEcs"
        }
)
public class SlaveEcsDatabaseConfig {
    @Value("${spring.datasource.hikari.slave-ecs.mybatis.config-location}")
    private String mybatisConfigLocation;

    @Bean(name = "slaveEcsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-ecs")
    public DataSource slaveEcsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveEcsSqlSessionFactory")
    public SqlSessionFactory slaveEcsSqlSessionFactory(@Qualifier("slaveEcsDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);

        Resource[] arrResource = resolver.getResources("classpath*:/persistence/mappers/slaveEcs/**/*.xml");
        sqlSessionFactoryBean.setMapperLocations(arrResource);
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(mybatisConfigLocation));
        sqlSessionFactoryBean.setTypeHandlersPackage("kr.co.pulmuone.v1.comm.base.mybatis.hanlder");

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slaveEcsSqlSessionTemplate")
    public SqlSessionTemplate slaveEcsSqlSessionTemplate(
            @Qualifier("slaveEcsSqlSessionFactory") SqlSessionFactory slaveEcsSqlSessionFactory) {
        return new SqlSessionTemplate(slaveEcsSqlSessionFactory);
    }

    @Bean(name = "slaveEcsTransactionManager")
    public DataSourceTransactionManager slaveEcsTransactionManager(
            @Qualifier("slaveEcsDataSource") DataSource slaveEcsDataSource) {
        return new DataSourceTransactionManager(slaveEcsDataSource);
    }
}

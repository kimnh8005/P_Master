package kr.co.pulmuone.batch.erp.common.config.database;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "masterSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.batch.erp.infra.mapper.system"
                , "kr.co.pulmuone.batch.erp.infra.mapper.send"
                , "kr.co.pulmuone.batch.erp.infra.mapper.sample.master"
                , "kr.co.pulmuone.batch.erp.infra.mapper.user.master"
        }
)
public class MasterDatabaseConfig {

    @Autowired
    private MasterDatabaseProperty masterDatabaseProperty;

    @Primary
    @Bean(name = "masterDatasource")
    public DataSource masterDataSource() {
        return new LazyConnectionDataSourceProxy(
                new HikariDataSource(masterDatabaseProperty.getHikariConfig())
        );
    }

    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config-master.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory);
    }

    @Bean(name = "masterSqlSessionTemplateBatch")
    public SqlSessionTemplate masterSqlSessionTemplateBatch(@Qualifier("masterSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory, ExecutorType.BATCH);
    }

    @Bean(name = "masterTxManager")
    public PlatformTransactionManager masterTxManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }
}
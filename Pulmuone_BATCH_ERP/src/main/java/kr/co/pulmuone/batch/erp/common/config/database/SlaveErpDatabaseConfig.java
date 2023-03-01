package kr.co.pulmuone.batch.erp.common.config.database;

import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "slaveErpSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.batch.erp.infra.mapper.user.slaveErp"
        }
)
public class SlaveErpDatabaseConfig {

    @Autowired
    private SlaveErpDatabaseProperty slaveErpDatabaseProperty;

    @Bean(name = "slaveErpDatasource")
    public DataSource slaveErpDataSource() {
        return new LazyConnectionDataSourceProxy(
                new HikariDataSource(slaveErpDatabaseProperty.getHikariConfig())
        );
    }

    @Bean(name = "slaveErpSqlSessionFactory")
    public SqlSessionFactory slaveErpSqlSessionFactory(@Qualifier("slaveErpDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config-slave-erp.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slaveErpSqlSessionTemplate")
    public SqlSessionTemplate slaveErpSqlSessionTemplate(@Qualifier("slaveErpSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory, ExecutorType.BATCH);
    }

    @Bean(name = "slaveErpTxManager")
    public PlatformTransactionManager slaveErpTxManager() {
        return new DataSourceTransactionManager(slaveErpDataSource());
    }
}
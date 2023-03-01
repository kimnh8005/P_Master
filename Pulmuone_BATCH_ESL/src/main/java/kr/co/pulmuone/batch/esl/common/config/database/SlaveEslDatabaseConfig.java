package kr.co.pulmuone.batch.esl.common.config.database;

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
        sqlSessionFactoryRef = "slaveEslSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.batch.esl.infra.mapper.order.slaveEsl"
        }
)
public class SlaveEslDatabaseConfig {

    @Autowired
    private SlaveEslDatabaseProperty slaveEslDatabaseProperty;

    @Bean(name = "slaveEslDatasource")
    public DataSource slaveEslDataSource() {
        return new LazyConnectionDataSourceProxy(
                new HikariDataSource(slaveEslDatabaseProperty.getHikariConfig())
        );
    }

    @Bean(name = "slaveEslSqlSessionFactory")
    public SqlSessionFactory slaveEslSqlSessionFactory(@Qualifier("slaveEslDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config-slave-esl.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slaveEslSqlSessionTemplate")
    public SqlSessionTemplate slaveEslSqlSessionTemplate(@Qualifier("slaveEslSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory, ExecutorType.BATCH);
    }

    @Bean(name = "slaveEslTxManager")
    public PlatformTransactionManager slaveEslTxManager() {
        return new DataSourceTransactionManager(slaveEslDataSource());
    }
}
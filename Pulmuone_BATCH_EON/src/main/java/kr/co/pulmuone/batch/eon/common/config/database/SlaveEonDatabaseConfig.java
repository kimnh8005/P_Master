package kr.co.pulmuone.batch.eon.common.config.database;

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
        sqlSessionFactoryRef = "slaveEonSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.batch.eon.infra.mapper.email.slaveEon"
        }
)
public class SlaveEonDatabaseConfig {

    @Autowired
    private SlaveEonDatabaseProperty slaveEonDatabaseProperty;

    @Bean(name = "slaveEonDatasource")
    public DataSource slaveEonDataSource() {
        return new LazyConnectionDataSourceProxy(
                new HikariDataSource(slaveEonDatabaseProperty.getHikariConfig())
        );
    }

    @Bean(name = "slaveEonSqlSessionFactory")
    public SqlSessionFactory slaveEonSqlSessionFactory(@Qualifier("slaveEonDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config-slave-eon.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slaveEonSqlSessionTemplate")
    public SqlSessionTemplate slaveEonSqlSessionTemplate(@Qualifier("slaveEonSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory, ExecutorType.BATCH);
    }

    @Bean(name = "slaveEonTxManager")
    public PlatformTransactionManager slaveEonTxManager() {
        return new DataSourceTransactionManager(slaveEonDataSource());
    }
}
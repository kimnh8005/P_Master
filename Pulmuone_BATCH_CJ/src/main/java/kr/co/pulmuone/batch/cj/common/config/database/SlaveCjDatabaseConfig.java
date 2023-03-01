package kr.co.pulmuone.batch.cj.common.config.database;

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
        sqlSessionFactoryRef = "slaveCjSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.batch.cj.infra.mapper"
                , "kr.co.pulmuone.batch.cj.infra.mapper.claim.slaveCj"
                /* Batch CJ Default Setting */
        }
)
public class SlaveCjDatabaseConfig {

    @Autowired
    private SlaveCjDatabaseProperty slaveCnDatabaseProperty;

    @Bean(name = "slaveCjDatasource")
    public DataSource slaveCjDataSource() {
        return new LazyConnectionDataSourceProxy(
                new HikariDataSource(slaveCnDatabaseProperty.getHikariConfig())
        );
    }

    @Bean(name = "slaveCjSqlSessionFactory")
    public SqlSessionFactory slaveCjSqlSessionFactory(@Qualifier("slaveCjDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config-slave-cj.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "slaveCjSqlSessionTemplate")
    public SqlSessionTemplate slaveCjSqlSessionTemplate(@Qualifier("slaveCjSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory, ExecutorType.BATCH);
    }

    @Bean(name = "slaveCjTxManager")
    public PlatformTransactionManager slaveCjTxManager() {
        return new DataSourceTransactionManager(slaveCjDataSource());
    }
}
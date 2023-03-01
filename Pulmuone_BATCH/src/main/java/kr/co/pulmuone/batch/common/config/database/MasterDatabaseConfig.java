package kr.co.pulmuone.batch.common.config.database;

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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        annotationClass = Mapper.class,
        sqlSessionFactoryRef = "masterSqlSessionFactory",
        basePackages = {
                "kr.co.pulmuone.batch.infra.mapper.system"
                , "kr.co.pulmuone.batch.infra.mapper.sample.master"
                , "kr.co.pulmuone.batch.infra.mapper.send"
                , "kr.co.pulmuone.v1.comm.mappers.batch.master"
                // service
                , "kr.co.pulmuone.v1.comm.mapper.api"
                , "kr.co.pulmuone.v1.comm.mapper.approval.auth"
                , "kr.co.pulmuone.v1.comm.mapper.base"
                , "kr.co.pulmuone.v1.comm.mapper.comm.api"
                , "kr.co.pulmuone.v1.comm.mapper.display"
                , "kr.co.pulmuone.v1.comm.mapper.goods.discount"
                , "kr.co.pulmuone.v1.comm.mapper.goods.goods"
                , "kr.co.pulmuone.v1.comm.mapper.goods.item"
                , "kr.co.pulmuone.v1.comm.mapper.goods.itemprice"
                , "kr.co.pulmuone.v1.comm.mapper.goods.price"
                , "kr.co.pulmuone.v1.comm.mapper.goods.search"
                , "kr.co.pulmuone.v1.comm.mapper.goods.stock"
                , "kr.co.pulmuone.v1.comm.mapper.order.claim"
                , "kr.co.pulmuone.v1.comm.mapper.order.delivery"
                , "kr.co.pulmuone.v1.comm.mapper.order.front"
                , "kr.co.pulmuone.v1.comm.mapper.order.order"
                , "kr.co.pulmuone.v1.comm.mapper.order.registration"
                , "kr.co.pulmuone.v1.comm.mapper.order.regular"
                , "kr.co.pulmuone.v1.comm.mapper.order.shipping"
                , "kr.co.pulmuone.v1.comm.mapper.order.status"
                , "kr.co.pulmuone.v1.comm.mapper.order.present"
                , "kr.co.pulmuone.v1.comm.mapper.pg"
                , "kr.co.pulmuone.v1.comm.mapper.policy.benefit"
                , "kr.co.pulmuone.v1.comm.mapper.policy.config"
                , "kr.co.pulmuone.v1.comm.mapper.policy.excel"
                , "kr.co.pulmuone.v1.comm.mapper.policy.holiday"
                , "kr.co.pulmuone.v1.comm.mapper.policy.payment"
                , "kr.co.pulmuone.v1.comm.mapper.policy.clause"
                , "kr.co.pulmuone.v1.comm.mapper.promotion.coupon"
                , "kr.co.pulmuone.v1.comm.mapper.promotion.exhibit"
                , "kr.co.pulmuone.v1.comm.mapper.promotion.point"
                , "kr.co.pulmuone.v1.comm.mapper.promotion.serialnumber"
                , "kr.co.pulmuone.v1.comm.mapper.promotion.advertising"
                , "kr.co.pulmuone.v1.comm.mapper.search.index"
                , "kr.co.pulmuone.v1.comm.mapper.send.template"
                , "kr.co.pulmuone.v1.comm.mapper.shopping.cart"
                , "kr.co.pulmuone.v1.comm.mapper.shopping.favorites"
                , "kr.co.pulmuone.v1.comm.mapper.shopping.recently"
                , "kr.co.pulmuone.v1.comm.mapper.store.delivery"
                , "kr.co.pulmuone.v1.comm.mapper.store.warehouse"
                , "kr.co.pulmuone.v1.comm.mapper.system.basic"
                , "kr.co.pulmuone.v1.comm.mapper.system.code"
                , "kr.co.pulmuone.v1.comm.mapper.system.log"
                , "kr.co.pulmuone.v1.comm.mapper.system.monitoring"
                , "kr.co.pulmuone.v1.comm.mapper.user.buyer"
                , "kr.co.pulmuone.v1.comm.mapper.user.certification"
                , "kr.co.pulmuone.v1.comm.mapper.user.device"
                , "kr.co.pulmuone.v1.comm.mapper.user.dormancy"
                , "kr.co.pulmuone.v1.comm.mapper.user.environment"
                , "kr.co.pulmuone.v1.comm.mapper.user.group"
                , "kr.co.pulmuone.v1.comm.mapper.user.join"
                , "kr.co.pulmuone.v1.comm.mapper.user.login"
                , "kr.co.pulmuone.v1.comm.mapper.user.noti"
                , "kr.co.pulmuone.v1.comm.mappers.slaveCjFront"
                , "kr.co.pulmuone.v1.comm.mapper.outmall.ezadmin"
                , "kr.co.pulmuone.v1.comm.mapper.order.schedule"
                , "kr.co.pulmuone.v1.comm.mapper.outmall.order"
                , "kr.co.pulmuone.v1.comm.mapper.order.email"
                , "kr.co.pulmuone.v1.comm.mapper.policy.claim"
                , "kr.co.pulmuone.v1.comm.mapper.order.create"
                , "kr.co.pulmuone.v1.comm.mapper.order.email.OrderEmailMapper"
                , "kr.co.pulmuone.v1.comm.mapper.order.ifday"
                , "kr.co.pulmuone.v1.comm.mapper.system.code"
                , "kr.co.pulmuone.v1.comm.mapper.policy.shiparea"
                , "kr.co.pulmuone.v1.comm.mapper.user.store"
                , "kr.co.pulmuone.v1.comm.mapper.user.company"
                , "kr.co.pulmuone.v1.comm.mapper.outmall.sellers"
                , "kr.co.pulmuone.v1.comm.mapper.policy.fee"
                , "kr.co.pulmuone.v1.comm.mapper.shopping.restock"
                , "kr.co.pulmuone.v1.comm.mapper.calculate.employee"
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
        sqlSessionFactoryBean.setTypeHandlersPackage("kr.co.pulmuone.v1.comm.base.mybatis.hanlder");
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
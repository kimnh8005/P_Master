package kr.co.pulmuone.batch.esl.common.config.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChainedTxConfig {
    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("masterTxManager") PlatformTransactionManager masterTxManager
            , @Qualifier("slaveEslTxManager") PlatformTransactionManager slaveEslTxManager
        ) {
        return new ChainedTransactionManager(masterTxManager, slaveEslTxManager);
    }
}

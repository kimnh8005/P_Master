package kr.co.pulmuone.batch.cj.common.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(this.password); //암호화에 사용할 키 -> 중요
        config.setAlgorithm("PBEWithMD5AndDES"); //사용할 알고리즘
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.salt.NoOpIVGenerator");
        config.setStringOutputType("base64");

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }
}

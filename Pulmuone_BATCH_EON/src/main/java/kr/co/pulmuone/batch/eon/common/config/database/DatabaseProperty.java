package kr.co.pulmuone.batch.eon.common.config.database;


import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DatabaseProperty {

    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private Long maxLifetime;
    private Long connectionTimeout;

    protected HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.jdbcUrl);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setDriverClassName(this.driverClassName);
        config.setMaxLifetime(this.maxLifetime);
        config.setConnectionTimeout(this.connectionTimeout);
        return config;
    }
}

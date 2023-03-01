package kr.co.pulmuone.batch.eon.common.config.database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString(callSuper = true)
@Component
@ConfigurationProperties("spring.datasource.hikari.slave-eon")
public class SlaveEonDatabaseProperty extends DatabaseProperty {

}

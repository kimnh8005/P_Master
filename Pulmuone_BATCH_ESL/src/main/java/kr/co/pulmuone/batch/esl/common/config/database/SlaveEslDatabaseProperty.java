package kr.co.pulmuone.batch.esl.common.config.database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString(callSuper = true)
@Component
@ConfigurationProperties("spring.datasource.hikari.slave-esl")
public class SlaveEslDatabaseProperty extends DatabaseProperty {

}

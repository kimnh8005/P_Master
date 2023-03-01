package kr.co.pulmuone.v1.comm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
public class ContextSessionCommon {
    /*
     * 개발 환경에서 Redis 구동시 ERR unknown command 'CONFIG' redis-server 에러 방지 처리
     *
     */
    @Bean
    public ConfigureRedisAction configureRedisActionCommon() {
        return ConfigureRedisAction.NO_OP;
    }

}

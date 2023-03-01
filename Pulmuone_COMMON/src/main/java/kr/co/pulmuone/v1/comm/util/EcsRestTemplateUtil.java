package kr.co.pulmuone.v1.comm.util;

import kr.co.pulmuone.v1.comm.api.factory.EcsRestTemplateFactory;
import kr.co.pulmuone.v1.comm.api.factory.RestTemplateFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class EcsRestTemplateUtil implements InitializingBean{

    @Autowired
    private EcsRestTemplateFactory ecsRestTemplateFactory;
	private RestTemplate ecsRestTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
		this.ecsRestTemplate = ecsRestTemplateFactory.getEcsRestTemplate();
    }

	public ResponseEntity<String> getEcs(String url, HttpEntity<?> entity) {
		return ecsRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	}
}
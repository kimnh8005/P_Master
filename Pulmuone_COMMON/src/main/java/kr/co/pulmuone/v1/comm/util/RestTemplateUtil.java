package kr.co.pulmuone.v1.comm.util;

import kr.co.pulmuone.v1.comm.api.factory.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class RestTemplateUtil {

    @Autowired
    private RestTemplateFactory restTemplateFactory;

	public ResponseEntity<String> get(String url) {
		return restTemplateFactory.getRestTemplate().getForEntity(url, String.class);
	}

	public ResponseEntity<String> get(UriComponentsBuilder builder) {
		return restTemplateFactory.getRestTemplate().getForEntity(builder.build().toUri(), String.class);
	}

	public ResponseEntity<String> get(UriComponentsBuilder builder, HttpEntity<?> entity) {
		return restTemplateFactory.getRestTemplate().exchange(builder.build().toUri(), HttpMethod.GET, entity, String.class);
	}

	public ResponseEntity<String> get(String url, HttpEntity<?> entity) {
		return restTemplateFactory.getRestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
	}

	public <T> ResponseEntity<T> get(UriComponentsBuilder builder, HttpEntity<?> entity, Class<T> response) {
		return restTemplateFactory.getRestTemplate().exchange(builder.build().toUri(), HttpMethod.GET, entity, response);
	}

	public <T> ResponseEntity<T> post(String url, HttpEntity<?> entity, Class<T> response) {
		return restTemplateFactory.getRestTemplate().exchange(url, HttpMethod.POST, entity, response);
	}

	public <T> ResponseEntity<T> post(UriComponentsBuilder builder, HttpEntity<?> entity, Class<T> response) {
		return restTemplateFactory.getRestTemplate().exchange(builder.build().toUri(), HttpMethod.POST, entity, response);
	}

	public <T> ResponseEntity<T> post(URI uri, HttpEntity<?> entity, Class<T> response) {
		return restTemplateFactory.getRestTemplate().exchange(uri, HttpMethod.POST, entity, response);
	}

	public <T> ResponseEntity<T> post(String uri, Object request, Class<T> response) {
		return restTemplateFactory.getRestTemplate().postForEntity(uri, request, response);
	}

}
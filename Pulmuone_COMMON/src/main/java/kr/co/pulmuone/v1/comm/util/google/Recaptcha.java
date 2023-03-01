package kr.co.pulmuone.v1.comm.util.google;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Component
public class Recaptcha extends RestTemplateUtil {

	@Value("${google.recaptcha.secret}")
	private String SECRET;

	public static final HttpHeaders HTTP_HEADER;
	static {
		HttpHeaders headers = new HttpHeaders();
		HTTP_HEADER = headers;
	}

	public boolean siteVerify(String token) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("secret", SECRET);
		map.add("response", token);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map,
				HTTP_HEADER);
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = this.post("https://www.google.com/recaptcha/api/siteverify", entity, String.class);
		} catch (Exception e) {
			log.error("=========Recaptcha siteVerify api===={}", e.getMessage());
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonode = null;
		try {
			log.info("=========Recaptcha checkUser api response===={}", responseEntity.getBody());
			jsonode = mapper.readTree(responseEntity.getBody());
		} catch (Exception e) {
			log.error("=========Recaptcha checkUser josn===={}", e.getMessage());
			e.printStackTrace();
		}

		if (jsonode.findValue("success").getValueAsText().equals("true")) {
			return true;
		} else {
			return false;
		}

	}
}

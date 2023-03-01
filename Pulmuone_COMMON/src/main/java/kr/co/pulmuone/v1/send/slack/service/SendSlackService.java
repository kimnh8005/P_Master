package kr.co.pulmuone.v1.send.slack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import kr.co.pulmuone.v1.send.slack.dto.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendSlackService {

    @Value("${slack.webhook-url}")
    private String webHookUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${slack.excludes}")
    private String[] excludeServerTypes;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    protected void notify(SlackMessage slack) {
        try {
            if (isExcludeServerType()) {
                return;
            }
            slack.setActiveProfile(activeProfile);
            restTemplateUtil.post(webHookUrl, slack, String.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isExcludeServerType() {
        return Arrays.asList(excludeServerTypes).contains(activeProfile);
    }

}

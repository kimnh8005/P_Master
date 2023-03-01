package kr.co.pulmuone.batch.eon.domain.service.system;

import kr.co.pulmuone.batch.eon.domain.model.system.SlackMessage;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService {

    @Value("${slack.webhook-url}")
    private String webHookUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${slack.excludes}")
    private String[] excludeServerTypes;

    @Value("${slack.channel}")
    private String defaultSlackChannel;

    private final RestTemplate restTemplate;

    @Override
    public void notify(SlackMessage slack) {
        try {
            if (isExcludeServerType()) {
                return;
            }
            slack.setActiveProfile(activeProfile);
            log.info("slack message: {}", slack.toString());
            restTemplate.postForEntity(webHookUrl, slack, String.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void notify(String message) {
        notify(new SlackMessage(defaultSlackChannel, message));
    }

    @Override
    public void notify(String channel, String message) {
        notify(new SlackMessage(channel, message));
    }

    private boolean isExcludeServerType() {
        return Arrays.asList(excludeServerTypes).contains(activeProfile);
    }

}

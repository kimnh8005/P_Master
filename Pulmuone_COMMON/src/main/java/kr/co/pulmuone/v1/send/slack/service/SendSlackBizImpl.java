package kr.co.pulmuone.v1.send.slack.service;

import kr.co.pulmuone.v1.send.slack.dto.SlackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSlackBizImpl implements SendSlackBiz {

    private final SendSlackService sendSlackService;

    @Value("${slack.channel}")
    private String defaultSlackChannel;

    @Override
    public void notify(SlackMessage slackMessage) {
        sendSlackService.notify(slackMessage);
    }

    @Override
    public void notify(String message) {
        sendSlackService.notify(new SlackMessage(defaultSlackChannel, message));
    }

    @Override
    public void notify(String channel, String message) {
        sendSlackService.notify(new SlackMessage(channel, message));
    }

}

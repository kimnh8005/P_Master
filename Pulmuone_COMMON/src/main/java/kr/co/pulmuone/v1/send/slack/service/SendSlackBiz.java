package kr.co.pulmuone.v1.send.slack.service;


import kr.co.pulmuone.v1.send.slack.dto.SlackMessage;

public interface SendSlackBiz {

    void notify(SlackMessage slack);

    void notify(String message);

    void notify(String channel, String message);

}

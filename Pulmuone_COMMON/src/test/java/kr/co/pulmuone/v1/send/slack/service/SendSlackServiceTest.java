package kr.co.pulmuone.v1.send.slack.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.send.slack.dto.SlackMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SendSlackServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SendSlackService sendSlackService;

    @Test
    void testNotify() {
        //given
        SlackMessage slackMessage = new SlackMessage("hangaram_forbiz_local_test", "test case");

        //when, then
        sendSlackService.notify(slackMessage);
    }

}
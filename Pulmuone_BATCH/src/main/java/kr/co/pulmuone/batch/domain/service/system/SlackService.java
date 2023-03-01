package kr.co.pulmuone.batch.domain.service.system;

import kr.co.pulmuone.batch.domain.model.system.SlackMessage;

public interface SlackService {

    void notify(SlackMessage slack);

    void notify(String message);

    void notify(String channel, String message);

}

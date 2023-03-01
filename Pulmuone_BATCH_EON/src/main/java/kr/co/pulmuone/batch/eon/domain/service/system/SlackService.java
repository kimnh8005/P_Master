package kr.co.pulmuone.batch.eon.domain.service.system;

import kr.co.pulmuone.batch.eon.domain.model.system.SlackMessage;

public interface SlackService {

    void notify(SlackMessage slack);

    void notify(String message);

    void notify(String channel, String message);

}

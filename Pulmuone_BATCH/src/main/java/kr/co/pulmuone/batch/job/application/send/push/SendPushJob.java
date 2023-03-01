package kr.co.pulmuone.batch.job.application.send.push;

import com.google.firebase.messaging.FirebaseMessagingException;
import kr.co.pulmuone.v1.batch.send.push.SendPushBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("sendPushJob")
@Slf4j
public class SendPushJob implements BaseJob {

    @Autowired
    private SendPushBatchBiz sendPushBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        try {
            sendPushBatchBiz.runSendPush();
        } catch (FirebaseMessagingException | InterruptedException | IOException e) {
            throw new BaseException(e.getMessage());
        }
    }
}

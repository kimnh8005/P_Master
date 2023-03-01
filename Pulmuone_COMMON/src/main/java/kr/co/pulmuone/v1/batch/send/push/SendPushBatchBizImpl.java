package kr.co.pulmuone.v1.batch.send.push;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
public class SendPushBatchBizImpl implements SendPushBatchBiz {

    @Autowired
    private SendPushBatchService sendPushBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runSendPush() throws FirebaseMessagingException, InterruptedException, IOException {
        sendPushBatchService.runSendPush();
    }

}

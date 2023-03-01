package kr.co.pulmuone.v1.batch.send.push;

import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;

public interface SendPushBatchBiz {

    void runSendPush() throws FirebaseMessagingException, InterruptedException, IOException;

}

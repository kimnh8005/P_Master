package kr.co.pulmuone.batch.job.application.user.join;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.user.join.UserJoinBatchBiz;
import kr.co.pulmuone.v1.batch.user.noti.UserNotiBatchBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userJoinDepositPointJob")
@Slf4j
@RequiredArgsConstructor
public class UserJoinDepositPointJob implements BaseJob {
    //BATCH NO : 93

    @Autowired
    private UserJoinBatchBiz userJoinBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        userJoinBatchBiz.runUserJoinDepositPoint();
    }

}

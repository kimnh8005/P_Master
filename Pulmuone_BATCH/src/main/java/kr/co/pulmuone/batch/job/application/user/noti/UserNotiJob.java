package kr.co.pulmuone.batch.job.application.user.noti;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.user.dormancy.UserDormancyBatchBiz;
import kr.co.pulmuone.v1.batch.user.noti.UserNotiBatchBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userNotiJob")
@Slf4j
@RequiredArgsConstructor
public class UserNotiJob implements BaseJob {
    //BATCH NO : 92

    @Autowired
    private UserNotiBatchBiz userNotiBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        userNotiBatchBiz.runUserNoti();
    }

}

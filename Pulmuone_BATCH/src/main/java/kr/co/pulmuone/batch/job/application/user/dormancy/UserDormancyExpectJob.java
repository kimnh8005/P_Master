package kr.co.pulmuone.batch.job.application.user.dormancy;

import kr.co.pulmuone.v1.batch.user.dormancy.UserDormancyBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userDormancyExpectJob")
@Slf4j
@RequiredArgsConstructor
public class UserDormancyExpectJob implements BaseJob {

    @Autowired
    private UserDormancyBatchBiz userDormancyBatchBiz;

    @Override
    public void run(String[] params) {
        userDormancyBatchBiz.runUserDormancyExpect();
    }
}

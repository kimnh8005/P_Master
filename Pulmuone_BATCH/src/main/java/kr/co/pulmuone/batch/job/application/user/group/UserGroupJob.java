package kr.co.pulmuone.batch.job.application.user.group;

import kr.co.pulmuone.v1.batch.user.group.UserGroupBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userGroupJob")
@Slf4j
public class UserGroupJob implements BaseJob {

    @Autowired
    private UserGroupBatchBiz userGroupBatchBiz;

    @Override
    public void run(String[] params) {
    	userGroupBatchBiz.runUserGroupSetUp();
    }
}

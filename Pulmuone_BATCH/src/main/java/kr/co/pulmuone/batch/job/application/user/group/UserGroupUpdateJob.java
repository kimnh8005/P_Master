package kr.co.pulmuone.batch.job.application.user.group;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.user.group.UserGroupBatchBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userGroupUpdateJob")
@Slf4j
public class UserGroupUpdateJob implements BaseJob {
    //Batch 번호 : 35

    @Autowired
    private UserGroupBatchBiz userGroupBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
    	userGroupBatchBiz.runUserGroupUpdate();
    }
}

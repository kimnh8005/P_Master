package kr.co.pulmuone.v1.batch.user.join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserJoinBatchBizImpl implements UserJoinBatchBiz {

    @Autowired
    private UserJoinBatchService userJoinBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runUserJoinDepositPoint() throws Exception {
        userJoinBatchService.runUserJoinDepositPoint();
    }

}
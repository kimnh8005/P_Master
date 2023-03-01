package kr.co.pulmuone.v1.batch.user.dormancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDormancyBatchBizImpl implements UserDormancyBatchBiz {

    @Autowired
    private UserDormancyBatchService userDormancyBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runUserDormancy() {
        userDormancyBatchService.runUserDormancy();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runUserDormancyExpect() {
        userDormancyBatchService.runUserDormancyExpect();
    }
}

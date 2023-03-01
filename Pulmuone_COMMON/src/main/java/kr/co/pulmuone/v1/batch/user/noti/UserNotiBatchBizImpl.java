package kr.co.pulmuone.v1.batch.user.noti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserNotiBatchBizImpl implements UserNotiBatchBiz {

    @Autowired
    private UserNotiBatchService userNotiBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runUserNoti() throws Exception {
        userNotiBatchService.runUserNoti();
    }

}
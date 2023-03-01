package kr.co.pulmuone.v1.batch.user.group;

import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupBenefitPointVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserGroupBatchBizImpl implements UserGroupBatchBiz {

    @Autowired
    private UserGroupBatchService userGroupBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runUserGroupSetUp() {
        userGroupBatchService.runUserGroupSetUp();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runUserGroupUpdate() throws Exception {
        userGroupBatchService.runUserGroupUpdate();
    }

    @Override
    public List<UserGroupBenefitPointVo> getUserGroupBenefitPoint() {
        return userGroupBatchService.getUserGroupBenefitPoint();
    }

}

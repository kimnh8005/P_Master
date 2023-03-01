package kr.co.pulmuone.v1.batch.user.group;

import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupBenefitPointVo;

import java.util.List;

public interface UserGroupBatchBiz {

    void runUserGroupSetUp();

    void runUserGroupUpdate() throws Exception;

    List<UserGroupBenefitPointVo> getUserGroupBenefitPoint();

}

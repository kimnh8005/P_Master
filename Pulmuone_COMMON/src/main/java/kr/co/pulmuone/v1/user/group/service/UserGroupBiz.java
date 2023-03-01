package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.user.group.dto.GroupInfoByUserResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupInfoByUserResultVo;

public interface UserGroupBiz {

    GroupInfoByUserResponseDto getGroupInfoByUser(Long urUserId) throws Exception;

    GroupInfoByUserResultVo getGroupByUser(Long urUserId) throws Exception;

    Long getDefaultGroup() throws Exception;

    String getGroupNameByMeta() throws Exception;

}

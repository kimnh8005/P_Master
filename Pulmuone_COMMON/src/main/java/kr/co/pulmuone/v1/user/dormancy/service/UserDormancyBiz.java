package kr.co.pulmuone.v1.user.dormancy.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.dormancy.dto.CommonPutUserDormantRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.UserDormancyResultVo;

public interface UserDormancyBiz {

    void putUserDormant(CommonPutUserDormantRequestDto dto) throws Exception;

    ApiResult<?> putUserActive(String password) throws Exception;

    GetIsCheckUserMoveResultVo isCheckUserMove(String urUserId) throws Exception;

    GetUserDormantListResponseDto getUserDormantList(GetUserDormantListRequestDto dto) throws Exception;

    GetUserDormantHistoryListResponseDto getUserDormantHistoryList(GetUserDormantHistoryListRequestDto dto) throws Exception;

    UserDormancyResultVo getUserDormancyInfo(Long urUserId);

    UserDormancyResultVo getUserDormancyExpected(Long urUserId);

}

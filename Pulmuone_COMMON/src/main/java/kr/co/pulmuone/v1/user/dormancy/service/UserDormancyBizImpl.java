package kr.co.pulmuone.v1.user.dormancy.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.UserDormancyResultVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDormancyBizImpl implements UserDormancyBiz {

    @Autowired
    private UserDormancyService userDormancyService;

    public void putUserDormant(CommonPutUserDormantRequestDto dto) throws Exception {

        userDormancyService.putUserDormant(dto);
    }

    public ApiResult<?> putUserActive(String password) throws Exception {

        return userDormancyService.putUserActive(password);
    }

    public GetIsCheckUserMoveResultVo isCheckUserMove(String urUserId) throws Exception {

        return userDormancyService.isCheckUserMove(urUserId);
    }

    @Override
    public GetUserDormantListResponseDto getUserDormantList(GetUserDormantListRequestDto dto) throws Exception {
        return userDormancyService.getUserDormantList(dto);
    }

    @Override
    public GetUserDormantHistoryListResponseDto getUserDormantHistoryList(GetUserDormantHistoryListRequestDto dto) throws Exception {
        return userDormancyService.getUserDormantHistoryList(dto);
    }

	@Override
	public UserDormancyResultVo getUserDormancyInfo(Long urUserId) {
		return userDormancyService.getUserDormancyInfo(urUserId);
	}

	@Override
	public UserDormancyResultVo getUserDormancyExpected(Long urUserId) {
		return userDormancyService.getUserDormancyExpected(urUserId);
	}
}

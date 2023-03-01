package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.UserDropRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.UserDropResultVo;

public interface UserBuyerDropBiz {

	GetUserDropListResponseDto getUserDropList(GetUserDropListRequestDto dto) throws Exception;

	ApiResult<?> progressUserDrop(UserDropRequestDto dto) throws Exception;

	UserDropResultVo getUserDropInfo(Long urUserId) throws Exception;

	void getUserDropCompleted(UserDropResultVo userDropResultVo, String mobile);



}

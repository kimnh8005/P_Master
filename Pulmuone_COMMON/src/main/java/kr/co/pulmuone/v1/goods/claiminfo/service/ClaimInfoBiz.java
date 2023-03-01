package kr.co.pulmuone.v1.goods.claiminfo.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoRequestDto;

public interface ClaimInfoBiz {

	ApiResult<?> getClaimInfoList(ClaimInfoRequestDto claimInfoRequestDto);

	ApiResult<?> getClaimInfo(String ilClaimInfoId);

	ApiResult<?> putClaimInfo(ClaimInfoRequestDto claimInfoRequestDto) throws Exception;

	//IlClaimDescriptionInfomationResponseDto addIlClaimDescriptionInfomation(IlClaimDescriptionInfomationRequestDto ilClaimDescriptionInfomationRequestDto) throws Exception;

	//IlClaimDescriptionInfomationResponseDto delIlClaimDescriptionInfomation(String ilClaimInfoId) throws Exception;
}
package kr.co.pulmuone.bos.goods.claiminfo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoListResponseDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoRequestDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoResponseDto;
import kr.co.pulmuone.v1.goods.claiminfo.service.ClaimInfoBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 배송/반품/취소안내
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.10.22     이성준             리팩토링
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ClaimInfoController {

	private final ClaimInfoBiz claimInfoBiz;

	@ApiOperation(value = "배송/반품/취소안내 목록 조회")
	@PostMapping(value = "/admin/goods/claiminfo/getClaimInfoList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ClaimInfoListResponseDto.class)
	})
	public ApiResult<?> getClaimInfoList(HttpServletRequest request, ClaimInfoRequestDto claimInfoRequestDto) throws Exception {
		return claimInfoBiz.getClaimInfoList(BindUtil.bindDto(request, ClaimInfoRequestDto.class));
	}


	@ApiOperation(value = "배송/반품/취소안내 상세 조회")
	@PostMapping(value = "/admin/goods/claiminfo/getClaimInfo")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = ClaimInfoResponseDto.class)
	})
	public ApiResult<?> getClaimInfo(@RequestParam(value = "ilClaimInfoId", required = true) String ilClaimInfoId){
		return claimInfoBiz.getClaimInfo(ilClaimInfoId);
	}


	@ApiOperation(value = "배송/반품/취소안내 수정")
	@PostMapping(value = "/admin/goods/claiminfo/putClaimInfo")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "request data", response = ClaimInfoRequestDto.class)
    })
	public ApiResult<?> putClaimInfo(ClaimInfoRequestDto claimInfoRequestDto) {

        try {

			 return claimInfoBiz.putClaimInfo(claimInfoRequestDto);

		} catch (Exception e) {
			 log.error("ClaimInfoController.putClaimInfo : {}", e);
             return ApiResult.fail();
		}
	}

	/**
	 * 배송/반품/취소안내 추가
	 * @param	ilClaimDescriptionInfomationRequestDto
	 * @return	IlClaimDescriptionInfomationResponseDto
	 * @throws Exception
	 */
//	@PostMapping(value = "/admin/il/ilClaimDescriptionInfomation/addIlClaimDescriptionInfomation")
//	public IlClaimDescriptionInfomationResponseDto addIlClaimDescriptionInfomation(IlClaimDescriptionInfomationRequestDto ilClaimDescriptionInfomationRequestDto) throws Exception {
//
//		return ilClaimDescriptionInfomationService.addIlClaimDescriptionInfomation(ilClaimDescriptionInfomationRequestDto);
//	}

	/**
	 * 배송/반품/취소안내 삭제
	 * @param	ilClaimInfoId 배송/반품/취소안내 ID(PK)
	 * @return	IlClaimDescriptionInfomationResponseDto
	 * @throws Exception
	 */
//	@PostMapping(value = "/admin/il/ilClaimDescriptionInfomation/delIlClaimDescriptionInfomation")
//	public IlClaimDescriptionInfomationResponseDto delIlClaimDescriptionInfomation(@RequestParam(value = "ilClaimInfoId", required = true) String ilClaimInfoId) throws Exception {
//
//		return ilClaimDescriptionInfomationService.delIlClaimDescriptionInfomation(ilClaimInfoId);
//	}

}

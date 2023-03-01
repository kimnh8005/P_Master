package kr.co.pulmuone.mall.order.claim;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimCompleteProcessBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* <PRE>
* Forbiz Korea
* 주문클레임 관련 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0  2021. 03. 02.     강상국          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@RestController
@RequiredArgsConstructor
public class ClaimRestoreController {

	@Autowired
	private final ClaimCompleteProcessBiz claimCompleteProcessBiz;

	@PostMapping(value = "/order/claim/cancelRestore")
	@ApiOperation(value = "취소철회", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = BasicDataResponseDto.class),
			@ApiResponse(code = 901, message = ""
					+ "클레임 상태가 취소요청 상태가 아닌경우 \n"
					+ "클레임 상태가 이미 취소철회 상태인경우  \n"
					) })
	public ApiResult<?> cancelRestore(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		//if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			//return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		//}
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			orderClaimRegisterRequestDto.setUrUserId(buyerVo.getUrUserId());
		} else{
			orderClaimRegisterRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
		}

		// 취소요청
		orderClaimRegisterRequestDto.setSourceStatusCd(OrderEnums.OrderStatus.CANCEL_APPLY.getCode());
		// 취소철회
		orderClaimRegisterRequestDto.setTargetStatusCd(OrderEnums.OrderStatus.CANCEL_WITHDRAWAL.getCode());

		return claimCompleteProcessBiz.calimRestore(orderClaimRegisterRequestDto);
	}

	@PostMapping(value = "/order/claim/returnRestore")
	@ApiOperation(value = "반품철회", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = BasicDataResponseDto.class),
			@ApiResponse(code = 901, message = ""
					+ "클레임 상태가 취소요청 상태가 아닌경우 \n"
					+ "클레임 상태가 이미 취소철회 상태인경우  \n"
			) })
	public ApiResult<?> returnRestore(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			//return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		// 반품요청
		//String sourceStatusCd = OrderEnums.OrderStatus.RETURN_APPLY.getCode();
		// 반품철회
		//String targetStatusCd = OrderEnums.OrderStatus.RETURN_WITHDRAWAL.getCode();

		//return claimCompleteProcessBiz.calimRestore(StringUtil.nvlLong(buyerVo.getUrUserId()), odClaimId, sourceStatusCd, targetStatusCd, Long.parseLong(buyerVo.getUrUserId()), nonMember);

		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			orderClaimRegisterRequestDto.setUrUserId(buyerVo.getUrUserId());
		} else{
			orderClaimRegisterRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
		}
		// 반품요청
		orderClaimRegisterRequestDto.setSourceStatusCd(OrderEnums.OrderStatus.RETURN_APPLY.getCode());
		// 반품철회
		orderClaimRegisterRequestDto.setTargetStatusCd(OrderEnums.OrderStatus.RETURN_WITHDRAWAL.getCode());

		return claimCompleteProcessBiz.calimRestore(orderClaimRegisterRequestDto);
	}
}
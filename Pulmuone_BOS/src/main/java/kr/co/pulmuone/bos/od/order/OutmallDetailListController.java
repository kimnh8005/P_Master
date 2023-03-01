package kr.co.pulmuone.bos.od.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.od.order.OutmallOrderDetailListBiz;
import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20201116		   	      안지영          최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OutmallDetailListController {


	@Autowired
	OutmallOrderDetailListBiz  outmallOrderDetailListBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 휴면회원 이력리스트조회
	 * @param dto
	 * @return GetUserDormantHistoryListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/od/order/getUserDormantHistoryList")
	@ApiOperation(value = "탈퇴회원 목록 조회", httpMethod = "POST", notes = "휴면회원 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserDormantHistoryList(GetOutmallOrderDetailListRequestDto dto) throws Exception {

		return ApiResult.success(outmallOrderDetailListBiz.getOutmallOrderDetailList((GetOutmallOrderDetailListRequestDto) BindUtil.convertRequestToObject(request, GetOutmallOrderDetailListRequestDto.class)));
	}
}

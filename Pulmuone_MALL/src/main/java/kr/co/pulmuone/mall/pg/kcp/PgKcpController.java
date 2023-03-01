package kr.co.pulmuone.mall.pg.kcp;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpVirtualAccountReturnRequestDto;
import kr.co.pulmuone.mall.pg.kcp.service.KcpService;

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
 *  1.0    20200924   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */
@Controller
public class PgKcpController {

	@Autowired
	public KcpService kcpService;

	/**
	 * KCP 승인 요청
	 *
	 * @param KcpApprovalRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/kcp/approval")
	@ApiOperation(value = "KCP 승인 요청", httpMethod = "POST")
	public String approval(KcpApprovalRequestDto kcpApprovalRequestDto) throws Exception {
		return kcpService.approval(kcpApprovalRequestDto);
	}

	/**
	 * KCP 가상계좌 입금통보
	 *
	 * @param KcpVirtualAccountReturnRequestDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pg/kcp/virtualAccountReturn")
	@ApiOperation(value = "가상계좌 입금통보", httpMethod = "POST")
	public void virtualAccountReturn(HttpServletResponse httpServletResponse,
			KcpVirtualAccountReturnRequestDto virtualAccountReturnRequestDto) throws Exception {
		kcpService.virtualAccountReturn(httpServletResponse, virtualAccountReturnRequestDto);
	}

	/**
	 * KCP 정기결제 키 승인 요청
	 *
	 * @param kcpApplyRegularBatchKeyRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/kcp/applyRegularBatchKey")
	@ApiOperation(value = "KCP 정기결제 키 승인 요청", httpMethod = "POST")
	public void applyRegularBatchKey(HttpServletResponse httpServletResponse,
			KcpApplyRegularBatchKeyRequestDto kcpReturnApplyRegularBatchKeyRequestDto) throws Exception {
		kcpService.applyRegularBatchKey(httpServletResponse, kcpReturnApplyRegularBatchKeyRequestDto);
	}

	/**
	 * 추가결제 KCP 승인 요청
	 *
	 * @param KcpApprovalRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/kcp/addPayApproval")
	@ApiOperation(value = "KCP 승인 요청", httpMethod = "POST")
	public String addPayApproval(KcpApprovalRequestDto kcpApprovalRequestDto) throws Exception {
		return kcpService.addPayApproval(kcpApprovalRequestDto);
	}
}
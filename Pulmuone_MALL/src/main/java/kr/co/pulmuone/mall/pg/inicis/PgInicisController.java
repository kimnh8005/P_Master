package kr.co.pulmuone.mall.pg.inicis;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNotiRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisVirtualAccountReturnRequestDto;
import kr.co.pulmuone.mall.pg.inicis.service.InicisService;

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
public class PgInicisController {

	@Autowired
	public InicisService inicisService;

	/**
	 * 이니시스 레이아웃 닫기
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/pg/inicis/close")
	@ApiOperation(value = "이니시스 레이아웃 닫기", httpMethod = "GET")
	public void close(HttpServletResponse httpServletResponse) throws Exception {
		inicisService.close(httpServletResponse);
	}

	/**
	 * 이니시스 PC 승인 요청
	 *
	 * @param InicisApprovalRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/inicis/result")
	@ApiOperation(value = "이니시스 승인 요청", httpMethod = "POST")
	public String result(InicisApprovalRequestDto inicisApprovalRequestDto) throws Exception {
		return inicisService.result(inicisApprovalRequestDto);
	}

	/**
	 * 이니시스 모바일 인증결과수신 Post
	 *
	 * @param InicisMobileApprovalRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/inicis/next")
	@ApiOperation(value = "이니시스 모바일 인증결과수신", httpMethod = "POST")
	public String nextPost(InicisMobileApprovalRequestDto inicisMobileApprovalRequestDto) throws Exception {
		return inicisService.next(inicisMobileApprovalRequestDto);
	}

	/**
	 * 이니시스 모바일 인증결과수신 Get
	 *
	 * @param InicisMobileApprovalRequestDto
	 * @throws Exception
	 */
	@GetMapping(value = "/pg/inicis/next")
	@ApiOperation(value = "이니시스 모바일 인증결과수신", httpMethod = "GET")
	public String nextGet(InicisMobileApprovalRequestDto inicisMobileApprovalRequestDto) throws Exception {
		return inicisService.next(inicisMobileApprovalRequestDto);
	}

	/**
	 * 이니시스 가상계좌 입금통보
	 *
	 * @param InicisVirtualAccountReturnRequestDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pg/inicis/virtualAccountReturn")
	@ApiOperation(value = "이니시스 가상계좌 입금통보", httpMethod = "POST")
	public void virtualAccountReturn(HttpServletResponse httpServletResponse,
			InicisVirtualAccountReturnRequestDto inicisVirtualAccountReturnRequestDto) throws Exception {
		inicisService.virtualAccountReturn(httpServletResponse, inicisVirtualAccountReturnRequestDto);
	}

	/**
	 * 이니시스 모바일 noti
	 *
	 * @param InicisMobileApprovalRequestDto
	 * @throws Exception
	 */
	@GetMapping(value = "/pg/inicis/noti")
	@ApiOperation(value = "이니시스 모바일 noti", httpMethod = "GET")
	public void notiGet(HttpServletResponse httpServletResponse, InicisNotiRequestDto inicisNotiRequestDto) throws Exception {
		inicisService.noti(httpServletResponse, inicisNotiRequestDto);
	}

	@PostMapping(value = "/pg/inicis/noti")
	@ApiOperation(value = "이니시스 모바일 noti", httpMethod = "POST")
	public void notiPost(HttpServletResponse httpServletResponse, InicisNotiRequestDto inicisNotiRequestDto) throws Exception {
		inicisService.noti(httpServletResponse, inicisNotiRequestDto);
	}

	/**
	 * 추가결제 이니시스 PC 승인 요청
	 *
	 * @param InicisApprovalRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/inicis/addPayResult")
	@ApiOperation(value = "이니시스 승인 요청", httpMethod = "POST")
	public String addPayResult(InicisApprovalRequestDto inicisApprovalRequestDto) throws Exception {
		return inicisService.addPayResult(inicisApprovalRequestDto);
	}

	/**
	 * 추가결제 이니시스 모바일 인증결과수신 Post
	 *
	 * @param InicisMobileApprovalRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/pg/inicis/addPayNext")
	@ApiOperation(value = "이니시스 모바일 인증결과수신", httpMethod = "POST")
	public String addPayNextPost(InicisMobileApprovalRequestDto inicisMobileApprovalRequestDto) throws Exception {
		return inicisService.addPayNext(inicisMobileApprovalRequestDto);
	}

	/**
	 * 추가결제 이니시스 모바일 인증결과수신 Get
	 *
	 * @param InicisMobileApprovalRequestDto
	 * @throws Exception
	 */
	@GetMapping(value = "/pg/inicis/addPayNext")
	@ApiOperation(value = "이니시스 모바일 인증결과수신", httpMethod = "GET")
	public String addPayNextGet(InicisMobileApprovalRequestDto inicisMobileApprovalRequestDto) throws Exception {
		return inicisService.addPayNext(inicisMobileApprovalRequestDto);
	}
}
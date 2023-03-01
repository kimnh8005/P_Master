package kr.co.pulmuone.bos.user.member;

import io.swagger.annotations.*;
import kr.co.pulmuone.bos.user.member.service.BuyerBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeePastInfoByUserResponseDto;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBosBiz;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordClearRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

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
 *  1.0    20200625		   	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
public class BuyerController {

  @Autowired
  private UserBuyerBosBiz    userBuyerBosBiz;

  @Autowired
  private UserBuyerBiz       userBuyerBiz;

  @Autowired(required = true)
  private HttpServletRequest request;

  @Autowired
  private ExcelDownloadView  excelDownloadView; // 엑셀 다운로드 뷰

  @Autowired
  private BuyerBosService    buyerBosService;

  @Autowired
  private PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

  /**
   * 회원 리스트조회
   *
   * @param getBuyerListRequestDto
   * @return GetBuyerListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getBuyerList")
  @ApiOperation(value = "전체고객회원 조회", httpMethod = "POST", notes = "전체고객회원 조회")
  public ApiResult<?> getBuyerList(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {

    return ApiResult.success(buyerBosService.getBuyerList((GetBuyerListRequestDto) BindUtil.convertRequestToObject(request,
                                                                                                                   GetBuyerListRequestDto.class)));
  }

  /**
   * 회원 리스트조회 엑셀다운로드
   *
   * @param getBuyerListRequestDto
   * @return GetBuyerListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getBuyerListExportExcel")
  @ApiOperation(value = "전체고객회원 조희 엑셀다운로드", httpMethod = "POST", notes = "전체고객회원조희 엑셀다운로드")
  public ModelAndView getBuyerListExportExcel(@RequestBody GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel,
    		buyerBosService.getBuyerListExcelDownload(getBuyerListRequestDto));

    return modelAndView;
  }


  /**
   * 회원상세 - 쿠폰정보 리스트 조회
   *
   * @param getBuyerListRequestDto
   * @return GetBuyerListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getCouponList")
  @ApiOperation(value = "쿠폰정보 조회", httpMethod = "POST", notes = "쿠폰정보 조회")
  public ApiResult<?> getCouponList(GetCouponListRequestDto getCouponListRequestDto) throws Exception {

    return ApiResult.success(buyerBosService.getCouponList((GetCouponListRequestDto) BindUtil.convertRequestToObject(request,
                                                                                                                     GetCouponListRequestDto.class)));
  }

  /**
   * 회원 단일조회
   *
   * @param getBuyerRequestDto
   * @return GetBuyerResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getBuyer")
  @ApiOperation(value = "단일회원 조회", httpMethod = "POST", notes = "단일회원 조회")
  public ApiResult<?> getBuyer(GetBuyerRequestDto getBuyerRequestDto) throws Exception {

    UserVo userVo = SessionUtil.getBosUserVO();
    if(userVo != null) {
    	getBuyerRequestDto.setPersonalInformationAccessYn(userVo.getPersonalInformationAccessYn());
    }else {
    	getBuyerRequestDto.setPersonalInformationAccessYn("N");
    }

    return ApiResult.success(buyerBosService.getBuyer(getBuyerRequestDto));
  }

  /**
   * 회원 그룹변경 이력 조회
   *
   * @param getBuyerGroupChangeHistoryListRequestDto
   * @return GetBuyerGroupChangeHistoryListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getBuyerGroupChangeHistoryList")
  @ApiOperation(value = "회원 그룹변경이력 조회", httpMethod = "POST", notes = "회원 그룹변경이력 조회")
  public ApiResult<?> getBuyerGroupChangeHistoryList(GetBuyerGroupChangeHistoryListRequestDto getBuyerGroupChangeHistoryListRequestDto) throws Exception {

    return ApiResult.success(userBuyerBosBiz.getBuyerGroupChangeHistoryList(getBuyerGroupChangeHistoryListRequestDto));
  }

  /**
   * 나를 추천한 추천인 리스트 조회
   *
   * @param getBuyerRecommendListRequestDto
   * @return GetBuyerRecommendListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getBuyerRecommendList")
  @ApiOperation(value = "추천인리스트 조회", httpMethod = "POST", notes = "추천인리스트 조회")
  public ApiResult<?> getBuyerRecommendList(GetBuyerRecommendListRequestDto getBuyerRecommendListRequestDto) throws Exception {

    return ApiResult.success(buyerBosService.getBuyerRecommendList(getBuyerRecommendListRequestDto));
  }

  /**
   * 이메일 중복체크
   *
   * @param commonDuplicateMailRequestDto
   * @return CommonDuplicateMailResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/checkDuplicateMail")
  @ApiOperation(value = "이메일 중복체크", httpMethod = "POST", notes = "이메일 중복체크")
  public ApiResult<?> checkDuplicateMail(CommonDuplicateMailRequestDto commonDuplicateMailRequestDto) throws Exception {
    CommonDuplicateMailResponseDto commonDuplicateMailResponseDto = new CommonDuplicateMailResponseDto();
    commonDuplicateMailResponseDto.setCount(userBuyerBiz.checkDuplicateMail(commonDuplicateMailRequestDto));
    return ApiResult.success(commonDuplicateMailResponseDto);
  }

  /**
   * 회원 수정
   *
   * @param putBuyerRequestDto
   * @return PutBuyerResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/putBuyer")
  @ApiOperation(value = "고객회원정보 수정", httpMethod = "POST", notes = "고객회원정보 수정")
  public ApiResult<?> putBuyer(PutBuyerRequestDto putBuyerRequestDto) throws Exception {
    UserVo userVo = SessionUtil.getBosUserVO();
    if(userVo != null) {
    	putBuyerRequestDto.setPersonalInformationAccessYn(userVo.getPersonalInformationAccessYn());
    }else {
    	putBuyerRequestDto.setPersonalInformationAccessYn("N");
    }
    return userBuyerBosBiz.putBuyer(putBuyerRequestDto);
  }

  /**
   * 휴면회원 단일조회
   *
   * @param getBuyerRequestDto
   * @return GetBuyerResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/buyer/getBuyerMove")
  @ApiOperation(value = "휴면회원 단일 조회", httpMethod = "POST", notes = "휴면회원 단일 조회")
  public ApiResult<?> getBuyerMove(GetBuyerRequestDto getBuyerRequestDto) throws Exception {
    UserVo userVo = SessionUtil.getBosUserVO();
    if(userVo != null) {
    	getBuyerRequestDto.setPersonalInformationAccessYn(userVo.getPersonalInformationAccessYn());
    }else {
    	getBuyerRequestDto.setPersonalInformationAccessYn("N");
    }
    return ApiResult.success(buyerBosService.getBuyerMove(getBuyerRequestDto));
  }

  /**
   * 비밀번호 초기화
   *
   * @param putPasswordClearRequestDto
   * @return PutPasswordClearResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/user/putPasswordClear")
  @ApiOperation(value = "비밀번호 초기화", httpMethod = "POST", notes = "비밀번호 초기화")
  public ApiResult<?> putPasswordClear(PutPasswordClearRequestDto putPasswordClearRequestDto) throws Exception {

    return userBuyerBosBiz.putPasswordClear(putPasswordClearRequestDto);
  }

  /**
   * 개인정보 이력관리 목록 조회
   *
   * @param getUserChangeHistoryListRequestDto
   * @return GetUserChangeHistoryListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/user/getUserChangeHistoryList")
  @ApiOperation(value = "개인정보 변경이력 목록 조회", httpMethod = "POST", notes = "개인정보 변경이력 목록 조회")
  public ApiResult<?> getUserChangeHistoryList(GetUserChangeHistoryListRequestDto getUserChangeHistoryListRequestDto) throws Exception {

    return ApiResult.success(buyerBosService.getUserChangeHistoryList((GetUserChangeHistoryListRequestDto) BindUtil.convertRequestToObject(request,
                                                                                                                                           GetUserChangeHistoryListRequestDto.class)));
  }

  /**
   * 악성클레임 히스토리 조회
   *
   * @param getUserMaliciousClaimHistoryListRequestDto
   * @return GetUserMaliciousClaimHistoryListResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/admin/ur/userMaliciousClaim/getUserMaliciousClaimHistoryList")
  @ApiOperation(value = "악성클레임 히스토리 조회", httpMethod = "POST", notes = "악성클레임 히스토리 조회")
  public ApiResult<?> getUserMaliciousClaimHistoryList(GetUserMaliciousClaimHistoryListRequestDto getUserMaliciousClaimHistoryListRequestDto) throws Exception {

    return userBuyerBosBiz.getUserMaliciousClaimHistoryList(getUserMaliciousClaimHistoryListRequestDto);
  }



	/**
	 * 회원 적립금 리스트조회
	 *
	 * @param getBuyerListRequestDto
	 * @return GetBuyerListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyer/getPointInfo")
	@ApiOperation(value = "회원별 적립금 정보 조회", httpMethod = "POST", notes = "회원별 적립금 정보  조회")
	public ApiResult<?> getPointInfo(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {
		return userBuyerBosBiz.getPointInfo((GetBuyerListRequestDto) BindUtil.convertRequestToObject(request, GetBuyerListRequestDto.class));
	}

    /**
     * 회원 임직원 할인정보 조회
     *
     * @param dto EmployeeDiscountRequestDto
     * @return
     * @throws Exception Exception
     */
    @PostMapping(value = "/admin/ur/buyer/getEmployeeDiscount")
    @ApiOperation(value = "회원 임직원 할인정보 조회", httpMethod = "POST", notes = "회원 임직원 할인정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "urErpEmployeeCd", value = "임직원 코드", required = true, dataType = "String")
    })
    public ApiResult<?> getEmployeeDiscount(@RequestParam(value = "urErpEmployeeCd") String urErpEmployeeCd) throws Exception {
        return ApiResult.success(policyBenefitEmployeeBiz.getEmployeeDiscountBrandByUser(urErpEmployeeCd));
    }

    /**
     * 회원 임직원 할인정보 과거내역 조회
     *
     * @param dto EmployeeDiscountRequestDto
     * @return
     * @throws Exception Exception
     */
    @PostMapping(value = "/admin/ur/buyer/getEmployeeDiscountPastInfo")
    @ApiOperation(value = "회원 임직원 할인정보 과거내역 조회", httpMethod = "POST", notes = "회원 임직원 할인정보 과거내역 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "urErpEmployeeCd", value = "임직원 코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "searchDate", value = "조회년월(yyyy-MM)", required = true, dataType = "String")
    })
    public ApiResult<?> getEmployeeDiscountPastInfo(@RequestParam(value = "urErpEmployeeCd") String urErpEmployeeCd, @RequestParam(value = "searchDate") String searchDate) throws Exception {
      LocalDate localDate = LocalDate.parse(searchDate + "-01");
      return ApiResult.success(policyBenefitEmployeeBiz.getEmployeeDiscountPastByUser(urErpEmployeeCd, searchDate + "-01", searchDate + "-" + localDate.lengthOfMonth()));
    }

  @PostMapping(value = "/admin/ur/buyer/addBuyerDrop")
  @ApiOperation(value = "회원 탈퇴 진행", httpMethod = "POST", notes = "회원 탈퇴 진행")
  @ApiResponses(value = {
          @ApiResponse(code = 900, message = "response data : null"),
          @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")
  })
  public ApiResult<?> addBuyerDrop(UserDropRequestDto dto) throws Exception {
    return buyerBosService.addBuyerDrop(dto);
  }

}

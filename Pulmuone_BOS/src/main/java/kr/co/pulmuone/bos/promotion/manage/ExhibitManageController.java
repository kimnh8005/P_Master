package kr.co.pulmuone.bos.promotion.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitMessage;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitTp;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.GiftShippingTp;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.UndeliverableAreaTp;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupDetlVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
import kr.co.pulmuone.v1.promotion.manage.service.ExhibitManageBiz;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 프로모션-기획전관리 BOS Controller
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2020.12.02.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
public class ExhibitManageController {

  final String JOB_ADD = "ADD";
  final String JOB_PUT = "PUT";

  final String JOB_PUT_APPROVAL_REQUEST = "PUT_APPROVAL_REQUEST";

  //@SuppressWarnings("unused")
  //@Autowired(required = true)
  //private HttpServletRequest request;

  @Autowired(required=true)
  private HttpServletRequest request;

  @Autowired
  private ExhibitManageBiz exhibitManageBiz;

  @Autowired
  private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시페이지 기획전 리스트조회
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitList")
  @ApiOperation(value = "기획전리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
      log.debug("# Pg.page     :: " + exhibitManageRequestDto.getPage());
      log.debug("# Pg.sPage    :: " + exhibitManageRequestDto.getsPage());
      log.debug("# Pg.ePage    :: " + exhibitManageRequestDto.getePage());
      log.debug("# Pg.pageSize :: " + exhibitManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageRequestDto reqDto = null;
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }

    reqDto = (ExhibitManageRequestDto) BindUtil.convertRequestToObject(request, ExhibitManageRequestDto.class);
    log.debug("# ExhibitManageController.reqDto[1] :: " + reqDto.toString());

    // ------------------------------------------------------------------------
    // 조회조건 Set
    // ------------------------------------------------------------------------
    // 키워드
    if(StringUtil.isEquals(reqDto.getSearchSe(), "NAME")) {
      // 기획전명
      reqDto.setTitle(StringUtil.nvl(reqDto.getKeyWord()));
    }
    else if(StringUtil.isEquals(reqDto.getSearchSe(), "ID")) {
      // 기획전ID
      reqDto.setEvExhibitId(StringUtil.nvl(reqDto.getKeyWord()));
    }
    // 코드값에 ALL 문자열이 포함되어 있어서  indexOf("ALL") != 0 으로 둠, 이 경우 ALL은 항상 맨 먼저 나타나게 해야 함
    // 참고
    //  1) indexOf("ALL") < 0 => ALL 이 아닌 경우
    //  2) indexOf("ALL") > 0 => ALL 이 아니지만, MALL_DIV에서 ALL을 인식한 경우
    // 몰구분(ListString -> List<String>)
    if(StringUtil.isNotEmpty(reqDto.getMallDiv()) && reqDto.getMallDiv().indexOf("ALL") != 0 ) {

      reqDto.setMallDivList(Stream.of(reqDto.getMallDiv().split(Constants.ARRAY_SEPARATORS))
                                  .map(String::trim)
                                  .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                  .collect(Collectors.toList()));
    }
    // 승인상태
    if(StringUtil.isNotEmpty(reqDto.getApprovalStatus()) && reqDto.getApprovalStatus().indexOf("ALL") != 0 ) {

      reqDto.setApprovalStatusList(Stream.of(reqDto.getApprovalStatus().split(Constants.ARRAY_SEPARATORS))
                                                  .map(String::trim)
                                                  .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                                  .collect(Collectors.toList()));
    }
    // 진행상태
    if(StringUtil.isNotEmpty(reqDto.getStatusYnSe()) && reqDto.getStatusYnSe().indexOf("ALL") < 0 ) {

      reqDto.setStatusYnSeList(Stream.of(reqDto.getStatusYnSe().split(Constants.ARRAY_SEPARATORS))
                                     .map(String::trim)
                                     .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                     .collect(Collectors.toList()));
    }
    // 진행상태 : List와 개별 변수에 다 Set 함
    if(StringUtil.isNotEmpty(reqDto.getStatusYnSe()) && reqDto.getStatusYnSe().indexOf("ALL") < 0 ) {
      // 진행상태-진행예정
      if (reqDto.getStatusYnSe().indexOf("WAIT") >= 0) {
        reqDto.setStatusWaitYn("Y");
      }
      // 진행상태-진행중
      if (reqDto.getStatusYnSe().indexOf("ING") >= 0) {
        reqDto.setStatusIngYn("Y");
      }
      // 진행상태-진행완료
      if (reqDto.getStatusYnSe().indexOf("END") >= 0) {
        reqDto.setStatusEndYn("Y");
      }
    }
    // 노출범위(디바이스)
    if(StringUtil.isNotEmpty(reqDto.getGoodsDisplayType()) && reqDto.getGoodsDisplayType().indexOf("ALL") < 0 ) {

      reqDto.setGoodsDisplayTypeList(Stream.of(reqDto.getGoodsDisplayType().split(Constants.ARRAY_SEPARATORS))
                                           .map(String::trim)
                                           .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                           .collect(Collectors.toList()));
    }
    // 노출범위(디바이스) : List와 개별 변수에 다 Set 함
    if(StringUtil.isNotEmpty(reqDto.getGoodsDisplayType()) && reqDto.getGoodsDisplayType().indexOf("ALL") < 0 ) {
      // 노출범위(디바이스)-PC Web
      if (reqDto.getGoodsDisplayType().indexOf("GOODS_DISPLAY_TYPE.WEB_PC") >= 0) {
        reqDto.setDispWebPcYn("Y");
      }
      // 노출범위(디바이스)-M Web
      if (reqDto.getGoodsDisplayType().indexOf("GOODS_DISPLAY_TYPE.WEB_MOBILE") >= 0) {
        reqDto.setDispWebMobileYn("Y");
      }
      // 노출범위(디바이스)-APP
      if (reqDto.getGoodsDisplayType().indexOf("GOODS_DISPLAY_TYPE.APP") >= 0) {
        reqDto.setDispAppYn("Y");
      }
    }
    // 임직원 전용 여부
    if(StringUtil.isNotEmpty(reqDto.getEvEmployeeTp()) && reqDto.getEvEmployeeTp().indexOf("ALL") < 0 ) {

      reqDto.setEvEmployeeTpList(Stream.of(reqDto.getEvEmployeeTp().split(Constants.ARRAY_SEPARATORS))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                       .collect(Collectors.toList()));
    }
    // 접근권한설정유형
    if(StringUtil.isNotEmpty(reqDto.getUserGroupFilter())) {

      reqDto.setUserGroupIdList(Stream.of(reqDto.getUserGroupFilter().split(Constants.ARRAY_SEPARATORS))
                                    .map(String::trim)
                                    .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                    .collect(Collectors.toList()));
    }
    // 지급방식(증정방식)리스트
    if(StringUtil.isNotEmpty(reqDto.getGiftGiveTp()) && reqDto.getGiftGiveTp().indexOf("ALL") < 0 ) {

      reqDto.setGiftGiveTpList(Stream.of(reqDto.getGiftGiveTp().split(Constants.ARRAY_SEPARATORS))
                                     .map(String::trim)
                                     .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                     .collect(Collectors.toList()));
    }
    // 비회원노출여부
    if(StringUtil.isEquals(reqDto.getDispNonmemberYn(), "ALL")) {
      reqDto.setDispNonmemberYn("");
    }
    // 진행기간-시작일자
    if (StringUtil.isNotEmpty(reqDto.getStartDt())) {
      reqDto.setStartDt(reqDto.getStartDt() + "000000");
    }
    // 진행기간-종료일자
    if (StringUtil.isNotEmpty(reqDto.getEndDt())) {
      reqDto.setEndDt(reqDto.getEndDt() + "235959");
    }
    log.debug("# ExhibitManageController.reqDto[2] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitList(reqDto);
    //return exhibitManageBiz.selectExhibitList((ExhibitManageRequestDto) BindUtil.convertRequestToObject(request, ExhibitManageRequestDto.class));

  }


  /**
   * 기획전 담당자리스트(조회조건 콤보용)
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitManagerList")
  @ApiOperation(value = "기획전담당자리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitManagerList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitManagerList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    log.debug("# In.exhibitTp :: " + exhibitManageRequestDto.getExhibitTp());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitManagerList(exhibitManageRequestDto);

  }


  /**
   * 기획전상세조회-기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitInfo")
  @ApiOperation(value = "기획전상세조회-기본정보")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
                              )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitInfo(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitInfo Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
      log.debug("# Pg.page     :: " + exhibitManageRequestDto.getPage());
      log.debug("# Pg.sPage    :: " + exhibitManageRequestDto.getsPage());
      log.debug("# Pg.ePage    :: " + exhibitManageRequestDto.getePage());
      log.debug("# Pg.pageSize :: " + exhibitManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitInfo(exhibitManageRequestDto);

  }


  /**
   * 기획전 상세조회 - 일반(그룹) - 그룹리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectfExhibitGroupList")
  @ApiOperation(value = "기획전상세조회-일반-그룹리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectfExhibitGroupList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectfExhibitGroupList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectfExhibitGroupList(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 일반(그룹) - 그룹상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectfExhibitGroupGoodsList")
  @ApiOperation(value = "기획전상세조회-일반-그룹상품리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                                  + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                                  + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 그룹ID를 입력하세요."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectfExhibitGroupGoodsList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectfExhibitGroupGoodsList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitGroupId())) {
      // 그룹ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_GROUPT_ID);
    }
    log.debug("# In.evExhibitGroupId :: " + exhibitManageRequestDto.getEvExhibitGroupId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectfExhibitGroupGoodsList(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 골라담기 - 기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectfExhibitSelectInfo")
  @ApiOperation(value = "기획전상세조회-골라담기-기본정보")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                                   + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                                   + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectfExhibitSelectInfo(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectfExhibitSelectInfo Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectfExhibitSelectInfo(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 골라담기 - 상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitSelectGoodsList")
  @ApiOperation(value = "기획전상세조회-골라담기-상품리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitSelectGoodsList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitSelectGoodsList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitSelectGoodsList(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 골라담기 - 추가상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitSelectAddGoodsList")
  @ApiOperation(value = "기획전상세조회-골라담기-추가상품리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
                              )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitSelectAddGoodsList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitSelectAddGoodsList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitSelectAddGoodsList(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 증정행사 - 기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitGiftInfo")
  @ApiOperation(value = "기획전상세조회-증정행사-기본정보")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
                              )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitGiftInfo(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitGiftInfo Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitGiftInfo(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 증정행사 - 증정상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitGiftGoodsList")
  @ApiOperation(value = "기획전상세조회-증정행사-증정상품리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitGiftGoodsList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitGiftGoodsList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitGiftGoodsList(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 증정행사 - 적용상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitGiftTargetGoodsList")
  @ApiOperation(value = "기획전상세조회-증정행사-적용상품리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitGiftTargetGoodsList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitGiftTargetGoodsList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitGiftTargetGoodsList(exhibitManageRequestDto);

  }

  /**
   * 기획전 상세조회 - 증정행사 - 적용브랜드리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitGiftTargetBrandList")
  @ApiOperation(value = "기획전상세조회-증정행사-적용브랜드리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitGiftTargetBrandList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitGiftTargetBrandList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitGiftTargetBrandList(exhibitManageRequestDto);

  }

  /**
   * 상품정보리스트조회(엑셀용)
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectGoodsInfoList")
  @ApiOperation(value = "기획전상세조회-증정행사-적용브랜드리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EXHIBIT_MANAGE_PARAM_NO_GROUP_GOODS_ID_LIST - 상품코드정보를 확인하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectGoodsInfoList(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectGoodsInfoList Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }

    // ilGoodsId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setIlGoodsIdList 에 넣음
    try {
      exhibitManageRequestDto.setIlGoodsIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getIlGoodsIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_GROUP_GOODS_ID_LIST);
    }
    log.debug("# In.ilGoodsIdList :: " + exhibitManageRequestDto.getIlGoodsIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectGoodsInfoList(exhibitManageRequestDto);

  }

  /**
   * 엑셀다운로드-증정행사-적용대상상품
   * @param dto
   * @return
   * @throws Exception
   */
  @SuppressWarnings({ "unchecked", "unused" })
  @ApiOperation(value = "증정행사적용대상상품엑셀다운로드")
  @RequestMapping(value = "/admin/pm/exhibit/getExportExcelExhibitGiftTargetGoodsList")
  public ModelAndView getExportExcelExhibitGiftTargetGoodsList(@RequestBody ExhibitManageRequestDto exhibitManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.getExportExcelExhibitGiftTargetGoodsList Start");
    log.debug("# ######################################");
    log.debug("# exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    log.debug("# request.evExhibitId  :: " + request.getParameter("evExhibitId"));
    log.debug("# request.giftTargetTp :: " + request.getParameter("giftTargetTp"));

    String evExhibitId = StringUtil.nvl(request.getParameter("evExhibitId"));
    String giftTargetTp = StringUtil.nvl(request.getParameter("giftTargetTp"));

    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "증정행사적용대상상품목록" + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    //log.debug("# excelFileName :: " + excelFileName);

    /*
     * 컬럼별 width 목록 : 단위 pixel
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
     */
    Integer[] widthListOfFirstWorksheet = {150, 300, 300, 300, 300, 300, 300, 300, 300};

    /*
     * 본문 데이터 컬럼별 정렬 목록
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
     * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
     */
    String[] alignListOfFirstWorksheet = {"center", "center", "center", "left", "center", "center", "center", "center", "center"};


    /*
     * 본문 데이터 컬럼별 데이터 property 목록
     * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
     */
    String[] propertyListOfFirstWorksheet = {"no", "targetEnableYnNm", "ilGoodsId", "goodsNm", "urBrandNm", "dpBrandNm", "warehouseNm", "shippingTmplNm", "undeliverableAreaTpNm"};

    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = {"No", "대상가능여부", "상품코드", "상품명", "표준브랜드", "전시브랜드", "출고처명", "배송정책", "도서산간제주배송"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    /*
     * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
     * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
     */
    ApiResult<?> resultApi = null;
    List<ExhibitGiftGoodsVo> resultList = new ArrayList<ExhibitGiftGoodsVo>();;

    try {

      String gbUrWarehouseId        = "";
      String gbStoreYn              = "";
      String gbIlShippingTmplId     = "";
      String gbUndeliverableAreaTp  = "";
      String targetEnableYn         = "";


      // ----------------------------------------------------------------------
      // 1. 증정상품조회
      // ----------------------------------------------------------------------
      exhibitManageRequestDto.setEvExhibitId(evExhibitId);

      resultApi = exhibitManageBiz.selectExhibitGiftGoodsList(exhibitManageRequestDto);

      if (resultApi != null) {

        ExhibitManageResponseDto exhibitManageResponseDto = (ExhibitManageResponseDto)resultApi.getData();

        if (exhibitManageResponseDto != null) {

          List<ExhibitGiftGoodsVo>  resultGiftGoodsList = (List<ExhibitGiftGoodsVo>)exhibitManageResponseDto.getRows();

          if (resultGiftGoodsList != null && resultGiftGoodsList.size() > 0) {

            for (ExhibitGiftGoodsVo vo : resultGiftGoodsList) {
              gbUrWarehouseId        = vo.getUrWarehouseId();
              gbStoreYn              = vo.getStoreYn();
              gbIlShippingTmplId     = vo.getIlShippingTmplId();
              gbUndeliverableAreaTp  = vo.getUndeliverableAreaTp();
              break;
            }

          } // End of if (resultGiftGoodsList != null && resultGiftGoodsList.size() > 0)

        } // End of if (exhibitManageResponseDto != null)

      } // End of if (resultApi != null)

      // ----------------------------------------------------------------------
      // 2. 적용대상상품조회
      // ----------------------------------------------------------------------
      resultApi = exhibitManageBiz.selectExhibitGiftTargetGoodsList(exhibitManageRequestDto);

      int i = 0;

      if (resultApi != null) {

        ExhibitManageResponseDto exhibitManageResponseDto = (ExhibitManageResponseDto)resultApi.getData();

        if (exhibitManageResponseDto != null) {

          List<ExhibitGiftGoodsVo>  resultGiftTargetGoodsList = (List<ExhibitGiftGoodsVo>)exhibitManageResponseDto.getRows();

          if (resultGiftTargetGoodsList != null && resultGiftTargetGoodsList.size() > 0) {

            for (ExhibitGiftGoodsVo vo : resultGiftTargetGoodsList) {

              // 대상가능여부
              if (StringUtil.isEquals(vo.getGiftShippingTp(), GiftShippingTp.COMBINED.getCode())) {
                if (StringUtil.isEquals(vo.getUrWarehouseId(), gbUrWarehouseId) &&
                    StringUtil.isEquals(vo.getStoreYn(), gbIlShippingTmplId)  &&
                    StringUtil.isEquals(vo.getIlShippingTmplId(), gbIlShippingTmplId) &&
                    StringUtil.isEquals(vo.getUndeliverableAreaTp(), gbUndeliverableAreaTp)) {

                  vo.setTargetEnableYnNm("가능");
                }
                else {
                  vo.setTargetEnableYnNm("불가");
                }
              }
              else {
                vo.setTargetEnableYnNm("가능");
              }

              // 도서산간제주배송가능여부
              if (StringUtil.isEquals(vo.getUndeliverableAreaTp(), UndeliverableAreaTp.NONE.getCode())) {
                vo.setUndeliverableAreaTpNm("가능");
              }
              else {
                vo.setUndeliverableAreaTpNm("불가능");
              }

              i++;
              vo.setNo(i);
              resultList.add(vo);

            } // End of for (ExhibitGiftGoodsVo vo : resultGiftTargetGoodsList)

          } // End of if (resultGiftTargetGoodsList != null && resultGiftTargetGoodsList.size() > 0)

        } // End of if (exhibitManageResponseDto != null)

      } // End of if (resultApi != null)

    } catch (Exception e) {
        log.error(e.getMessage());
        throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()  //
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))   //
        .excelFileName(excelFileName)   //
        .build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;
  }

  /**
   * 엑셀다운로드-증정행사-적용대상브랜드
   * @param dto
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @ApiOperation(value = "증정행사적용대상브랜드엑셀다운로드")
  @RequestMapping(value = "/admin/pm/exhibit/getExportExcelExhibitGiftTargetBrandList")
  public ModelAndView getExportExcelExhibitGiftTargetBrandList(@RequestBody ExhibitManageRequestDto exhibitManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.getExportExcelExhibitGiftTargetGoodsList Start");
    log.debug("# ######################################");
    log.debug("# exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    log.debug("# request.evExhibitId  :: " + request.getParameter("evExhibitId"));
    log.debug("# request.giftTargetTp :: " + request.getParameter("giftTargetTp"));

    String evExhibitId = StringUtil.nvl(request.getParameter("evExhibitId"));

    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "증정행사적용대상브랜드목록" + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    //log.debug("# excelFileName :: " + excelFileName);

    /*
     * 컬럼별 width 목록 : 단위 pixel
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
     */
    Integer[] widthListOfFirstWorksheet = {150, 300, 300, 300};

    /*
     * 본문 데이터 컬럼별 정렬 목록
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
     * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
     */
    String[] alignListOfFirstWorksheet = {"center", "center", "center", "left"};


    /*
     * 본문 데이터 컬럼별 데이터 property 목록
     * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
     */
    String[] propertyListOfFirstWorksheet = {"no", "brandId", "giftTargetBrandTpNm", "brandNm"};

    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = {"No", "브랜드ID", "브랜드구분", "브랜드명"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    /*
     * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
     * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
     */
    ApiResult<?> resultApi = null;
    List<ExhibitGiftGoodsVo> resultList = new ArrayList<ExhibitGiftGoodsVo>();;

    try {
      // ----------------------------------------------------------------------
      // 적용대상브랜드조회
      // ----------------------------------------------------------------------
      exhibitManageRequestDto.setEvExhibitId(evExhibitId);
      resultApi = exhibitManageBiz.selectExhibitGiftTargetBrandList(exhibitManageRequestDto);

      int i = 0;

      if (resultApi != null) {

        ExhibitManageResponseDto exhibitManageResponseDto = (ExhibitManageResponseDto)resultApi.getData();

        if (exhibitManageResponseDto != null) {

          List<ExhibitGiftGoodsVo>  resultGiftTargetGoodsList = (List<ExhibitGiftGoodsVo>)exhibitManageResponseDto.getRows();

          if (resultGiftTargetGoodsList != null && resultGiftTargetGoodsList.size() > 0) {

            for (ExhibitGiftGoodsVo vo : resultGiftTargetGoodsList) {

              i++;
              vo.setNo(i);
              resultList.add(vo);

            } // End of for (ExhibitGiftGoodsVo vo : resultGiftTargetGoodsList)

          } // End of if (resultGiftTargetGoodsList != null && resultGiftTargetGoodsList.size() > 0)

        } // End of if (exhibitManageResponseDto != null)

      } // End of if (resultApi != null)

    } catch (Exception e) {
      log.error(e.getMessage());
      throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()  //
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))   //
        .excelFileName(excelFileName)   //
        .build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;
  }




  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  /**
   * 기획전 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibit")
  @ApiOperation(value = "기획전삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET - 기획전 기본 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL - 기획전 기본 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_PROC - 기획전 기본 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibit(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitIdListString())) {

      // 기획전 기본 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitIdList :: " + exhibitManageRequestDto.getEvExhibitIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibit(exhibitManageRequestDto);

  }

  /**
   * 그룹상세 삭제(그룹상품)
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibitGroupDetl")
  @ApiOperation(value = "그룹상세삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET - 기획전 그룹상세 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL - 기획전 그룹상세 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_PROC - 기획전 그룹상세 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibitGroupDetl(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibitGroupDetl Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitGroupDetlIdListString())) {

      // 기획전 그룹상세 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitGroupDetlId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitGroupDetlIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitGroupDetlIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitGroupDetlIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitGroupDetlIdList :: " + exhibitManageRequestDto.getEvExhibitGroupDetlIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibitGroupDetl(exhibitManageRequestDto);

  }

  /**
   * 골라담기 상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibitSelectGoods")
  @ApiOperation(value = "골라담기상품삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET - 기획전 골라담기 상품 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL - 기획전 골라담기 상품 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_PROC - 기획전 골라담기 상품 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibitSelectGoods(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibitSelectGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitSelectGoodsIdListString())) {

      // 기획전 골라담기 상품 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitSelectGoodsId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitSelectGoodsIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitSelectGoodsIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitSelectGoodsIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitSelectGoodsIdList :: " + exhibitManageRequestDto.getEvExhibitSelectGoodsIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibitSelectGoods(exhibitManageRequestDto);

  }

  /**
   * 골라담기 추가상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibitSelectAddGoods")
  @ApiOperation(value = "골라담기상품삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET - 기획전 골라담기 추가상품 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL - 기획전 골라담기 추가상품 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_PROC - 기획전 골라담기 추가상품 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibitSelectAddGoods(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibitSelectAddGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitSelectAddGoodsIdListString())) {

      // 기획전 골라담기 상품 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitSelectAddGoodsId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitSelectAddGoodsIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitSelectAddGoodsIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitSelectAddGoodsIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitSelectAddGoodsIdList :: " + exhibitManageRequestDto.getEvExhibitSelectAddGoodsIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibitSelectAddGoods(exhibitManageRequestDto);

  }

  /**
   * 증정행사 상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibitGiftGoods")
  @ApiOperation(value = "증정행사상품삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET - 기획전 증정행사 상품 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL - 기획전 증정행사 상품 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_PROC - 기획전 증정행사 상품 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibitGiftGoods(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibitGiftGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitGiftGoodsIdListString())) {

      // 기획전 골라담기 상품 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitGiftGoodsId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitGiftGoodsIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitGiftGoodsIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitGiftGoodsIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitGiftGoodsIdListString :: " + exhibitManageRequestDto.getEvExhibitGiftGoodsIdListString().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibitGiftGoods(exhibitManageRequestDto);

  }

  /**
   * 증정행사 적용대상상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibitGiftTargetGoods")
  @ApiOperation(value = "증정행사적용대상상품삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET - 기획전 증정행사 대상상품 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL - 기획전 증정행사 대상상품 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_PROC - 기획전 증정행사 대상상품 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibitGiftTargetGoods(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibitGiftTargetGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitGiftTargetGoodsIdListString())) {

      // 기획전 골라담기 상품 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitGiftTargetGoodsId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitGiftTargetGoodsIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitGiftTargetGoodsIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitGiftTargetGoodsIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitGiftTargetGoodsIdListString :: " + exhibitManageRequestDto.getEvExhibitGiftTargetGoodsIdListString().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibitGiftTargetGoods(exhibitManageRequestDto);

  }

  /**
   * 증정행사 대상브랜드 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/delExhibitGiftTargetBrand")
  @ApiOperation(value = "증정행사대상브랜드삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET - 기획전 증정행사 대상브랜드 정보삭제 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL - 기획전 증정행사 대상브랜드 정보삭제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_PROC - 기획전 증정행사 대상브랜드 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delExhibitGiftTargetBrand(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.delExhibitGiftTargetBrand Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitGiftTargetBrandIdListString())) {

      // 기획전 골라담기 상품 정보삭제 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_INPUT_TARGET);
    }

    // evExhibitGiftTargetBrandId JsonString -> List<String> 으로 변환하여  exhibitManageRequestDto.setEvExhibitGiftTargetBrandIdList 에 넣음
    try {
      exhibitManageRequestDto.setEvExhibitGiftTargetBrandIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitGiftTargetBrandIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evExhibitGiftTargetBrandIdListString :: " + exhibitManageRequestDto.getEvExhibitGiftTargetBrandIdListString().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.delExhibitGiftTargetBrand(exhibitManageRequestDto);

  }



  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 등록
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/addExhibit")
  @ApiOperation(value = "기획전등록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_INPUT_TARGET - 기획전 기본 정보등록 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL - 기획전 기본 정보등록 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_PROC - 기획전 기본 정보등록 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> addExhibit(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.addExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getExhibitTp())) {
      // 기획전유형을 확인하세요
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_TP);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getExhibitDataJsonString())) {
      // 기획전 기본 정보등록 입력정보 오류입니다
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_INPUT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    return this.porcExhibit(exhibitManageRequestDto, JOB_ADD);

  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 수정
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/putExhibit")
  @ApiOperation(value = "기획전 수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_INPUT_TARGET - 기획전 기본 정보수정 입력정보 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL - 기획전 기본 정보수정 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC - 기획전 기본 정보수정 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> putExhibit(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.putExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getExhibitTp())) {
      // 기획전유형을 확인하세요
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_TP);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getExhibitDataJsonString())) {
      // 기획전 기본 정보등록 입력정보 오류입니다
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_INPUT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    return this.porcExhibit(exhibitManageRequestDto, JOB_PUT);

  }

  @RequestMapping(value="/admin/pm/exhibit/putApprovalRequestExhibit")
  @ApiOperation(value = "기획전 수정 - 승인요청")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_INPUT_TARGET - 기획전 기본 정보수정 입력정보 오류입니다."
                  + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL - 기획전 기본 정보수정 오류입니다."
                  + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC - 기획전 기본 정보수정 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> putApprovalRequestExhibit(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.putApprovalRequestExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getExhibitDataJsonString())) {
      // 기획전 기본 정보등록 입력정보 오류입니다
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_INPUT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    exhibitManageRequestDto.setExhibitTp(ExhibitTp.NORMAL.getCode());
    return this.porcExhibit(exhibitManageRequestDto, JOB_PUT_APPROVAL_REQUEST);

  }

  /**
   * 증정행사 대표상품 변경
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/putExhibitGiftRepGoods")
  @ApiOperation(value = "기획전삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_INPUT_TARGET - 대표상품변경 정보수정 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL - 대표상품변경 해제 오류입니다."
          + "EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC - 대표상품변경 설정 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> putExhibitGiftRepGoods(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.putExhibitGiftRepGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId()) || StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitGiftGoodsId())) {

      // 입력정보 오류입니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_PUT_FAIL);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    return exhibitManageBiz.putExhibitGiftRepGoods(exhibitManageRequestDto);

  }


  // ########################################################################
  // private
  // ########################################################################
  /**
   * 등록/수정 입력데이터 Set
   * @param exhibitManageRequestDto
   * @param jobSe
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private  ApiResult<?> porcExhibit(ExhibitManageRequestDto exhibitManageRequestDto, String jobSe) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.porcExhibit Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    String exhibitTp = exhibitManageRequestDto.getExhibitTp();

    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // 데이터 변환 - 기본정보
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // @ 1. 기획전 기본정보 변환 : JsonString -> ExhibitVo
    // ------------------------------------------------------------------------
    try {
      ObjectMapper objMqpper = new ObjectMapper();
      exhibitManageRequestDto.setExhibitInfo(objMqpper.readValue(exhibitManageRequestDto.getExhibitDataJsonString(), ExhibitVo.class));
    }
    catch (Exception e) {
      log.error("# In.exhibitManageRequestDto(1) :: " + exhibitManageRequestDto.toString());
      if (StringUtil.isEquals(jobSe, JOB_ADD)) {
        return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL);
      }
      else {
        return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL);
      }
    }

    // ------------------------------------------------------------------------
    // 2. 데이터 변환 - 기획전유형별
    // ------------------------------------------------------------------------
    if (StringUtil.isEquals(exhibitTp, ExhibitTp.NORMAL.getCode())) {
      // **********************************************************************
      // 2.1. 일반기획전
      // **********************************************************************
      // ----------------------------------------------------------------------
      // @ 2.1.1. 기획전 그룹리스트정보 변환 : JsonString -> List<ExhibitGroupVo>
      // ----------------------------------------------------------------------
      try {
        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getGroupListJsonString())) {
          exhibitManageRequestDto.setGroupList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getGroupListJsonString(), ExhibitGroupVo.class));
        }
      }
      catch (Exception e) {
        log.error("# In.exhibitManageRequestDto(2) :: " + exhibitManageRequestDto.toString());
        log.error("# e :: " + e.toString());
        e.printStackTrace();
        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_INPUT_TARGET);
        }
        else {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.1.2. 기획전 그룹리스트 내 그룹상품리스트 변환
      // ----------------------------------------------------------------------
      if (exhibitManageRequestDto.getGroupList() != null && exhibitManageRequestDto.getGroupList().size() > 0) {


        for (ExhibitGroupVo exhibitGroupVo : exhibitManageRequestDto.getGroupList()) {

          // ----------------------------------------------------------------------
          // @ 기획전 그룹리스트정보 변환 : JsonString -> List<exhibitGroupGoodsVo>
          // ----------------------------------------------------------------------
          try {
            if (StringUtil.isNotEmpty(exhibitGroupVo.getGroupGoodsListJsonString())) {
              exhibitGroupVo.setGroupGoodsList(BindUtil.convertJsonArrayToDtoList(exhibitGroupVo.getGroupGoodsListJsonString(), ExhibitGroupDetlVo.class));
            }
          }
          catch (Exception e) {
            log.error("# In.exhibitManageRequestDto(3) :: " + exhibitManageRequestDto.toString());
            if (StringUtil.isEquals(jobSe, JOB_ADD)) {
              return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_INPUT_TARGET);
            }
            else {
              return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_PUT_FAIL_INPUT_TARGET);
            }
          }

        }

      } // End of if (exhibitManageRequestDto.getGroupList() != null && exhibitManageRequestDto.getGroupList().size() > 0)
    }
    else if (StringUtil.isEquals(exhibitTp, ExhibitTp.SELECT.getCode())) {
      // **********************************************************************
      // 2.2. 골라담기
      // **********************************************************************
      // ----------------------------------------------------------------------
      // @ 2.2.1. 기획전 골라담기 변환 : JsonString -> ExhibitSelectVo
      // ----------------------------------------------------------------------
      try {
        ObjectMapper objMqpper = new ObjectMapper();
        exhibitManageRequestDto.setExhibitSelectInfo(objMqpper.readValue(exhibitManageRequestDto.getExhibitSelectInfoJsonString(), ExhibitSelectVo.class));
      }
      catch (Exception e) {
        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL_INPUT_TARGET);
        }
        else {
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.2.2. 기획전 상품리스트 변환 : JsonString -> List<ExhibitSelectGoodsVo>
      // ----------------------------------------------------------------------
      try {
        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getExhibitSelectGoodsListJsonString())) {
          exhibitManageRequestDto.getExhibitSelectInfo().setSelectGoodsList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getExhibitSelectGoodsListJsonString(), ExhibitSelectGoodsVo.class));
        }
      }
      catch (Exception e) {

        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL_INPUT_TARGET);
        }
        else {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.2.3. 기획전 추가상품리스트 변환 : JsonString -> List<ExhibitSelectGoodsVo>
      // ----------------------------------------------------------------------
      try {
        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getExhibitSelectAddGoodsListJsonString())) {
          exhibitManageRequestDto.getExhibitSelectInfo().setSelectAddGoodsList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getExhibitSelectAddGoodsListJsonString(), ExhibitSelectGoodsVo.class));
        }
      }
      catch (Exception e) {

        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL_INPUT_TARGET);
        }
        else {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.2.4. 최대할인율계산 : ExhibitSelectVo.maxDiscountRate
      // ----------------------------------------------------------------------
      exhibitManageRequestDto.getExhibitSelectInfo().setMaxDiscountRate(exhibitManageBiz.getExhibitSelectGoodsListForMaxRate(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId(), exhibitManageRequestDto.getExhibitSelectInfo().getSelectPrice()));
    }
    else if (StringUtil.isEquals(exhibitTp, ExhibitTp.GIFT.getCode())) {
      // **********************************************************************
      // 2.3. 증정행사
      // **********************************************************************

      // ----------------------------------------------------------------------
      // @ 2.3.1. 기획전 증정행사 변환 : JsonString -> ExhibitGiftVo
      // ----------------------------------------------------------------------
      try {
        ObjectMapper objMqpper = new ObjectMapper();
        exhibitManageRequestDto.setExhibitGiftInfo(objMqpper.readValue(exhibitManageRequestDto.getExhibitGiftInfoJsonString(), ExhibitGiftVo.class));
      }
      catch (Exception e) {
        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL_INPUT_TARGET);
        }
        else {
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.3.2. 기획전 증정행사 상품 변환 : JsonString -> List<ExhibitGiftGoodsVo>
      // ----------------------------------------------------------------------
      try {
        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getExhibitGiftGoodsListJsonString())) {
          exhibitManageRequestDto.getExhibitGiftInfo().setGiftGoodsList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getExhibitGiftGoodsListJsonString(), ExhibitGiftGoodsVo.class));
        }
      }
      catch (Exception e) {

        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL_INPUT_TARGET);
        }
        else {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.3.3. 기획전 증정대상 상품 변환 : JsonString -> List<ExhibitGiftGoodsVo>
      // ----------------------------------------------------------------------
      try {
        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getExhibitGiftTargetGoodsListJsonString())) {
          exhibitManageRequestDto.getExhibitGiftInfo().setGiftTargetGoodsList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getExhibitGiftTargetGoodsListJsonString(), ExhibitGiftGoodsVo.class));
        }
      }
      catch (Exception e) {

        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL_INPUT_TARGET);
        }
        else {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.3.4. 기획전 증정대상 브랜드 변환 : JsonString -> List<ExhibitGiftGoodsVo>
      // ----------------------------------------------------------------------
      try {
        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getExhibitGiftTargetBrandListJsonString())) {
          exhibitManageRequestDto.getExhibitGiftInfo().setGiftTargetBrandList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getExhibitGiftTargetBrandListJsonString(), ExhibitGiftGoodsVo.class));
        }
      }
      catch (Exception e) {

        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL_INPUT_TARGET);
        }
        else {
          return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_PUT_FAIL_INPUT_TARGET);
        }
      }

      // ----------------------------------------------------------------------
      // @ 2.3.5. 기획전 그룹정보
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitGiftInfo().getExhibitDispYn(), "Y")) {
        // 기획전 전시여부가 Y인 경우
        // --------------------------------------------------------------------
        // @ 2.3.5.1. 그룹리스트정보 변환 : JsonString -> List<ExhibitGroupVo>
        // --------------------------------------------------------------------
        try {
          if (StringUtil.isNotEmpty(exhibitManageRequestDto.getGroupListJsonString())) {
            exhibitManageRequestDto.setGroupList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getGroupListJsonString(), ExhibitGroupVo.class));
          }
        }
        catch (Exception e) {
          log.error("# In.exhibitManageRequestDto(2) :: " + exhibitManageRequestDto.toString());
          log.error("# e :: " + e.toString());
          e.printStackTrace();
          if (StringUtil.isEquals(jobSe, JOB_ADD)) {
            return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_INPUT_TARGET);
          }
          else {
            return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_PUT_FAIL_INPUT_TARGET);
          }
        }

        // --------------------------------------------------------------------
        // @ 2.3.5.2. 기획전 그룹리스트 내 그룹상품리스트 변환
        // --------------------------------------------------------------------
        if (exhibitManageRequestDto.getGroupList() != null && exhibitManageRequestDto.getGroupList().size() > 0) {


          for (ExhibitGroupVo exhibitGroupVo : exhibitManageRequestDto.getGroupList()) {

            // ----------------------------------------------------------------------
            // @ 기획전 그룹리스트정보 변환 : JsonString -> List<exhibitGroupGoodsVo>
            // ----------------------------------------------------------------------
            try {
              if (StringUtil.isNotEmpty(exhibitGroupVo.getGroupGoodsListJsonString())) {
                exhibitGroupVo.setGroupGoodsList(BindUtil.convertJsonArrayToDtoList(exhibitGroupVo.getGroupGoodsListJsonString(), ExhibitGroupDetlVo.class));
              }
            }
            catch (Exception e) {
              log.error("# In.exhibitManageRequestDto(3) :: " + exhibitManageRequestDto.toString());
              if (StringUtil.isEquals(jobSe, JOB_ADD)) {
                return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_INPUT_TARGET);
              }
              else {
                return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_PUT_FAIL_INPUT_TARGET);
              }
            }

          }

        } // End of if (exhibitManageRequestDto.getGroupList() != null && exhibitManageRequestDto.getGroupList().size() > 0)

      }

    }
    else {
      // 기획전유형 오류
      log.error("# exhibitTp :: " + exhibitManageRequestDto.getExhibitTp());
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_TP);
    }

    // ------------------------------------------------------------------------
    // 임시로그 Start
    log.debug("# In.exhibitManageRequestDto(8) :: " + exhibitManageRequestDto.toString());
    if (StringUtil.isNotEmpty(exhibitManageRequestDto.getExhibitInfo())) {
      log.debug("# In.ExhibitInfo :: " + exhibitManageRequestDto.getExhibitInfo().toString());
    }
    else {
      log.debug("# In.getExhibitInfo is Null");
    }
    if (StringUtil.isNotEmpty(exhibitManageRequestDto.getGroupList())) {
      log.debug("# In.GroupList   :: " + exhibitManageRequestDto.getGroupList().toString());
    }
    else {
      log.debug("# In.getGroupList is Null");
    }


    if (StringUtil.isNotEmpty(exhibitManageRequestDto.getGroupList()) && exhibitManageRequestDto.getGroupList().size() > 0) {

      int groupIdx = 1;
      for (ExhibitGroupVo exhibitGroupVo : exhibitManageRequestDto.getGroupList()) {

        if (StringUtil.isNotEmpty(exhibitGroupVo.getGroupGoodsList()) && exhibitGroupVo.getGroupGoodsList().size() > 0) {

          for (ExhibitGroupDetlVo exhibitGroupDetlVo : exhibitGroupVo.getGroupGoodsList()) {

            log.debug("# [" + exhibitGroupVo.getGroupNm() + "]["+groupIdx+"] [" + exhibitGroupDetlVo.getIlGoodsId() + "][" + exhibitGroupDetlVo.getGoodsSort() + "]");

          }
        }
        else {
          log.debug("# [" + exhibitGroupVo.getEvExhibitGroupId() + "] Not Exists Goods");
        }
        groupIdx++;
      }
    }
    // 임시로그 End
    // ------------------------------------------------------------------------

    // ========================================================================
    // # 반환
    // ========================================================================
    // # BOS 서비스 호출
    if (StringUtil.isEquals(jobSe, JOB_ADD)) {
      // 등록
      return exhibitManageBiz.addExhibit(exhibitManageRequestDto);
    }
    else if (StringUtil.isEquals(jobSe, JOB_PUT)) {
      // 수정
      return exhibitManageBiz.putExhibit(exhibitManageRequestDto);
    }
    else if (StringUtil.isEquals(jobSe, JOB_PUT_APPROVAL_REQUEST)) {
      // 수정 - 승인요청
      return exhibitManageBiz.putApprovalRequestExhibit(exhibitManageRequestDto.getExhibitInfo());
    }
    else {
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_NO_JOB);
    }
  }


  /**
   * 기획전상세조회-상세정보
   * @param exhibitManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/exhibit/selectExhibitDetlInfo")
  @ApiOperation(value = "기획전상세조회-모든정보")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ExhibitManageResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                  + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - 기획전ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectExhibitDetlInfo(ExhibitManageRequestDto exhibitManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# ExhibitManageController.selectExhibitInfo Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto     :: " + exhibitManageRequestDto.toString());
      log.debug("# Pg.page     :: " + exhibitManageRequestDto.getPage());
      log.debug("# Pg.sPage    :: " + exhibitManageRequestDto.getsPage());
      log.debug("# Pg.ePage    :: " + exhibitManageRequestDto.getePage());
      log.debug("# Pg.pageSize :: " + exhibitManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (exhibitManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(exhibitManageRequestDto.getEvExhibitId())) {
      // 기획전ID를 입력하세요.
      return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID);
    }
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return exhibitManageBiz.selectExhibitDetlInfo(exhibitManageRequestDto);

  }


}

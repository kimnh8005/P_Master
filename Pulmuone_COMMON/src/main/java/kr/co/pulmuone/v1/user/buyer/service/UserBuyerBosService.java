package kr.co.pulmuone.v1.user.buyer.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.service.ComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PointEnums.PointProcessType;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.base.UrCommonMapper;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PointUseMapper;
import kr.co.pulmuone.v1.comm.mapper.user.buyer.BuyerMapper;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.*;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordClearRequestDto;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200625    	  천혜현           최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class UserBuyerBosService {

  @Autowired
  private BuyerMapper     buyerMapper;

  @Autowired
  private UrCommonMapper  urCommonMapper;

  @Autowired
  private SendTemplateBiz sendTemplateBiz;

  @Autowired
  private UserBuyerBosBiz userBuyerBosBiz;

  @Autowired
  private ComnBiz comnBiz;

  @Autowired
  private PointUseMapper  pointUseMapper;


  /**
   * 전체회원 리스트조회
   *
   * @param getBuyerListRequestDto
   * @return GetBuyerListResponseDto
   * @throws Exception
   */
  protected GetBuyerListResponseDto getBuyerList(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {
    if (StringUtils.isNotEmpty(getBuyerListRequestDto.getCondiValue())) {
      ArrayList<String> array = new ArrayList<>();
      StringTokenizer st = new StringTokenizer(getBuyerListRequestDto.getCondiValue(), "\n|,");
      while (st.hasMoreElements()) {
        String object = (String) st.nextElement();
        array.add(object);
      }
      getBuyerListRequestDto.setCondiValueArray(array);
    }

    // int total = buyerMapper.getBuyerListCount(dto); // total
    PageMethod.startPage(getBuyerListRequestDto.getPage(), getBuyerListRequestDto.getPageSize());
    Page<GetBuyerListResultVo> rows = buyerMapper.getBuyerList(getBuyerListRequestDto); // rows

    return GetBuyerListResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
  }

  /**
   * 회원상세 - 쿠폰정보 리스트 조회
   *
   * @param getBuyerListRequestDto
   * @return GetBuyerListResponseDto
   * @throws Exception
   */
  protected GetCouponListResponseDto getCouponList(GetCouponListRequestDto getCouponListRequestDto) throws Exception {
    PageMethod.startPage(getCouponListRequestDto.getPage(), getCouponListRequestDto.getPageSize());
    Page<GetCouponListResultVo> rows = buyerMapper.getCouponList(getCouponListRequestDto); // rows

    return GetCouponListResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
  }

  /**
   * 전체회원 리스트조회 엑셀다운로드
   *
   * @param getBuyerListRequestDto
   * @return GetBuyerListResponseDto
   * @throws Exception
   */
  protected List<GetBuyerListResultVo> getBuyerListExcel(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {
    if (StringUtils.isNotEmpty(getBuyerListRequestDto.getCondiValue())) {
      ArrayList<String> array = new ArrayList<>();
      StringTokenizer st = new StringTokenizer(getBuyerListRequestDto.getCondiValue(), "\n|,");
      while (st.hasMoreElements()) {
        String object = (String) st.nextElement();
        array.add(object);
      }
      getBuyerListRequestDto.setCondiValueArray(array);
    }
    return buyerMapper.getBuyerListExcel(getBuyerListRequestDto);
  }

  /**
   * 회원 단일조회
   *
   * @param getBuyerRequestDto
   * @return GetBuyerResponseDto
   * @throws Exception
   */
  protected GetBuyerResultVo getBuyer(GetBuyerRequestDto getBuyerRequestDto) throws Exception {
    return buyerMapper.getBuyer(getBuyerRequestDto); // rows
  }

  /**
   * 회원 그룹변경 이력 조회
   *
   * @param getBuyerGroupChangeHistoryListRequestDto
   * @return GetBuyerGroupChangeHistoryListResponseDto
   * @throws Exception
   */
  protected List<GetBuyerGroupChangeHistoryListResultVo> getBuyerGroupChangeHistoryList(GetBuyerGroupChangeHistoryListRequestDto getBuyerGroupChangeHistoryListRequestDto) throws Exception {

	  List<GetBuyerGroupChangeHistoryListResultVo> buyerGroupChangeHistoryList = buyerMapper.getBuyerGroupChangeHistoryList(getBuyerGroupChangeHistoryListRequestDto);

	  // 회원 그룹변경이력이 없는 경우 -> 현재 회원 등급의 정보 리턴
	  if(CollectionUtils.isEmpty(buyerGroupChangeHistoryList)) {
		  return buyerMapper.getBuyerGroupChangeHistoryListForNoChangeLog(getBuyerGroupChangeHistoryListRequestDto);
	  }

    return buyerGroupChangeHistoryList;
  }

  /**
   * 나를 추천한 추천인 리스트 조회
   *
   * @param getBuyerRecommendListRequestDto
   * @return GetBuyerRecommendListResponseDto
   * @throws Exception
   */
  protected List<GetBuyerRecommendListResultVo> getBuyerRecommendList(GetBuyerRecommendListRequestDto getBuyerRecommendListRequestDto) throws Exception {
    return buyerMapper.getBuyerRecommendList(getBuyerRecommendListRequestDto); // rows
  }

  /**
   * 회원 수정
   *
   * @param putBuyerRequestDto
   * @return PutBuyerResponseDto
   * @throws Exception
   */
  protected Map<String, Object> putBuyer(PutBuyerRequestDto putBuyerRequestDto) throws Exception {

    int cnt = buyerMapper.putBuyer(putBuyerRequestDto);

    if (StringUtils.isNotEmpty(putBuyerRequestDto.getChangeLogArray())) {
      List<Map> insertData = StringUtil.convertJsonToList(putBuyerRequestDto.getChangeLogArray());
      if (!insertData.isEmpty()) {
        AddBuyerChangeHististoryParamVo addBuyerChangeHistoryParamVo =
                                                                     AddBuyerChangeHististoryParamVo.builder()
                                                                                                    .adminId(putBuyerRequestDto.getUserVo()
                                                                                                                               .getUserId())
                                                                                                    .urUserId(putBuyerRequestDto.getUrUserId())
                                                                                                    .insertData(insertData)
                                                                                                    .DATABASE_ENCRYPTION_KEY(putBuyerRequestDto.getDATABASE_ENCRYPTION_KEY())
                                                                                                    .build();
        urCommonMapper.addBuyerChangeHististory(addBuyerChangeHistoryParamVo);
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("cnt", cnt);
    result.put("urUserId", putBuyerRequestDto.getUrUserId());

    return result;
  }

  /**
   * 휴면회원 단일조회
   *
   * @param getBuyerRequestDto
   * @return GetBuyerResponseDto
   * @throws Exception
   */
  protected GetBuyerResultVo getBuyerMove(GetBuyerRequestDto getBuyerRequestDto) throws Exception {
    return buyerMapper.getBuyerMove(getBuyerRequestDto);
  }

  /**
   * 전체회원 엑셀 다운로드 목록 조회
   *
   * @param getBuyerListRequestDto
   * @return ExcelDownloadDto
   * @throws Exception
   */
  protected ExcelDownloadDto getBuyerListExcelDownload(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {
    String excelFileName = "전체회원엑셀다운로드";
    String excelSheetName = "sheet";
    /* 화면값보다 20더 하면맞다. */
    Integer[] widthListOfFirstWorksheet = { 100, 100, 100, 110, 120, 120, 110, 80, 100, 110, 110, 80, 80, 80, 80, 100, 100 };

    String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center",
            "center", "center", "center", "center", "center", "center", "center", "center" };

    String[] propertyListOfFirstWorksheet = { "rnum", "employeeYn", "userName", "loginId", "mobile", "mail", "bdayView", "genderView",
            "createDate", "lastLoginDate", "marketingYn", "smsYn", "mailYn", "pushYn", "groupName", "status"};

    String[] firstHeaderListOfFirstWorksheet = { "No", "회원유형", "회원명", "회원ID", "휴대폰", "EMAIL", "생년월일(나이)", "성별", "가입일자", "최근방문일자",
            "마케팅 활용동의", "수신여부", "수신여부", "수신여부", "회원등급", "회원상태" };
    String[] secondtHeaderListOfFirstWorksheet = { "No", "회원유형", "회원명", "회원ID", "휴대폰", "EMAIL", "생년월일(나이)", "성별", "가입일자", "최근방문일자",
            "마케팅 활용동의", "SMS", "EMAIL", "PUSH", "회원등급", "회원상태" };

    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                           .workSheetName(excelSheetName)
                                                           .propertyList(propertyListOfFirstWorksheet)
                                                           .widthList(widthListOfFirstWorksheet)
                                                           .alignList(alignListOfFirstWorksheet)
                                                           .build();

    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);
    firstWorkSheetDto.setHeaderList(1, secondtHeaderListOfFirstWorksheet);

    List<GetBuyerListResultVo> buyerList = userBuyerBosBiz.getBuyerListExcel(getBuyerListRequestDto);

    firstWorkSheetDto.setExcelDataList(buyerList);

    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    return excelDownloadDto;
  }

  /**
   * 사용자 패스워드 초기화
   *
   * @param putPasswordClearRequestDto
   * @return PutPasswordClearResponseDto
   * @throws Exception
   */
  protected int putPasswordClear(PutPasswordClearRequestDto putPasswordClearRequestDto) throws Exception {
    int cnt = 0;

    String userLoginId = putPasswordClearRequestDto.getLoginId();
    String emailTemplateApi = "/admin/system/emailtmplt/getBuyerPassWordEmailTmplt?urUserId=";
    if(UserEnums.PasswordClearUserType.EMPLOYEE.getCode().equals(putPasswordClearRequestDto.getUserType())) {
    	emailTemplateApi = "/admin/system/emailtmplt/getBosPassWordEmailTmplt?urUserId=";
    }

    // 임시비밀번호 대소문자숫자 5자리
    String tempPassword = RandomStringUtils.randomAlphanumeric(5);
    putPasswordClearRequestDto.setPassword(SHA256Util.getUserPassword(tempPassword));

    // 기존비밀번호를 임시비밀번호로 수정
    cnt = buyerMapper.putPasswordClear(putPasswordClearRequestDto);
    // 비밀번호 실패 횟수 초기화
    cnt += buyerMapper.putCertificationFailCntClear(putPasswordClearRequestDto);

    // 임시 비밀번호 SMS,MAIL 발송
    if(StringUtils.isNotEmpty(putPasswordClearRequestDto.getPutPasswordType())) {
    	List<String> putPasswordTypeList = StringUtil.getArrayList(putPasswordClearRequestDto.getPutPasswordType());

    	HashMap<String,String> userInfoMap = new HashMap<>();
    	// 회원일때
        if(UserEnums.PasswordClearUserType.BUYER.getCode().equals(putPasswordClearRequestDto.getUserType())) {

        	// 회원 정보 조회
        	GetBuyerRequestDto getBuyerRequestDto = new GetBuyerRequestDto();
        	getBuyerRequestDto.setUrUserId(putPasswordClearRequestDto.getUrUserId());
        	GetBuyerResultVo buyervo = getBuyer(getBuyerRequestDto);
        	userLoginId = buyervo.getLoginId();

        	userInfoMap.put("urUserId", buyervo.getUrUserId());
        	userInfoMap.put("mobile", buyervo.getMobile());
        	userInfoMap.put("email", buyervo.getMail());

        // 임직원일때
        }else {
        	userInfoMap.put("urUserId", putPasswordClearRequestDto.getUrUserId());
        	userInfoMap.put("mobile", putPasswordClearRequestDto.getMobile());
        	userInfoMap.put("email", putPasswordClearRequestDto.getMail());
        }

    	for(String type : putPasswordTypeList) {

    		GetEmailSendResultVo getEmailSendResultVo = null;
    		//메일 sms 발송 확인 체크
    		if(UserEnums.PasswordClearUserType.BUYER.getCode().equals(putPasswordClearRequestDto.getUserType())) {
    			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BUYER_FIND_PASSWORD.getCode());
    			getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();
    		}else {
    			ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.BOS_FIND_PASSWORD.getCode());
    			getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();
    		}

        	//SMS 발송
    		if(type.equals("SMS") && "Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    			LoginResultVo loginResultVo = new LoginResultVo();
    			loginResultVo.setLoginId(userLoginId);
    			loginResultVo.setPassword(tempPassword);

    			String content = comnBiz.getSMSTmplt(getEmailSendResultVo, loginResultVo);
    			String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
    			String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    			AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
    	                .content(content)
    	                .urUserId(userInfoMap.get("urUserId"))
    	                .mobile(userInfoMap.get("mobile"))
    	                .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    	                .reserveYn(reserveYn)
    	                .build();

    			sendTemplateBiz.sendSmsApi(addSmsIssueSelectRequestDto);
    		}

    		//EMAIL 발송
    		if(type.equals("EMAIL") && "Y".equals(getEmailSendResultVo.getMailSendYn())) {

            	//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
        		String content = sendTemplateBiz.getDomainManagement() + emailTemplateApi + putPasswordClearRequestDto.getUrUserId()+"&userPassword="+tempPassword;
            	String title = getEmailSendResultVo.getMailTitle();
                String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

                AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                        .reserveYn(reserveYn)
                        .content(content)
                        .title(title)
                        .urUserId(userInfoMap.get("urUserId"))
                        .mail(userInfoMap.get("email"))
                        .build();

                sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    		}
    	}
    }


    return cnt;
  }

  /**
   * 개인정보 변경 이력 목록 조회
   *
   * @param getUserChangeHistoryListRequestDto
   * @return GetUserChangeHistoryListResponseDto
   * @throws Exception
   */
  protected GetUserChangeHistoryListResponseDto getUserChangeHistoryList(GetUserChangeHistoryListRequestDto getUserChangeHistoryListRequestDto) throws Exception {

    if (StringUtils.isNotEmpty(getUserChangeHistoryListRequestDto.getCondiValue())) {
      ArrayList<String> array = new ArrayList<>();
      StringTokenizer st = new StringTokenizer(getUserChangeHistoryListRequestDto.getCondiValue(), "\n|,");
      while (st.hasMoreElements()) {
        String object = (String) st.nextElement();
        array.add(object);
      }
      getUserChangeHistoryListRequestDto.setCondiValueArray(array);
    }

    // int total =
    // buyerMapper.getUserChangeHistoryListCount(getUserChangeHistoryListRequestDto);
    // // total
    PageMethod.startPage(getUserChangeHistoryListRequestDto.getPage(), getUserChangeHistoryListRequestDto.getPageSize());
    Page<GetUserChangeHistoryResultVo> rows = buyerMapper.getUserChangeHistoryList(getUserChangeHistoryListRequestDto); // rows

    return GetUserChangeHistoryListResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
  }

  /**
   * 악성 클레임 회원 목록 조회
   *
   * @param getUserMaliciousClaimHistoryListRequestDto
   * @return GetUserMaliciousClaimHistoryListResponseDto
   * @throws Exception
   */
  protected List<GetUserMaliciousClaimHistoryListResultVo> getUserMaliciousClaimHistoryList(GetUserMaliciousClaimHistoryListRequestDto getUserMaliciousClaimHistoryListRequestDto) throws Exception {

    return buyerMapper.getUserMaliciousClaimHistoryList(getUserMaliciousClaimHistoryListRequestDto);
  }

  /**
	 * 회원별 적립금 정보 조회
	 *
	 * @param getBuyerListRequestDto
	 * @return GetBuyerListResponseDto
	 * @throws Exception
	 */
	protected Page<GetPointInfoVo> getPointInfo(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception {
		Page<GetPointInfoVo> res = buyerMapper.getPointInfo(getBuyerListRequestDto);
		for (GetPointInfoVo vo : res.getResult()) {
			// 적립 상세 내역
            if(vo.getPointProcessType() != null){
                String displayName = PointProcessType.findByCode(vo.getPointProcessType()).getDisplayName();
                vo.setPointTypeName(displayName);
            }
		}
		return res;
	}

	/**
	 * 잔여 적립금 조회
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	protected int getUserAvailablePoints(GetBuyerListRequestDto dto) throws Exception{

		int availablePoint = 0;
		if(dto.getUrUserId() != null && !dto.getUrUserId().isEmpty() && dto.getUrUserId() != "") {
			availablePoint = pointUseMapper.getUserAvailablePoints(Long.parseLong(dto.getUrUserId()));
		}

		return availablePoint;
	}
}

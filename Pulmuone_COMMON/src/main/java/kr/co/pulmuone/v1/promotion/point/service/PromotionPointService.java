package kr.co.pulmuone.v1.promotion.point.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthInfoVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums.PointMasterStatus;
import kr.co.pulmuone.v1.comm.enums.PointEnums.PointProcessType;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.exception.BosCustomException;
import kr.co.pulmuone.v1.comm.mapper.approval.auth.ApprovalAuthMapper;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PointUseMapper;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PromotionPointMapper;
import kr.co.pulmuone.v1.comm.mapper.user.join.UserJoinMapper;
import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.point.dto.*;
import kr.co.pulmuone.v1.promotion.point.dto.vo.*;
import kr.co.pulmuone.v1.user.join.dto.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionPointService {

	private final PolicyConfigBiz policyConfigBiz;
	private final PromotionPointMapper promotionPointMapper;
	private final ApprovalAuthMapper approvalAuthMapper;
	private final UserJoinMapper userJoinMapper;
	private final PointUseMapper pointUseMapper;
	private static final int APPROVE_POINT_BY_SYSTEM_AMOUNT = 10000;
	private static final int PC_MESSAGE_LENGTH = 20;
    private static final int MOBILE_MESSAGE_LENGTH = 11;

	@Autowired
    private PointBiz pointBiz;



    /**
     * 적립금 설정 조회
     *
     * @param pointSettingListRequestDto
     * @return PointSettingListResponseDto
     * @throws Exception
     */
    protected Page<PointSettingResultVo> getPointSettingList(PointSettingListRequestDto pointSettingListRequestDto) throws Exception {
    	PageMethod.startPage(pointSettingListRequestDto.getPage(), pointSettingListRequestDto.getPageSize());
        return promotionPointMapper.getPointSettingList(pointSettingListRequestDto);
    }


    /**
     * 적립금 설정 상세 조회
     *
     * @param pointRequestDto PointRequestDto
     * @return PointSettingListResponseDto
     * @throws Exception
     */
    protected PointResponseDto getPointDetail(PointRequestDto pointRequestDto) throws Exception {
    	PointResponseDto result = new PointResponseDto();
    	PointSettingResultVo vo = new PointSettingResultVo();

    	// 적립금 상세 정보 조회
		vo = promotionPointMapper.getPointDetail(pointRequestDto.getPmPointId());

		//난수번호 리스트
		List<AccountInfoVo> serialNumberList = this.getSerialNumberList(pointRequestDto);
		if(!serialNumberList.isEmpty()) {
			vo.setSerialNumberList(serialNumberList);
		}

		//회원정보 리스트
		List<AccountInfoVo> userList = promotionPointMapper.getUserList(pointRequestDto);
		if(!userList.isEmpty()) {
			vo.setUserList(userList);
		}

		//회원등급별 리스트
		List<PointUserGradeVo> userGradeList = promotionPointMapper.getUserGradeList(pointRequestDto);
		if(!userGradeList.isEmpty()) {
			vo.setUserGradeList(userGradeList);
		}

		if(vo.getApprUserId() != null) {
			pointRequestDto.setApprKindType(ApprovalAuthType.APPR_KIND_TP_POINT.getCode());
			pointRequestDto.setApprSubUserId(vo.getApprSubUserId());
			pointRequestDto.setApprUserId(vo.getApprUserId());
			// 승인관리자 정보 조회
			List<ApprovalAuthManagerVo> apprUserList = promotionPointMapper.getApprUserList(pointRequestDto);
			if (!apprUserList.isEmpty()) {
				vo.setApprUserList(apprUserList);
			}
		}

		// 로그인한 관리자 롤 ID 리스트
		List<String> roleIds = SessionUtil.getBosUserVO().getListRoleId();
		vo.setListRoleId(roleIds);

		// 마스터 롤 ID
		vo.setMasterRoleId(policyConfigBiz.getConfigValue(Constants.POINT_MASTER_ROLL_IDS));

		// 적립금 지급 정보
		List<PointPayInfoVo> pointPayInfoList = promotionPointMapper.getPointPayInfo(pointRequestDto.getPmPointId());
		vo.setPointPayInfoList(pointPayInfoList);

        result.setRows(vo);
        return result;
    }

    /**
     *
     * @param pointRequestDto
     * @return
     * @throws Exception
     */
    protected List<AccountInfoVo> getSerialNumberList(PointRequestDto pointRequestDto) throws Exception {

    	List<AccountInfoVo> serialNumberList = promotionPointMapper.getSerialNumberList(pointRequestDto);

    	return serialNumberList;
    }


    /**
     *
     * @param uploadInfoVo
     * @return
     * @throws Exception
     */
    protected int getDuplicateSerialNumber(UploadInfoVo uploadInfoVo) throws Exception{

    	int seralCount = promotionPointMapper.getDuplicateSerialNumber(uploadInfoVo);
    	return seralCount;
    }

    /**
     * 적립금 설정 저장
     *
     * @param pointSettingMgmRequestDto PointSettingMgmRequestDto
     * @return int
     * @throws 	Exception
     */
    protected ApiResult<?> addPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto)throws Exception {

		//포인트 등록 승인 요청처리 상태 설정 (승인요청, 저장, 자동승인)
		//관리자 지급/차감 아닐때만 적립급 컬럼으로 승인코드 세팅
		if (!pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {
			pointSettingMgmRequestDto = this.determinAddPointApprovalStatus(pointSettingMgmRequestDto);
		}

		if(!pointSettingMgmRequestDto.getUploadUser().isEmpty()) {
			pointSettingMgmRequestDto.setUploadUserList((List<UploadInfoVo>) BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUploadUser(), UploadInfoVo.class));
		}

		// 이용권 단일코드 체크
		if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.SERIAL_NUMBER.getCode()) &&
				pointSettingMgmRequestDto.getSerialNumberType().equals(PointEnums.SerialNumberType.FIXED_VALUE.getCode())) {

			int fixSeralCount = promotionPointMapper.getDuplicateFixedNumber(pointSettingMgmRequestDto);

			if (fixSeralCount > 0) {  // 단일코드 중복 체크
				throw new BosCustomException(PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getCode(), PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getMessage());
			}
		}

		// 적립금 등록 (관리자 지급/차감)
		if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {

			//업로드 리스트
			List<UploadInfoVo> list = pointSettingMgmRequestDto.getUploadUserList() ;
			Map<String, List<UploadInfoVo>> issueUserList = null;

			if ( pointSettingMgmRequestDto.getIssueValue() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getIssueValue())
					&& !PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				if (Integer.parseInt(pointSettingMgmRequestDto.getIssueValue()) > 0) {
					//적립금 입력필드
					issueUserList = new HashMap<>();
					issueUserList.put(pointSettingMgmRequestDto.getIssueValue(), list);
				}
			} else {

				// 엑셀 변경 없이 수정하는 경우 UploadIssueValue 값 null
				if(list.size() > 0 && list.get(0).getUploadIssueValue() == null) {
					for(int i = 0; i < list.size(); i++) {
						list.get(i).setUploadIssueValue(String.valueOf(list.get(i).getIssueVal()));
					}
				}
				//엑셀 업로드 적립금액
				issueUserList = list.stream().collect(Collectors.groupingBy(UploadInfoVo::getUploadIssueValue, LinkedHashMap::new, Collectors.toList()));
			}
			Set<String> issueValues = issueUserList.keySet();

			String grPmPointId = "GR"+System.currentTimeMillis();
			pointSettingMgmRequestDto = this.determinAddPointApprovalStatus(pointSettingMgmRequestDto);

			for (String issueValue : issueValues ) {
				if (!issueValue.isEmpty()) {
					//업로드된 회원의 설정 적립금 으로 issueValue 에 세팅
					pointSettingMgmRequestDto.setIssueValue(issueValue);
				}
				pointSettingMgmRequestDto.setIssueStartDate(null);
				pointSettingMgmRequestDto.setIssueEndDate(null);

				List<UploadInfoVo> accountUserPointList = pointSettingMgmRequestDto.getUploadUserList();

				// 포인트 체크 (적립구분 : 차감)
				if (pointSettingMgmRequestDto.getPointPaymentType().equals(PointEnums.PointPayment.DEDUCTION.getCode())) {


					int lackUserCount = 0;
					String userName = "";
					String checkPointMessage = "";


					if (accountUserPointList != null) {

						for (int i = 0; i < accountUserPointList.size(); i++) {
							//		    			if("Y".equals(pointSettingMgmRequestDto.getPointAdmin())) {
							//		    				pointSettingMgmRequestDto.setUrUserId(accountUserPointList.get(i).getUrUserId());
							//		    			}else {
							//		    				pointSettingMgmRequestDto.setLoginId(accountUserPointList.get(i).getLoginId());
							//		    			}

							//pointSettingMgmRequestDto.setUrUserId(accountUserPointList.get(i).getUrUserId());
							pointSettingMgmRequestDto.setLoginId(accountUserPointList.get(i).getLoginId());

							//urUserId
							UploadInfoVo vo = promotionPointMapper.getUserPoint(pointSettingMgmRequestDto);

							if (Integer.parseInt(pointSettingMgmRequestDto.getIssueValue()) > vo.getAmount()) {
								lackUserCount++;
								userName = vo.getUserName() + " ";
							}
						}

						if (lackUserCount == 1) {
							checkPointMessage = userName;
							throw new BosCustomException(PointEnums.AdminPointCheck.USER_POINT_LACK.getCode(), checkPointMessage + PointEnums.AdminPointCheck.USER_POINT_LACK.getMessage());
						} else if (lackUserCount > 1) {
							lackUserCount--;
							checkPointMessage = userName + " 외" + lackUserCount + " 명";
							throw new BosCustomException(PointEnums.AdminPointCheck.GROUP_POINT_LACK.getCode(), checkPointMessage + PointEnums.AdminPointCheck.GROUP_POINT_LACK.getMessage());
						}

						pointSettingMgmRequestDto.setUrUserId("");

					}

				}

				// 엑셀 업로드 회원 ID 체크
				int userIdCnt = 0;
				for (int i = 0; i < issueUserList.get(issueValue).size(); i++) {
					if (issueUserList.get(issueValue).get(i).getUrUserId() == null) {
						pointSettingMgmRequestDto.setLoginId(issueUserList.get(issueValue).get(i).getLoginId());
						int checkCnt = promotionPointMapper.getUserIdCnt(pointSettingMgmRequestDto);
						if (checkCnt > 0) {
							userIdCnt++;
						}
					} else {
						userIdCnt++;
					}
				}

				if (userIdCnt != issueUserList.get(issueValue).size()) {
					throw new BosCustomException(PointEnums.AdminPointCheck.USER_ID_FAIL.getCode(), PointEnums.AdminPointCheck.USER_ID_FAIL.getMessage());
				}

				// 유효일 설정
				pointSettingMgmRequestDto.setValidityDay(pointSettingMgmRequestDto.getPointPaymentAmount());


				// 적립금 등록 (관리자 지급/차감) :관리자 지급 유효기간 확인
				String loginId = SessionUtil.getBosUserVO().getUserId();
				PointSettingResultVo pointSettingResultVo = promotionPointMapper.getGroupValidityDay(loginId);
				if (pointSettingResultVo != null && PointEnums.PointPayment.PROVISION.getCode().equals(pointSettingMgmRequestDto.getPointPaymentType())) {
					if (Integer.parseInt(pointSettingMgmRequestDto.getValidityDay()) > Integer.parseInt(pointSettingResultVo.getValidityDay())) {
						throw new BosCustomException(PointEnums.AdminPointCheck.INVALID_VALIDITY_DAY_FAIL.getCode(), PointEnums.AdminPointCheck.INVALID_VALIDITY_DAY_FAIL.getMessage());
					}
				}

				// 기간 종료일 시간
				if (pointSettingMgmRequestDto.getIssueEndDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getIssueEndDate())) {
					pointSettingMgmRequestDto.setIssueEndDate(pointSettingMgmRequestDto.getIssueEndDate() + "235959");
				}

				// 유효기간 : 기간설정
				if (pointSettingMgmRequestDto.getValidityDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getValidityDate())) {
					pointSettingMgmRequestDto.setValidityDate(pointSettingMgmRequestDto.getValidityDate() + "235959");
				}


				// 후기등록
				if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.FEEDBACK.getCode())) {
					// 유효일 설정
					pointSettingMgmRequestDto.setValidityDay(pointSettingMgmRequestDto.getFeedbackValidityDay());
				}

				// 엑셀 대량 지급
				if(PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
					pointSettingMgmRequestDto.setPayMethodType(pointSettingMgmRequestDto.getPayMethodType());
					pointSettingMgmRequestDto.setGrPmPointId(grPmPointId);
				}

				// 적립금 등록
				promotionPointMapper.addPointSetting(pointSettingMgmRequestDto);

				// 분담정보 등록
				promotionPointMapper.addOrganization(pointSettingMgmRequestDto);


				// 적립금 등록 (관리자 지급/차감)
				// 회원설정 정보 등록
				if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {

					PointSettingMgmRequestDto pointUserParamDto = new PointSettingMgmRequestDto();
					pointUserParamDto.setPmPointId(pointSettingMgmRequestDto.getPmPointId());

					for(UploadInfoVo accountUserList: issueUserList.get(issueValue)){

						if (accountUserList != null) {

							pointUserParamDto.setIssueValue(issueValue);
							pointUserParamDto.setPointPaymentType(PointEnums.PointPayment.PROVISION.getCode());

							if ( accountUserList.getUrUserId() != null) {
								pointUserParamDto.setUrUserId( accountUserList.getUrUserId() );
							} else {
								pointUserParamDto.setLoginId( accountUserList.getLoginId() );
							}

							// 적립금_발급정보 등록
							promotionPointMapper.addBosPointIssue(pointUserParamDto);

						}
					}
				}


				if (!pointSettingMgmRequestDto.getUploadTicket().isEmpty()) {
					pointSettingMgmRequestDto.setUploadTicketList((List<UploadInfoVo>) BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUploadTicket(), UploadInfoVo.class));
				}

				if (pointSettingMgmRequestDto.getUserGradeList() != null) {
					pointSettingMgmRequestDto.setPointUserGradeVoList(BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUserGradeList(), PointUserGradeVo.class));

					List<PointUserGradeVo> pointUserGradeList = pointSettingMgmRequestDto.getPointUserGradeVoList();
					// 적립금 등록 (후기)
					if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.FEEDBACK.getCode())) {
						if (pointUserGradeList != null && !pointUserGradeList.isEmpty()) {
							for (int i = 0; i < pointUserGradeList.size(); i++) {
								PointUserGradeVo vo = pointUserGradeList.get(i);
								vo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
								pointUserGradeList.get(i).setPmPointId(pointSettingMgmRequestDto.getPmPointId());
								pointUserGradeList.get(i).setReviewType(PointEnums.PointUsergradeType.USER_GRADE.getCode());
								if (pointSettingMgmRequestDto.getUrUserId() == null) {
									pointUserGradeList.get(i).setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
								} else {
									pointUserGradeList.get(i).setUserId(pointSettingMgmRequestDto.getUrUserId());
								}
							}

							PointUserGradeVo feedbackPointVo = new PointUserGradeVo();
							feedbackPointVo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
							feedbackPointVo.setReviewType(PointEnums.PointUsergradeType.NORMAL.getCode());
							feedbackPointVo.setNormalAmount(Integer.parseInt(pointSettingMgmRequestDto.getNormalAmount()));
							feedbackPointVo.setPhotoAmount(Integer.parseInt(pointSettingMgmRequestDto.getPhotoAmount()));
							feedbackPointVo.setPremiumAmount(Integer.parseInt(pointSettingMgmRequestDto.getPremiumAmount()));

							if (pointSettingMgmRequestDto.getUrUserId() == null) {
								feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
							} else {
								feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUrUserId());
							}
							// 후기 적립금 추가
							pointUserGradeList.add(feedbackPointVo);

							// 적립금 적용범위 등록
							promotionPointMapper.addPointUserGrade(pointUserGradeList);
						} else {

							PointUserGradeVo feedbackPointVo = new PointUserGradeVo();
							feedbackPointVo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
							feedbackPointVo.setReviewType(PointEnums.PointUsergradeType.NORMAL.getCode());
							feedbackPointVo.setNormalAmount(Integer.parseInt(pointSettingMgmRequestDto.getNormalAmount()));
							feedbackPointVo.setPhotoAmount(Integer.parseInt(pointSettingMgmRequestDto.getPhotoAmount()));
							feedbackPointVo.setPremiumAmount(Integer.parseInt(pointSettingMgmRequestDto.getPremiumAmount()));

							if (pointSettingMgmRequestDto.getUrUserId() == null) {
								feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
							} else {
								feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUrUserId());
							}

							// 후기 적립금 추가
							pointUserGradeList.add(feedbackPointVo);

							// 적립금 적용범위 등록
							promotionPointMapper.addPointUserGrade(pointUserGradeList);

						}
					}
				}

				// 난수생성 코드 설정
				if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.SERIAL_NUMBER.getCode())) {
					if (!pointSettingMgmRequestDto.getSerialNumberType().equals(PointEnums.SerialNumberType.FIXED_VALUE.getCode())) {

						pointSettingMgmRequestDto.setSerialNumberUseType(PointEnums.SerialNumberUseType.POINT.getCode());        // 사용타입 : 적립금

						pointSettingMgmRequestDto.setSerialNumberStatus(PointEnums.SerialNumberStatus.ISSUED.getCode());            // 이용권 상태: 발급

						if (pointSettingMgmRequestDto.getRandNumTypeSelect().equals(PointEnums.SerialNumberType.AUTO_CREATE.getCode())) {  // 자동생성

							if (pointSettingMgmRequestDto.getIssueQty() > 0) {
								for (int i = 0; i < pointSettingMgmRequestDto.getIssueQty(); i++) {

									// 개별난수번호 등록  (등록시 난수 생성  function 호출)
									promotionPointMapper.addSerialNumber(pointSettingMgmRequestDto);
								}
							}

						} else if (pointSettingMgmRequestDto.getRandNumTypeSelect().equals(PointEnums.SerialNumberType.EXCEL_UPLOAD.getCode())) {  // 엑셀업로드
							List<UploadInfoVo> accountTicketList = pointSettingMgmRequestDto.getUploadTicketList();
							for (int i = 0; i < accountTicketList.size(); i++) {
								UploadInfoVo vo = new UploadInfoVo();
								vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
								pointSettingMgmRequestDto.setFixSerialNumber(vo.getSerialNumber());
								int seralCount = this.getDuplicateSerialNumber(vo);
								if (seralCount > 0) {
									throw new BosCustomException(PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getCode(), PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getMessage());
								} else {

									// 개별난수번호 등록  (엑셀에서 저장된 난수로 등록)
									promotionPointMapper.addSerialNumber(pointSettingMgmRequestDto);
								}
							}

						}
					}
				}

				ApprovalStatusVo history = ApprovalStatusVo.builder()
						.taskPk(pointSettingMgmRequestDto.getPmPointId())
						.apprUserId(pointSettingMgmRequestDto.getApprUserId())
						.apprSubUserId(pointSettingMgmRequestDto.getApprSubUserId())
						.approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
						.masterStat(pointSettingMgmRequestDto.getPointMasterStat())
						.apprStat(pointSettingMgmRequestDto.getApprStat())
						.build();

				// 적립금상태이력 등록
				this.addPointStatusHistory(history);
			}
		} else {
			//관리자 지급/차감 아닐때

			// 기간 종료일 시간
			if (pointSettingMgmRequestDto.getIssueEndDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getIssueEndDate())) {
				pointSettingMgmRequestDto.setIssueEndDate(pointSettingMgmRequestDto.getIssueEndDate() + "235959");
			}

			// 유효기간 : 기간설정
			if (pointSettingMgmRequestDto.getValidityDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getValidityDate())) {
				pointSettingMgmRequestDto.setValidityDate(pointSettingMgmRequestDto.getValidityDate() + "235959");
			}


			// 후기등록
			if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.FEEDBACK.getCode())) {
				// 유효일 설정
				pointSettingMgmRequestDto.setValidityDay(pointSettingMgmRequestDto.getFeedbackValidityDay());
			}


			// 적립금 등록
			promotionPointMapper.addPointSetting(pointSettingMgmRequestDto);

			// 분담정보 등록
			promotionPointMapper.addOrganization(pointSettingMgmRequestDto);


			// 적립금 등록 (관리자 지급/차감)
			// 회원설정 정보 등록
			if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {

				List<UploadInfoVo> list = pointSettingMgmRequestDto.getUploadUserList() ;
				Map<String, List<UploadInfoVo>> issueUserList = null;

				if ( pointSettingMgmRequestDto.getIssueValue() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getIssueValue()) ) {
					if (Integer.parseInt(pointSettingMgmRequestDto.getIssueValue()) > 0) {
						//적립금 입력필드
						issueUserList = new HashMap<>();
						issueUserList.put(pointSettingMgmRequestDto.getIssueValue(), list);
					}
				} else {
					//엑셀 업로드 적립금액
					issueUserList = list.stream().collect(Collectors.groupingBy(UploadInfoVo::getUploadIssueValue, LinkedHashMap::new, Collectors.toList()));
				}
				Set<String> issueValues = issueUserList.keySet();

				for (String issueValue : issueValues ) {
					PointSettingMgmRequestDto pointUserParamDto = new PointSettingMgmRequestDto();
					pointUserParamDto.setPmPointId(pointSettingMgmRequestDto.getPmPointId());

					for(UploadInfoVo accountUserList: issueUserList.get(issueValue)){

						if (accountUserList != null) {

							pointUserParamDto.setIssueValue(issueValue);
							pointUserParamDto.setPointPaymentType(PointEnums.PointPayment.PROVISION.getCode());

							if ( accountUserList.getUrUserId() != null) {
								pointUserParamDto.setUrUserId( accountUserList.getUrUserId() );
							} else {
								pointUserParamDto.setLoginId( accountUserList.getLoginId() );
							}

							// 적립금_발급정보 등록
							promotionPointMapper.addBosPointIssue(pointUserParamDto);

						}
					}
				}
			}


			if (!pointSettingMgmRequestDto.getUploadTicket().isEmpty()) {
				pointSettingMgmRequestDto.setUploadTicketList((List<UploadInfoVo>) BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUploadTicket(), UploadInfoVo.class));
			}

			if (pointSettingMgmRequestDto.getUserGradeList() != null) {
				pointSettingMgmRequestDto.setPointUserGradeVoList(BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUserGradeList(), PointUserGradeVo.class));

				List<PointUserGradeVo> pointUserGradeList = pointSettingMgmRequestDto.getPointUserGradeVoList();
				// 적립금 등록 (후기)
				if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.FEEDBACK.getCode())) {
					if (pointUserGradeList != null && !pointUserGradeList.isEmpty()) {
						for (int i = 0; i < pointUserGradeList.size(); i++) {
							PointUserGradeVo vo = pointUserGradeList.get(i);
							vo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
							pointUserGradeList.get(i).setPmPointId(pointSettingMgmRequestDto.getPmPointId());
							pointUserGradeList.get(i).setReviewType(PointEnums.PointUsergradeType.USER_GRADE.getCode());
							if (pointSettingMgmRequestDto.getUrUserId() == null) {
								pointUserGradeList.get(i).setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
							} else {
								pointUserGradeList.get(i).setUserId(pointSettingMgmRequestDto.getUrUserId());
							}
						}

						PointUserGradeVo feedbackPointVo = new PointUserGradeVo();
						feedbackPointVo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
						feedbackPointVo.setReviewType(PointEnums.PointUsergradeType.NORMAL.getCode());
						feedbackPointVo.setNormalAmount(Integer.parseInt(pointSettingMgmRequestDto.getNormalAmount()));
						feedbackPointVo.setPhotoAmount(Integer.parseInt(pointSettingMgmRequestDto.getPhotoAmount()));
						feedbackPointVo.setPremiumAmount(Integer.parseInt(pointSettingMgmRequestDto.getPremiumAmount()));

						if (pointSettingMgmRequestDto.getUrUserId() == null) {
							feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
						} else {
							feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUrUserId());
						}
						// 후기 적립금 추가
						pointUserGradeList.add(feedbackPointVo);

						// 적립금 적용범위 등록
						promotionPointMapper.addPointUserGrade(pointUserGradeList);
					} else {

						PointUserGradeVo feedbackPointVo = new PointUserGradeVo();
						feedbackPointVo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
						feedbackPointVo.setReviewType(PointEnums.PointUsergradeType.NORMAL.getCode());
						feedbackPointVo.setNormalAmount(Integer.parseInt(pointSettingMgmRequestDto.getNormalAmount()));
						feedbackPointVo.setPhotoAmount(Integer.parseInt(pointSettingMgmRequestDto.getPhotoAmount()));
						feedbackPointVo.setPremiumAmount(Integer.parseInt(pointSettingMgmRequestDto.getPremiumAmount()));

						if (pointSettingMgmRequestDto.getUrUserId() == null) {
							feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
						} else {
							feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUrUserId());
						}

						// 후기 적립금 추가
						pointUserGradeList.add(feedbackPointVo);

						// 적립금 적용범위 등록
						promotionPointMapper.addPointUserGrade(pointUserGradeList);

					}
				}
			}

			// 난수생성 코드 설정
			if (pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.SERIAL_NUMBER.getCode())) {
				if (!pointSettingMgmRequestDto.getSerialNumberType().equals(PointEnums.SerialNumberType.FIXED_VALUE.getCode())) {

					pointSettingMgmRequestDto.setSerialNumberUseType(PointEnums.SerialNumberUseType.POINT.getCode());        // 사용타입 : 적립금

					pointSettingMgmRequestDto.setSerialNumberStatus(PointEnums.SerialNumberStatus.ISSUED.getCode());            // 이용권 상태: 발급

					if (pointSettingMgmRequestDto.getRandNumTypeSelect().equals(PointEnums.SerialNumberType.AUTO_CREATE.getCode())) {  // 자동생성

						if (pointSettingMgmRequestDto.getIssueQty() > 0) {
							for (int i = 0; i < pointSettingMgmRequestDto.getIssueQty(); i++) {

								// 개별난수번호 등록  (등록시 난수 생성  function 호출)
								promotionPointMapper.addSerialNumber(pointSettingMgmRequestDto);
							}
						}

					} else if (pointSettingMgmRequestDto.getRandNumTypeSelect().equals(PointEnums.SerialNumberType.EXCEL_UPLOAD.getCode())) {  // 엑셀업로드
						List<UploadInfoVo> accountTicketList = pointSettingMgmRequestDto.getUploadTicketList();
						for (int i = 0; i < accountTicketList.size(); i++) {
							UploadInfoVo vo = new UploadInfoVo();
							vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
							pointSettingMgmRequestDto.setFixSerialNumber(vo.getSerialNumber());
							int seralCount = this.getDuplicateSerialNumber(vo);
							if (seralCount > 0) {
								throw new BosCustomException(PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getCode(), PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getMessage());
							} else {

								// 개별난수번호 등록  (엑셀에서 저장된 난수로 등록)
								promotionPointMapper.addSerialNumber(pointSettingMgmRequestDto);
							}
						}

					}
				}
			}

			ApprovalStatusVo history = ApprovalStatusVo.builder()
					.taskPk(pointSettingMgmRequestDto.getPmPointId())
					.apprUserId(pointSettingMgmRequestDto.getApprUserId())
					.apprSubUserId(pointSettingMgmRequestDto.getApprSubUserId())
					.approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
					.masterStat(pointSettingMgmRequestDto.getPointMasterStat())
					.apprStat(pointSettingMgmRequestDto.getApprStat())
					.build();

			// 적립금상태이력 등록
			this.addPointStatusHistory(history);
		}

		return ApiResult.success(pointSettingMgmRequestDto);
    }

    /**
     * 적립금 설정 수정
     *
     * @param pointSettingMgmRequestDto PointSettingMgmRequestDto
     * @return int
     * @throws 	Exception
     */
    protected ApiResult<?> putPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto)throws Exception {

		//포인트 수정 승인 상태값 세팅
		pointSettingMgmRequestDto = this.determinPutPointApprovalStatus(pointSettingMgmRequestDto);

		// 적립금 등록 (관리자 지급/차감)
		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {
			pointSettingMgmRequestDto.setIssueStartDate(null);
			pointSettingMgmRequestDto.setIssueEndDate(null);

			// 유효일 설정
			pointSettingMgmRequestDto.setValidityDay(pointSettingMgmRequestDto.getPointPaymentAmount());
		}

		// 후기등록
		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.FEEDBACK.getCode())) {
			// 유효일 설정
			pointSettingMgmRequestDto.setValidityDay(pointSettingMgmRequestDto.getFeedbackValidityDay());
		}


		// 기간 종료일 시간
		if(pointSettingMgmRequestDto.getIssueEndDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getIssueEndDate())) {
			pointSettingMgmRequestDto.setIssueEndDate(pointSettingMgmRequestDto.getIssueEndDate() + "235959");
		}

		// 유효기간 : 기간설정
		if(pointSettingMgmRequestDto.getValidityDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getValidityDate())) {
			pointSettingMgmRequestDto.setValidityDate(pointSettingMgmRequestDto.getValidityDate() + "235959");
		}


		// 이용권 단일코드 체크
		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.SERIAL_NUMBER.getCode()) &&
				pointSettingMgmRequestDto.getSerialNumberType().equals(PointEnums.SerialNumberType.FIXED_VALUE.getCode())) {
			int fixSeralCount = promotionPointMapper.getDuplicateFixedNumber(pointSettingMgmRequestDto);

			if(fixSeralCount > 0) {  // 단일코드 중복 체크
				throw new BosCustomException(PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getCode(), PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getMessage());
			}
		}

		if(!pointSettingMgmRequestDto.getUploadUser().isEmpty()) {
			pointSettingMgmRequestDto.setUploadUserList((List<UploadInfoVo>) BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUploadUser(), UploadInfoVo.class));
		}


		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {

			// 적립금 등록 (관리자 지급/차감) :엑셀 업로드 회원ID 확인  Start
			List<UploadInfoVo> accountUserPointList = pointSettingMgmRequestDto.getUploadUserList();
			// 엑셀 업로드 회원 ID 체크
			if(accountUserPointList != null) {
				int userIdCnt = 0;
				for(int i=0 ; i<accountUserPointList.size() ; i++) {
					if(accountUserPointList.get(i).getUrUserId() == null) {
						pointSettingMgmRequestDto.setLoginId(accountUserPointList.get(i).getLoginId());
						int checkCnt = promotionPointMapper.getUserIdCnt(pointSettingMgmRequestDto);
						if(checkCnt > 0) {
							userIdCnt++;
						}
					}else {
						userIdCnt++;
					}
				}

				if(userIdCnt != accountUserPointList.size()) {
					throw new BosCustomException(PointEnums.AdminPointCheck.USER_ID_FAIL.getCode(), PointEnums.AdminPointCheck.USER_ID_FAIL.getMessage());
				}
			}

			// 적립금 등록 (관리자 지급/차감) :엑셀 업로드 회원ID 확인  End

			// 적립금 등록 (관리자 지급/차감) :관리자 지급 유효기간 확인
			String loginId = SessionUtil.getBosUserVO().getUserId();
			PointSettingResultVo pointSettingResultVo = promotionPointMapper.getGroupValidityDay(loginId);
			if(pointSettingResultVo != null && PointEnums.PointPayment.PROVISION.getCode().equals(pointSettingMgmRequestDto.getPointPaymentType())) {
				if(Integer.parseInt(pointSettingMgmRequestDto.getValidityDay()) > Integer.parseInt(pointSettingResultVo.getValidityDay())) {
					throw new BosCustomException(PointEnums.AdminPointCheck.INVALID_VALIDITY_DAY_FAIL.getCode(), PointEnums.AdminPointCheck.INVALID_VALIDITY_DAY_FAIL.getMessage());
				}
			}
		}

		promotionPointMapper.putPointSetting(pointSettingMgmRequestDto);

		// 분담정보 삭제
		promotionPointMapper.removeOrganization(pointSettingMgmRequestDto);

		// 분담정보 등록
		promotionPointMapper.addOrganization(pointSettingMgmRequestDto);

		// 적립금 등록 (관리자 지급/차감)
		// 회원설정 정보 등록
		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {
			PointSettingMgmRequestDto pointUserParamDto = new PointSettingMgmRequestDto();
			pointUserParamDto.setPmPointId(pointSettingMgmRequestDto.getPmPointId());

			// 엑셀 대량 건은 761라인에서 처리
			if(!PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				// 적립금_발급정보 삭제
				promotionPointMapper.remomveBosPointIssue(pointUserParamDto);
				List<UploadInfoVo> accountUserList = pointSettingMgmRequestDto.getUploadUserList();

				if(accountUserList != null && !accountUserList.isEmpty()) {
					pointUserParamDto.setIssueValue(pointSettingMgmRequestDto.getIssueValue());
					pointUserParamDto.setPointPaymentType(PointEnums.PointPayment.PROVISION.getCode());
					for(int i=0;i<accountUserList.size();i++) {
						if(accountUserList.get(i).getUrUserId() != null ) {
							pointUserParamDto.setUrUserId(accountUserList.get(i).getUrUserId());
						}else {
							pointUserParamDto.setLoginId(accountUserList.get(i).getLoginId());
						}
						// 적립금_발급정보 등록
						promotionPointMapper.addBosPointIssue(pointUserParamDto);

					}
				}
			}
		}


		if(!pointSettingMgmRequestDto.getUploadTicket().isEmpty()) {
			pointSettingMgmRequestDto.setUploadTicketList((List<UploadInfoVo>) BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUploadTicket(), UploadInfoVo.class));
		}


		if(pointSettingMgmRequestDto.getUserGradeList() != null) {
			pointSettingMgmRequestDto.setPointUserGradeVoList(BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUserGradeList(), PointUserGradeVo.class));

			List<PointUserGradeVo> pointUserGradeList = pointSettingMgmRequestDto.getPointUserGradeVoList();
			// 적립금 등록 (후기)
			if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.FEEDBACK.getCode())) {

				// 적립금 회원등급 설정 삭제
				promotionPointMapper.removePointUserGrade(pointSettingMgmRequestDto);

				if(pointUserGradeList != null && !pointUserGradeList.isEmpty()) {
					for(int i=0; i<pointUserGradeList.size();i++) {
						PointUserGradeVo vo = pointUserGradeList.get(i);
						vo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
						pointUserGradeList.get(i).setPmPointId(pointSettingMgmRequestDto.getPmPointId());
						pointUserGradeList.get(i).setReviewType(PointEnums.PointUsergradeType.USER_GRADE.getCode());
						if(pointSettingMgmRequestDto.getUrUserId() == null) {
							pointUserGradeList.get(i).setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
						}else {
							pointUserGradeList.get(i).setUserId(pointSettingMgmRequestDto.getUrUserId());
						}
					}

					PointUserGradeVo feedbackPointVo = new PointUserGradeVo();
					feedbackPointVo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
					feedbackPointVo.setReviewType(PointEnums.PointUsergradeType.NORMAL.getCode());
					feedbackPointVo.setNormalAmount(Integer.parseInt(pointSettingMgmRequestDto.getNormalAmount()));
					feedbackPointVo.setPhotoAmount(Integer.parseInt(pointSettingMgmRequestDto.getPhotoAmount()));
					feedbackPointVo.setPremiumAmount(Integer.parseInt(pointSettingMgmRequestDto.getPremiumAmount()));
					if(pointSettingMgmRequestDto.getUrUserId() == null) {
						feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
					}else {
						feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUrUserId());
					}
					// 후기 적립금 추가
					pointUserGradeList.add(feedbackPointVo);

					// 적립금 회원등급 설정 등록
					promotionPointMapper.addPointUserGrade(pointUserGradeList);
				}else {

					PointUserGradeVo feedbackPointVo = new PointUserGradeVo();
					feedbackPointVo.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
					feedbackPointVo.setReviewType(PointEnums.PointUsergradeType.NORMAL.getCode());
					feedbackPointVo.setNormalAmount(Integer.parseInt(pointSettingMgmRequestDto.getNormalAmount()));
					feedbackPointVo.setPhotoAmount(Integer.parseInt(pointSettingMgmRequestDto.getPhotoAmount()));
					feedbackPointVo.setPremiumAmount(Integer.parseInt(pointSettingMgmRequestDto.getPremiumAmount()));

					if(pointSettingMgmRequestDto.getUrUserId() == null) {
						feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUserVo().getUserId());
					}else {
						feedbackPointVo.setUserId(pointSettingMgmRequestDto.getUrUserId());
					}

					// 후기 적립금 추가
					pointUserGradeList.add(feedbackPointVo);

					// 적립금 적용범위 등록
					promotionPointMapper.addPointUserGrade(pointUserGradeList);

				}
			}
		}

		// 난수생성 코드 설정
		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.SERIAL_NUMBER.getCode())) {
			if(!pointSettingMgmRequestDto.getSerialNumberType().equals(PointEnums.SerialNumberType.FIXED_VALUE.getCode())) {

				pointSettingMgmRequestDto.setSerialNumberUseType(PointEnums.SerialNumberUseType.POINT.getCode());		// 사용타입 : 적립금

				pointSettingMgmRequestDto.setSerialNumberStatus(PointEnums.SerialNumberStatus.ISSUED.getCode());			// 이용권 상태: 발급

				// 개별난수번호 삭제
				promotionPointMapper.removeSerialNumber(pointSettingMgmRequestDto);

				if(pointSettingMgmRequestDto.getRandNumTypeSelect().equals(PointEnums.SerialNumberType.AUTO_CREATE.getCode())) {  // 자동생성

					if(pointSettingMgmRequestDto.getIssueQty() > 0) {
						for(int i = 0 ; i<pointSettingMgmRequestDto.getIssueQty() ; i++) {

							// 개별난수번호 등록  (등록시 난수 생성  function 호출)
							promotionPointMapper.addSerialNumber(pointSettingMgmRequestDto);
						}
					}

				}else if(pointSettingMgmRequestDto.getRandNumTypeSelect().equals(PointEnums.SerialNumberType.EXCEL_UPLOAD.getCode())) {  // 엑셀업로드
					List<UploadInfoVo> accountTicketList = pointSettingMgmRequestDto.getUploadTicketList();
					for(int i=0;i<accountTicketList.size();i++) {
						UploadInfoVo vo = new UploadInfoVo();
						vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
						pointSettingMgmRequestDto.setFixSerialNumber(vo.getSerialNumber());
						int seralCount = this.getDuplicateSerialNumber(vo);
						if(seralCount > 0) {
							throw new BosCustomException(PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getCode(), PointEnums.FixedNumberValidation.DUPLICATE_NUMBER.getMessage());
						}else {

							// 개별난수번호 등록  (엑셀에서 저장된 난수로 등록)
							promotionPointMapper.addSerialNumber(pointSettingMgmRequestDto);
						}
					}

				}
			}
		}

		// 이전 적립금정보 조회
		ApprovalStatusVo history  = this.getPointStatusHistory(pointSettingMgmRequestDto, "PREV");

		if(history != null) {
			// 적립금상태이력 등록
			this.addPointStatusHistory(history);
		}

		else return ApiResult.fail();
		return ApiResult.success(pointSettingMgmRequestDto);
    }

	/**
	 * 적립금 설정 수정 ( 엑셀 대량 지급 )
	 *
	 * @param pointSettingMgmRequestDto PointSettingMgmRequestDto
	 * @return int
	 * @throws 	Exception
	 */
	protected ApiResult<?> putExcelLargePayPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto)throws Exception {

		//포인트 수정 승인 상태값 세팅
		pointSettingMgmRequestDto = this.determinPutPointApprovalStatus(pointSettingMgmRequestDto);

		// 적립금 등록 (관리자 지급/차감)
		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {
			pointSettingMgmRequestDto.setIssueStartDate(null);
			pointSettingMgmRequestDto.setIssueEndDate(null);

			// 유효일 설정
			pointSettingMgmRequestDto.setValidityDay(pointSettingMgmRequestDto.getPointPaymentAmount());
		}

		// 기간 종료일 시간
		if(pointSettingMgmRequestDto.getIssueEndDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getIssueEndDate())) {
			pointSettingMgmRequestDto.setIssueEndDate(pointSettingMgmRequestDto.getIssueEndDate() + "235959");
		}

		// 유효기간 : 기간설정
		if(pointSettingMgmRequestDto.getValidityDate() != null && !StringUtils.isEmpty(pointSettingMgmRequestDto.getValidityDate())) {
			pointSettingMgmRequestDto.setValidityDate(pointSettingMgmRequestDto.getValidityDate() + "235959");
		}

		if(!pointSettingMgmRequestDto.getUploadUser().isEmpty()) {
			pointSettingMgmRequestDto.setUploadUserList((List<UploadInfoVo>) BindUtil.convertJsonArrayToDtoList(pointSettingMgmRequestDto.getUploadUser(), UploadInfoVo.class));
		}

		if(pointSettingMgmRequestDto.getPointType().equals(PointEnums.PointType.ADMIN.getCode())) {

			// 적립금 등록 (관리자 지급/차감) :엑셀 업로드 회원ID 확인  Start
			List<UploadInfoVo> accountUserPointList = pointSettingMgmRequestDto.getUploadUserList();
			// 엑셀 업로드 회원 ID 체크
			if(accountUserPointList != null) {
				int userIdCnt = 0;
				for(int i=0 ; i<accountUserPointList.size() ; i++) {
					if(accountUserPointList.get(i).getUrUserId() == null) {
						pointSettingMgmRequestDto.setLoginId(accountUserPointList.get(i).getLoginId());
						int checkCnt = promotionPointMapper.getUserIdCnt(pointSettingMgmRequestDto);
						if(checkCnt > 0) {
							userIdCnt++;
						}
					}else {
						userIdCnt++;
					}
				}

				if(userIdCnt != accountUserPointList.size()) {
					throw new BosCustomException(PointEnums.AdminPointCheck.USER_ID_FAIL.getCode(), PointEnums.AdminPointCheck.USER_ID_FAIL.getMessage());
				}
			}

			// 적립금 등록 (관리자 지급/차감) :엑셀 업로드 회원ID 확인  End

			// 적립금 등록 (관리자 지급/차감) :관리자 지급 유효기간 확인
			String loginId = SessionUtil.getBosUserVO().getUserId();
			PointSettingResultVo pointSettingResultVo = promotionPointMapper.getGroupValidityDay(loginId);
			if(pointSettingResultVo != null && PointEnums.PointPayment.PROVISION.getCode().equals(pointSettingMgmRequestDto.getPointPaymentType())) {
				if(Integer.parseInt(pointSettingMgmRequestDto.getValidityDay()) > Integer.parseInt(pointSettingResultVo.getValidityDay())) {
					throw new BosCustomException(PointEnums.AdminPointCheck.INVALID_VALIDITY_DAY_FAIL.getCode(), PointEnums.AdminPointCheck.INVALID_VALIDITY_DAY_FAIL.getMessage());
				}
			}
		}

		// 적립금 수정 ( 엑셀 대량 지급인 건 )
		if(PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
			// GR_PM_POINT_ID + 회원 ID로 PM_POINT_ID 구하고 각각 PM_POINT 테이블 업데이트
			List<UploadInfoVo> uploadAccountUserList = pointSettingMgmRequestDto.getUploadUserList();
			PointSettingMgmRequestDto pointUserParamDto = new PointSettingMgmRequestDto();
			String beforeGrPmPointId = pointSettingMgmRequestDto.getGrPmPointId();
			pointSettingMgmRequestDto.setGrPmPointId("GR"+System.currentTimeMillis());
			String savePmPointId = "";

			for(int i = 0; i < uploadAccountUserList.size(); i++) {
				boolean sameValueCheck = false;

				if(uploadAccountUserList.get(i).getLoginId() != null) {
					// 로그인 ID
					String loginId = uploadAccountUserList.get(i).getLoginId();
					// 회원 정보 조회
					UserVo userVo = userJoinMapper.getUserInfo(loginId);
					// 회원 ID로 PM_POINT_ISSUE 테이블에서 PM_POINT_ID 조회
					String pmPointId = promotionPointMapper.getPmPointId(userVo.getUserId().toString(), beforeGrPmPointId);


					if(uploadAccountUserList.get(i).getUploadIssueValue() != null) {
						// 동일한 적립금이 있는지 체크
						if(uploadAccountUserList.get(i).getUploadIssueValue().equals(pointSettingMgmRequestDto.getIssueValue()) && i > 0) {
							sameValueCheck = true;
						}
						pointSettingMgmRequestDto.setIssueValue(uploadAccountUserList.get(i).getUploadIssueValue());
					} else {
						if(String.valueOf(uploadAccountUserList.get(i).getIssueVal()).equals(pointSettingMgmRequestDto.getIssueValue()) && i > 0) {
							sameValueCheck = true;
						}
						pointSettingMgmRequestDto.setIssueValue(String.valueOf(uploadAccountUserList.get(i).getIssueVal()));
					}
					if(sameValueCheck && i > 0) {
						pmPointId = savePmPointId;
					}

					pointSettingMgmRequestDto.setPmPointId(pmPointId);
					pointUserParamDto.setPmPointId(pmPointId);
					if(!sameValueCheck) {
						// PM_POINT 새로 INSERT
						// 분담정보 삭제
						promotionPointMapper.remomveBosPointIssue(pointUserParamDto);
						promotionPointMapper.removeOrganization(pointSettingMgmRequestDto);
						promotionPointMapper.insertPmPointExcel(pointSettingMgmRequestDto);
						savePmPointId = pointSettingMgmRequestDto.getPmPointId();
						pointUserParamDto.setPmPointId(pointSettingMgmRequestDto.getPmPointId());
					}

					pointUserParamDto.setUrUserId(userVo.getUserId().toString());
					// 적립금_발급정보 등록
					promotionPointMapper.addBosPointIssue(pointUserParamDto);

					if(!sameValueCheck) {

						// 분담정보 등록
						promotionPointMapper.addOrganization(pointSettingMgmRequestDto);

						ApprovalStatusVo history = ApprovalStatusVo.builder()
								.taskPk(pointSettingMgmRequestDto.getPmPointId())
								.apprUserId(pointSettingMgmRequestDto.getApprUserId())
								.apprSubUserId(pointSettingMgmRequestDto.getApprSubUserId())
								.approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
								.masterStat(pointSettingMgmRequestDto.getPointMasterStat())
								.apprStat(pointSettingMgmRequestDto.getApprStat())
								.build();

						// 적립금상태이력 등록
						this.addPointStatusHistory(history);
					}
				}
			}
			// 이전 GR_PM_POINT_ID로 PM_POINT 테이블에서 삭제처리
			// promotionPointMapper.removeGroupPointInfo(beforeGrPmPointId);
			pointSettingMgmRequestDto.setBeforeGrPmPointId(beforeGrPmPointId);
		}

		return ApiResult.success(pointSettingMgmRequestDto);
	}

	/**
	 * 적립금 승인 상태 변경
	 * @param pointSettingMgmRequestDto PointSettingMgmRequestDto
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> putPointStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{

		// 엑셀 대량 지급건
		if(PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())){
			promotionPointMapper.putGrPmPointStatus(pointSettingMgmRequestDto);
			List<PointSettingResultVo> pmPointIdList = promotionPointMapper.getPmPointIdList(pointSettingMgmRequestDto);
			for(int i = 0 ; i < pmPointIdList.size(); i++) {
				pointSettingMgmRequestDto.setPmPointId(pmPointIdList.get(i).getPmPointId());
				// 이전 적립금정보 조회
				ApprovalStatusVo history  = this.getPointStatusHistory(pointSettingMgmRequestDto, "PREV");

				if(history != null) {
					// 적립금상태이력 등록
					this.addPointStatusHistory(history);
				}
			}
		} else {
			// 적립금 승인 상태변경
			promotionPointMapper.putPmPointStatus(pointSettingMgmRequestDto);
			// 이전 적립금정보 조회
			ApprovalStatusVo history  = this.getPointStatusHistory(pointSettingMgmRequestDto, "PREV");

			if(history != null) {
				// 적립금상태이력 등록
				this.addPointStatusHistory(history);
			}
		}

		return ApiResult.success(true);
	}


	/**
	 * 포인트등록인 경우 승인요청 상태값 세팅
	 * 자동 승인 절차는 쿠폰 포인트만 가능 하다.
	 * - 쿠폰 자동승인 : 클래임 처리관련 로직
	 * - 포인트 자동승인 : 포인트 지급 금액이 10000원 이하인 경우 자동승인
	 * @param pointSettingMgmRequestDto
	 * @return
	 */
	protected PointSettingMgmRequestDto determinAddPointApprovalStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) {

		// 승인 요청처리 상태 설정
		if("Y".equals(pointSettingMgmRequestDto.getApprovalCheck())){
//    		pointSettingMgmRequestDto.setStatus(PointEnums.PointApprovalStatus.REQUEST_APPROVAL.getCode()); // 승인요청처리 채크 할 경우 : 승인요청 상태
			pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.SAVE.getCode());
			pointSettingMgmRequestDto.setApprStat(ApprovalStatus.REQUEST.getCode());
		}else {
//			pointSettingMgmRequestDto.setStatus(PointEnums.PointApprovalStatus.SAVE.getCode()); 			// 승인요청처리 채크 안할 경우 :저장 상태
			pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.SAVE.getCode());
			pointSettingMgmRequestDto.setApprStat(ApprovalStatus.NONE.getCode());
		}

		// 관리자 차감 지급일 경우만 자동승인
		if (PointEnums.PointType.ADMIN.getCode().equals(pointSettingMgmRequestDto.getPointType())) {
			int issueValue = Integer.valueOf(pointSettingMgmRequestDto.getIssueValue());
			//적립금 10000원 이하 자동 승인 & 단일 지급인 건은 자동승인
			if (APPROVE_POINT_BY_SYSTEM_AMOUNT >= issueValue && !PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
				pointSettingMgmRequestDto.setApprStat(ApprovalStatus.APPROVED_BY_SYSTEM.getCode());
			}

			if(PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				// 엑셀 대량 지급 & 1인 최대금액이 10000원보다 크면 모두 승인요청
				if(APPROVE_POINT_BY_SYSTEM_AMOUNT >= Integer.valueOf(pointSettingMgmRequestDto.getMaxIssueValue()) && "Y".equals(pointSettingMgmRequestDto.getAutoApprStatus()) && PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
					pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
					pointSettingMgmRequestDto.setApprStat(ApprovalStatus.APPROVED_BY_SYSTEM.getCode());
				}
			}
		}

		return pointSettingMgmRequestDto;
	}


	/**
	 * 포인트 수정일 경우 승인 요청상태 세팅
	 * @param pointSettingMgmRequestDto
	 * @return
	 */
	protected PointSettingMgmRequestDto determinPutPointApprovalStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {

		String pmPointId = pointSettingMgmRequestDto.getPmPointId();
		PointSettingResultVo pointSettingResultVo = promotionPointMapper.getPointDetail(pmPointId);
		ApprovalAuthInfoVo approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessPointInfo(pmPointId);

		//포인트 마스터 승인 상태가 APPROVED, STOP 일경우 변경사항 없음
		if (PointMasterStatus.APPROVED.getCode().equals(approvalAuthInfoVo.getMasterStat())
			|| PointMasterStatus.STOP.getCode().equals(approvalAuthInfoVo.getMasterStat())) {

			if (PointEnums.PointType.BUY.getCode().equals(pointSettingResultVo.getPointType())) {
				this.checkBuyPointApprovalValidate(pointSettingMgmRequestDto, pointSettingResultVo);
			} else if (PointEnums.PointType.FEEDBACK.getCode().equals(pointSettingResultVo.getPointType())) {
				this.checkFeedbackPointApprovalValidate(pointSettingMgmRequestDto, pointSettingResultVo);
			} else if (PointEnums.PointType.AUTO.getCode().equals(pointSettingResultVo.getPointType())) {
				this.checkAutoPointApprovalValidate(pointSettingMgmRequestDto, pointSettingResultVo);
			}

			pointSettingMgmRequestDto.setPointMasterStat(approvalAuthInfoVo.getMasterStat());
			pointSettingMgmRequestDto.setApprStat(approvalAuthInfoVo.getApprStat());
			return pointSettingMgmRequestDto;
		}

		//포인트 마스터 승인 상태가 SAVE 일 경우
		if("Y".equals(pointSettingMgmRequestDto.getApprovalCheck())){
//    		pointSettingMgmRequestDto.setStatus(PointEnums.PointApprovalStatus.REQUEST_APPROVAL.getCode()); // 승인요청처리 채크 할 경우 : 승인요청 상태
			pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.SAVE.getCode());
			pointSettingMgmRequestDto.setApprStat(ApprovalStatus.REQUEST.getCode());
		}else {
//			pointSettingMgmRequestDto.setStatus(PointEnums.PointApprovalStatus.SAVE.getCode()); 			// 승인요청처리 채크 안할 경우 :저장 상태
			pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.SAVE.getCode());
			pointSettingMgmRequestDto.setApprStat(ApprovalStatus.NONE.getCode());
		}

		// 관리자 차감 지급일 경우만 자동승인
		if (PointEnums.PointType.ADMIN.getCode().equals(pointSettingMgmRequestDto.getPointType())) {
			int issueValue = Integer.valueOf(pointSettingMgmRequestDto.getIssueValue());

			//적립금 10000원 이하 자동 승인 & 단일 지급인 건은 자동승인
			// if (APPROVE_POINT_BY_SYSTEM_AMOUNT >= issueValue) {
			if (APPROVE_POINT_BY_SYSTEM_AMOUNT >= issueValue && !PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
				pointSettingMgmRequestDto.setApprStat(ApprovalStatus.APPROVED_BY_SYSTEM.getCode());
			}

			// 엑셀 대량 지급 & 1인 최대금액이 10000원이 안되는 적립금 자동 승인
			if("Y".equals(pointSettingMgmRequestDto.getAutoApprStatus()) && PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				pointSettingMgmRequestDto.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
				pointSettingMgmRequestDto.setApprStat(ApprovalStatus.APPROVED_BY_SYSTEM.getCode());
			}
		}

		return pointSettingMgmRequestDto;
	}

    /**
     * @Desc 적립금 수정
     * @param PointSettingMgmRequestDto
     * @throws Exception
     * @return int
     */
    protected int updatePointName(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{
    	return promotionPointMapper.updatePointName(pointSettingMgmRequestDto);
    }

	/**
	 * @Desc 적립금 수정
	 * @param pointSettingMgmRequestDto
	 * @throws Exception
	 * @return int
	 */
	protected int updatePointIssueReason(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{
		return promotionPointMapper.updatePointIssueReason(pointSettingMgmRequestDto);
	}

    /**
     * @Desc 적립금 상태 이력 조회
     * @param PointSettingMgmRequestDto
     * @return ApprovalStatusVo
     */
    protected ApprovalStatusVo getPointStatusHistory(PointSettingMgmRequestDto pointSettingMgmRequestDto, String getType) throws Exception{
    	ApprovalStatusVo history = promotionPointMapper.getPointStatusHistory(pointSettingMgmRequestDto);

        if("PREV".equals(getType)) {
        	if(history != null) {
    			// 이전 적립금정보 조회
    			history.setPrevMasterStat(history.getMasterStat());
    			history.setPrevApprStat(history.getApprStat());
    			history.setMasterStat(pointSettingMgmRequestDto.getPointMasterStat());
    			history.setApprStat(pointSettingMgmRequestDto.getApprStat());

    			if (!StringUtils.isEmpty(pointSettingMgmRequestDto.getPointMasterStat())) {
        			history.setMasterStat(pointSettingMgmRequestDto.getPointMasterStat());
    			}

    			if (!StringUtils.isEmpty(pointSettingMgmRequestDto.getApprStat())) {
        			history.setApprStat(pointSettingMgmRequestDto.getApprStat());
    			}

    			if (StringUtils.isEmpty(history.getApprSubUserId())) {
    				history.setApprSubUserId(pointSettingMgmRequestDto.getApprSubUserId());
				}

				if (StringUtils.isEmpty(history.getApprUserId())) {
					history.setApprUserId(pointSettingMgmRequestDto.getApprUserId());
				}
        	}
        }
        return history;
    }


	/**
	 * 적립금 설정 삭제
	 * @param pointRequestDto
	 * @return
	 * @throws Exception
	 */
	protected PointResponseDto removePoint(PointRequestDto pointRequestDto) throws Exception {
		PointResponseDto result = new PointResponseDto();

		promotionPointMapper.removePoint(pointRequestDto);
		return result;
	}


    /**
     * 포인트 조회 - 사용가능
     *
     * @param urUserId Long
     * @return String
     * @throws Exception exception
     */
    protected int getPointUsable(Long urUserId) throws Exception {

        return pointUseMapper.getUserAvailablePoints(urUserId);
    }

    /**
     * 포인트 조회 - 소멸예정
     *
     * @param urUserId  Long
     * @param startDate String
     * @param endDate   String
     * @return String
     * @throws Exception exception
     */
    protected String getPointExpectExpired(Long urUserId, String startDate, String endDate) throws Exception {
        return promotionPointMapper.getPointExpectExpired(urUserId, startDate, endDate);
    }

    /**
     * 포인트 정보 목록조회 - User기준
     *
     * @param dto CommonGetPointListByUserRequestDto
     * @return CommonGetPointListByUserResponseDto
     * @throws Exception exception
     */
    protected CommonGetPointListByUserResponseDto getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
		int deviceCount = DeviceUtil.getDirInfo().equals("pc") ? PC_MESSAGE_LENGTH : MOBILE_MESSAGE_LENGTH;
		dto.setDeviceCount(deviceCount);
        Page<CommonGetPointListByUserVo> rows = promotionPointMapper.getPointListByUser(dto);

        if(CollectionUtils.isNotEmpty(rows)) {
        	for(CommonGetPointListByUserVo vo : rows) {
        		// 적립 상세 내역
        		String displayName = PointProcessType.findByCode(vo.getPointProcessType()).getDisplayName();
        		// 예외처리 추가
				if(PointEnums.BuyerDisplayName.DEPOSIT_POINT_CUSTOMER_SERVICE_REFUND_ORDER.getCode().equals(vo.getPointProcessType())){
					displayName = PointEnums.BuyerDisplayName.DEPOSIT_POINT_CUSTOMER_SERVICE_REFUND_ORDER.getMessage();
				}
        		vo.setPointTypeName(displayName);
        	}
        }

        return CommonGetPointListByUserResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(rows.getResult())
                .build();
    }

    /**
     * 적립금 소멸예정 목록 조회
     *
     * @param urUserId  Long
     * @param startDate String
     * @param endDate   String
     * @return List<CommonGetPointExpectExpireListResultVo>
     * @throws Exception exception
     */
    protected List<CommonGetPointExpectExpireListResultVo> getPointExpectExpireList(Long urUserId, String startDate, String endDate) throws Exception {
        return promotionPointMapper.getPointExpectExpireList(urUserId, startDate, endDate);
    }

    /**
     * 유저 적립금 등록 Validation
     *
     * @param urUserId  Long
     * @param pmPointId Long
     * @return String
     * @throws Exception exception
     */
    protected CommonCheckAddPointValidationByUserResponseDto checkAddPointValidationByUser(Long urUserId, Long pmPointId) throws Exception {
        CommonCheckAddPointValidationByUserResponseDto result = new CommonCheckAddPointValidationByUserResponseDto();
        CommonGetAddPointValidationInfoVo vo = promotionPointMapper.getAddPointValidationInfo(urUserId, pmPointId);
        result.setData(vo);

        if (vo == null){
			result.setValidationEnum(PointEnums.AddPointValidation.NOT_EXIST_POINT);	//존재하지 않는 적립금
			return result;
		}

        if (!PointEnums.PointMasterStatus.APPROVED.getCode().equals(vo.getPointMasterStat())) { //이용권 승인 상태 확인
            result.setValidationEnum(PointEnums.AddPointValidation.NOT_ACCEPT_APPROVAL);
            return result;
        }
        if (vo.getIssueQty() <= vo.getIssueCnt()) {                                             //발급수량 비교
            result.setValidationEnum(PointEnums.AddPointValidation.OVER_ISSUE_QTY);
            return result;
        }
        if (vo.getIssueQtyLimit() != 0 && vo.getIssueQtyLimit() <= vo.getUserIssueCnt()) {      //1인 발급 제한 수량 비교
            result.setValidationEnum(PointEnums.AddPointValidation.OVER_ISSUE_QTY_LIMIT);
            return result;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate issueStartDate = LocalDate.parse(vo.getIssueStartDate(), dateFormatter);
        LocalDate issueEndDate = LocalDate.parse(vo.getIssueEndDate(), dateFormatter);

        if (now.isBefore(issueStartDate)) {                                                     // 발급시작일 이전
            result.setValidationEnum(PointEnums.AddPointValidation.NOT_ISSUE_DATE);
            return result;
        } else if (now.isAfter(issueEndDate)) {                                                 // 발급종료일 지남
            result.setValidationEnum(PointEnums.AddPointValidation.OVER_ISSUE_DATE);
            return result;
        }

        result.setValidationEnum(PointEnums.AddPointValidation.PASS_VALIDATION);
        return result;
    }

    /**
     * @Desc 적립금상태이력 등록
     * @param history
     * @return int
     */
    protected int addPointStatusHistory(ApprovalStatusVo history){
    	return promotionPointMapper.addPointStatusHistory(history);
    }

    /**
	 * 적립금승인 목록 조회
	 *
	 * @param PointApprovalRequestDto
	 * @return PointApprovalResponseDto
	 */
    @UserMaskingRun
    protected PointApprovalResponseDto getApprovalPointList(PointApprovalRequestDto requestDto) {
    	PointApprovalResponseDto result = new PointApprovalResponseDto();
    	ArrayList<String> approvalStatusArray = null;

    	if (!StringUtil.isEmpty(requestDto.getSearchApprovalStatus())) {
    		approvalStatusArray = StringUtil.getArrayList(requestDto.getSearchApprovalStatus().replace(" ", ""));
    		requestDto.setApprovalStatusArray(approvalStatusArray);
        }

    	PageMethod.startPage(requestDto.getPage(), requestDto.getPageSize());
    	Page<PointApprovalResultVo> rows = promotionPointMapper.getApprovalPointList(requestDto);
    	result.setTotal((int)rows.getTotal());
    	result.setRows(rows.getResult());
    	return result;
    }

    /**
     * 적립금승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalPoint(ApprovalStatusVo approvalVo) throws Exception {
		if(promotionPointMapper.putCancelRequestApprovalPoint(approvalVo) > 0
			&& this.addPointStatusHistory(approvalVo) > 0
			) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 적립금승인 폐기처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putDisposalApprovalPoint(ApprovalStatusVo approvalVo) throws Exception {
		if(promotionPointMapper.putDisposalApprovalPoint(approvalVo) > 0
			&& this.addPointStatusHistory(approvalVo) > 0
			) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 적립금승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessPoint(ApprovalStatusVo approvalVo) throws Exception {
		if(promotionPointMapper.putApprovalProcessPoint(approvalVo) > 0
			&& this.addPointStatusHistory(approvalVo) > 0
			) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 적립금 조회 - 소멸예정 - 적립금 소멸예정 자동메일 발송용
     *
     * @param urUserId
     * @return PointExpiredForEmailVo
     * @throws Exception exception
     */
    protected PointExpiredForEmailVo getPointExpectExpiredForEmail(Long urUserId) throws Exception {
        return promotionPointMapper.getPointExpectExpiredForEmail(urUserId);
    }

    /**
     * 적립금 소멸예정 목록 조회 - 적립금 소멸예정 자동메일 발송용
     *
     * @param urUserId
     * @return PointExpiredListForEmailVo
     * @throws Exception exception
     */
    protected List<PointExpiredListForEmailVo> getPointExpectExpireListForEmail(Long urUserId) throws Exception {
        return promotionPointMapper.getPointExpectExpireListForEmail(urUserId);
    }

    /**
     * 이벤트 - 적립금 정보 조회
     * @param pointRequestDto
     * @return
     */
	protected ApiResult<?> getEventCallPointInfo(PointRequestDto pointRequestDto) {
		PointSettingListResponseDto result = new PointSettingListResponseDto();

		List<PointSettingResultVo> rows = promotionPointMapper.getEventCallPointInfo(pointRequestDto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * 적립금 지급 상태 조회
	 * @param pointRequestDto
	 * @return
	 */
	protected ApiResult<?> getPointSearchStatus(PointRequestDto pointRequestDto) {
		PointSettingListResponseDto result = new PointSettingListResponseDto();

		List<PointSettingResultVo> rows = promotionPointMapper.getPointSearchStatus(pointRequestDto);	// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * 이용권 수금 상태 변경
	 * @param pointRequestDto
	 * @return
	 * @throws Exception
	 */
	protected int putTicketCollectStatus(PointRequestDto pointRequestDto) throws Exception {
		return promotionPointMapper.putTicketCollectStatus(pointRequestDto);
	}


    /**
     * @Desc 적립금 이용권 승인 상태 변경
     * @param PointRequestDto
     * @throws Exception
     * @return int
     */
    protected int putSerialNumberStatus(PointRequestDto pointRequestDto) throws Exception{
        return promotionPointMapper.putSerialNumberStatus(pointRequestDto);
    }


    /**
     * 적립금 확인
     * @param pointRequestDto
     * @return
     * @throws Exception
     */
    protected UploadInfoVo getAdminAmountCheck(PointRequestDto pointRequestDto) throws Exception {
    	return promotionPointMapper.getAdminAmountCheck(pointRequestDto);
    }

	/**
	 * 이용권 승인확정 데이터 정합성 체크
	 * @param pointSettingMgmRequestDto
	 */
	private void checkBuyPointApprovalValidate(PointSettingMgmRequestDto pointSettingMgmRequestDto, PointSettingResultVo pointSettingResultVo) throws Exception {
		if (pointSettingResultVo.getIssueValue() != Integer.valueOf(pointSettingMgmRequestDto.getIssueValue())) {
			throw new BaseException("승인 완료된 포인트 적립금이 아닙니다.");
		}

		if (pointSettingResultVo.getIssueBudget() != Integer.valueOf(pointSettingMgmRequestDto.getIssueBudget())) {
			throw new BaseException("승인 완료된 포인트 지급액이 아닙니다.");
		}

		if (Integer.valueOf(pointSettingResultVo.getIssueQty()) != pointSettingMgmRequestDto.getIssueQty()) {
			throw new BaseException("승인 완료된 생성개수가 아닙니다.");
		}

		if (!pointSettingResultVo.getIssueQtyLimit().equals(pointSettingMgmRequestDto.getIssueQtyLimit())) {
			throw new BaseException("승인 완료된 지급수량제한이 아닙니다.");
		}

		if (PointEnums.ValidityType.VALIDITY.getCode().equals(pointSettingResultVo.getValidityType())
				&& !pointSettingResultVo.getValidityDay().equals(pointSettingMgmRequestDto.getValidityDay())) {
			throw new BaseException("승인 완료된 유효일과 맞지 않습니다.");
		} else if (PointEnums.ValidityType.PERIOD.getCode().equals(pointSettingResultVo.getValidityType())
				&& !pointSettingResultVo.getValidityDate().equals(pointSettingMgmRequestDto.getValidityDate())) {
			throw new BaseException("승인 완료된 유효기간과 맞지 않습니다.");
		}

		String savedStartDate = DateUtil.convertFormat(pointSettingResultVo.getIssueStartDate(), "yyyy-MM-dd", "yyyyMMdd");
		String savedEndDate = DateUtil.convertFormat(pointSettingResultVo.getIssueEndDate(), "yyyy-MM-dd", "yyyyMMdd");

		if (!savedStartDate.equals(pointSettingMgmRequestDto.getIssueStartDate())
				|| savedEndDate.equals(pointSettingMgmRequestDto.getIssueEndDate())) {
			throw new BaseException("승인 완료된 지급기간이 아닙니다.");
		}
	}

	/**
	 * 후기 승인확정 데이터 정합성 체크
	 * @param pointSettingMgmRequestDto
	 */
	private void checkFeedbackPointApprovalValidate(PointSettingMgmRequestDto pointSettingMgmRequestDto, PointSettingResultVo pointSettingResultVo) throws Exception {
		if (StringUtils.isEmpty(pointSettingMgmRequestDto.getNormalAmount()) == false
				&& pointSettingResultVo.getNormalAmount() != Integer.valueOf(pointSettingMgmRequestDto.getNormalAmount())) {
			throw new BaseException("승인 완료된 일반후기 지급 금액이 아닙니다.");
		}

		if (StringUtils.isEmpty(pointSettingMgmRequestDto.getPhotoAmount()) == false
				&& pointSettingResultVo.getPhotoAmount() != Integer.valueOf(pointSettingMgmRequestDto.getPhotoAmount())) {
			throw new BaseException("승인 완료된 포토후기 지급 금액이 아닙니다.");
		}

		if (StringUtils.isEmpty(pointSettingMgmRequestDto.getPremiumAmount()) == false
				&& pointSettingResultVo.getPremiumAmount() != Integer.valueOf(pointSettingMgmRequestDto.getPremiumAmount())) {
			throw new BaseException("승인 완료된 프리미엄후기 지급 금액이 아닙니다.");
		}

		if (!pointSettingResultVo.getIssueDayCount().equals(pointSettingMgmRequestDto.getIssueDayCount())) {
			throw new BaseException("승인 완료된 지급기준일이 아닙니다.");
		}

		if (!pointSettingResultVo.getValidityDay().equals(pointSettingMgmRequestDto.getFeedbackValidityDay())) {
			throw new BaseException("승인 완료된 유효기간이 아닙니다.");
		}

		String savedStartDate = DateUtil.convertFormat(pointSettingResultVo.getIssueStartDate(), "yyyy-MM-dd", "yyyyMMdd");
		String savedEndDate = DateUtil.convertFormat(pointSettingResultVo.getIssueEndDate(), "yyyy-MM-dd", "yyyyMMdd");

		if (!pointSettingMgmRequestDto.getIssueStartDate().equals(savedStartDate)
				|| !pointSettingMgmRequestDto.getIssueEndDate().equals(savedEndDate)) {
			throw new BaseException("승인 완료된 지급기간이 아닙니다.");
		}
	}

	/**
	 * 자동지급 승인확정 데이터 정합성 체크
	 * @param pointSettingMgmRequestDto
	 * @param pointSettingResultVo
	 * @throws Exception
	 */
	private void checkAutoPointApprovalValidate(PointSettingMgmRequestDto pointSettingMgmRequestDto, PointSettingResultVo pointSettingResultVo) throws Exception {
		if (pointSettingResultVo.getIssueValue() != Integer.valueOf(pointSettingMgmRequestDto.getIssueValue())) {
			throw new BaseException("승인 완료된 포인트 적립금이 아닙니다.");
		}

		if (pointSettingResultVo.getIssueBudget() != Integer.valueOf(pointSettingMgmRequestDto.getIssueBudget())) {
			throw new BaseException("승인 완료된 포인트 지급액이 아닙니다.");
		}

		if (Integer.valueOf(pointSettingResultVo.getIssueQty()) != pointSettingMgmRequestDto.getIssueQty()) {
			throw new BaseException("승인 완료된 생성개수가 아닙니다.");
		}

		if (!pointSettingResultVo.getIssueQtyLimit().equals(pointSettingMgmRequestDto.getIssueQtyLimit())) {
			throw new BaseException("승인 완료된 지급수량제한이 아닙니다.");
		}

		if (PointEnums.ValidityType.VALIDITY.getCode().equals(pointSettingResultVo.getValidityType())
				&& !pointSettingResultVo.getValidityDay().equals(pointSettingMgmRequestDto.getValidityDay())) {
			throw new BaseException("승인 완료된 유효일과 맞지 않습니다.");
		} else if (PointEnums.ValidityType.PERIOD.getCode().equals(pointSettingResultVo.getValidityType())
				&& !pointSettingResultVo.getValidityDate().equals(pointSettingMgmRequestDto.getValidityDate())) {
			throw new BaseException("승인 완료된 유효기간과 맞지 않습니다.");
		}

		String savedStartDate = DateUtil.convertFormat(pointSettingResultVo.getIssueStartDate(), "yyyy-MM-dd", "yyyyMMdd");
		String savedEndDate = DateUtil.convertFormat(pointSettingResultVo.getIssueEndDate(), "yyyy-MM-dd", "yyyyMMdd");

		if (!savedStartDate.equals(pointSettingMgmRequestDto.getIssueStartDate())
				|| !savedEndDate.equals(pointSettingMgmRequestDto.getIssueEndDate())) {
			throw new BaseException("승인 완료된 지급기간이 아닙니다.");
		}
	}


    /**
     * @Desc 이용권 단일코드 중복조회
     * @param PointSettingMgmRequestDto
     * @throws Exception
     * @return int
     */
    protected int getDuplicateFixedNumber(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{
        return promotionPointMapper.getDuplicateFixedNumber(pointSettingMgmRequestDto);
    }

    /**
	 * @Desc 포인트 자동승인시 승인 날짜 업데이트
	 * @param requestDto
	 * @return
	 */
	protected int putPointApprDateBySystem(PointSettingMgmRequestDto requestDto) throws Exception {
        return promotionPointMapper.putPointApprDateBySystem(requestDto);
    }

	/**
	 * 회원등급별 후기 적립금 정보 조회
	 *
	 * @param urGroupId Long
	 * @return List<GoodsFeedbackPointRewardSettingVo>
	 */
	protected List<GoodsFeedbackPointRewardSettingVo> getGoodsFeedbackPointRewardSettingList(Long urGroupId) {
		return pointUseMapper.getGoodsFeedbackPointRewardSettingList(urGroupId);
	}

	/**
	 * @Desc 적립금 지급 목록 엑셀 다운로드 ( 승인요청 상태에서만 가능 )
	 * @param PointRequestDto : 적립금 지급 목록 검색 조건 request dto
	 * @return List<PointPayListVo> : 적립금 지급 목록 엑셀 다운로드
	 */
	@UserMaskingRun(system="BOS")
	public List<PointPayListVo> getPointPayListExportExcel(PointRequestDto pointRequestDto) {
		return promotionPointMapper.getPointPayListExportExcel(pointRequestDto);
	}

	protected List<PointSettingResultVo> getPmPointIdList(PointSettingMgmRequestDto requestDto) throws Exception {
		return promotionPointMapper.getPmPointIdList(requestDto);
	}

	public int removeGroupPointInfo(String grPmPointId) throws Exception {
		return promotionPointMapper.removeGroupPointInfo(grPmPointId);
	}
}

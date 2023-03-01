package kr.co.pulmuone.v1.promotion.point.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.exception.BosCustomException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.point.dto.*;
import kr.co.pulmuone.v1.promotion.point.dto.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PromotionPointBizImpl implements PromotionPointBiz {
    @Autowired
    PromotionPointService promotionPointService;

    @Autowired
    private ApprovalAuthBiz approvalAuthBiz;

	@Autowired
	private PointUseService pointUseService;

	public static final String DEPOSIT_POINT_EXCEEDED = "DEPOSIT_POINT_EXCEEDED";

    @Override
    public ApiResult<?> getPointSettingList(PointSettingListRequestDto pointSettingListRequestDto) throws Exception {
    	PointSettingListResponseDto result = new PointSettingListResponseDto();

        Page<PointSettingResultVo> pointSettingResultVoList = promotionPointService.getPointSettingList(pointSettingListRequestDto);

        result.setRows(pointSettingResultVoList.getResult());
        result.setTotal(pointSettingResultVoList.getTotal());

        return ApiResult.success(result);
    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public ApiResult<?> addPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{

    	//적립금 설정 저장
		ApiResult resultDto = promotionPointService.addPointSetting(pointSettingMgmRequestDto);
		PointSettingMgmRequestDto resultInfo = (PointSettingMgmRequestDto) resultDto.getData();
		List<PointSettingResultVo> pmPointIdList = new ArrayList();
		PointSettingResultVo pointSettingResultVo = new PointSettingResultVo();
		String pmPointId = "";
		boolean depositPointExceededCheck = false;

		if (ApprovalStatus.APPROVED_BY_SYSTEM.getCode().equals(resultInfo.getApprStat()) == true &&
				PointEnums.PointType.ADMIN.getCode().equals(pointSettingMgmRequestDto.getPointType())) {

			// 엑셀 대량지급건으로 등록한 케이스
			if(resultInfo.getGrPmPointId() != null) {
				pmPointIdList = promotionPointService.getPmPointIdList(resultInfo);
			} else {
				pmPointId = resultInfo.getPmPointId();
				pointSettingResultVo.setPmPointId(pmPointId);
				pmPointIdList.add(pointSettingResultVo);
			}



			if(PointEnums.PointPayment.PROVISION.getCode().equals(pointSettingMgmRequestDto.getPointPaymentType())) {
				List<UploadInfoVo> accountUserPointList = pointSettingMgmRequestDto.getUploadUserList();

				for(int i = 0; i < pmPointIdList.size(); i++) {
					//역할 사용 적립금 조회
					PointRequestDto pointRequestDto = PointRequestDto.builder()
							.pmPointId(pmPointIdList.get(i).toString())
							.build();
					UploadInfoVo vo = promotionPointService.getAdminAmountCheck(pointRequestDto);

					int issueValue = 0;
					if(accountUserPointList != null) {
						issueValue = accountUserPointList.size() * vo.getIssueVal();
					}else {
						issueValue = vo.getIssueVal();
					}
					// Validation 체크 역할그룹 총 적립금 확인   역할그룹 잔여 적립금 확인
					if(vo.getRoleValidityAmount() < (vo.getUseAmount() + issueValue)) {
						throw new BosCustomException(PointEnums.AdminPointCheck.USER_GROUP_POINT_LACK.getCode(), PointEnums.AdminPointCheck.USER_GROUP_POINT_LACK.getMessage());
					}
				}
			}

			for(int i = 0; i < pmPointIdList.size(); i++) {
				// 포인트 자동 승인 날짜 업데이트 처리
				resultInfo.setPmPointId(pmPointIdList.get(i).getPmPointId());
				promotionPointService.putPointApprDateBySystem(resultInfo);

				// 적립금 지급/차감 적립내역 처리
				ApiResult pointApiResult = pointUseService.applyAdminPoint(Long.parseLong(pmPointIdList.get(i).getPmPointId()));

				if ((boolean) pointApiResult.getData() == false) {
					throw new BaseException(pointApiResult.getMessage());
				}

				if(pointApiResult.getCode().equals(DEPOSIT_POINT_EXCEEDED)){
					depositPointExceededCheck = true;
				}
			}

		}

		if(depositPointExceededCheck) {
			return ApiResult.success(DEPOSIT_POINT_EXCEEDED);
		} else {
			return ApiResult.success();
		}

    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public ApiResult<?> putPointSetting(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{
        ApiResult resultDto;
		boolean depositPointExceededCheck = false;
        // 엑셀 대량 지급인 케이스는 따로 처리
        if(PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
            resultDto = promotionPointService.putExcelLargePayPointSetting(pointSettingMgmRequestDto);
        } else {
            resultDto = promotionPointService.putPointSetting(pointSettingMgmRequestDto);
        }

		PointSettingMgmRequestDto resultInfo = (PointSettingMgmRequestDto) resultDto.getData();

		if (ApprovalStatus.APPROVED_BY_SYSTEM.getCode().equals(resultInfo.getApprStat()) == true &&
				PointEnums.PointType.ADMIN.getCode().equals(pointSettingMgmRequestDto.getPointType())) {
			//역할 사용 적립금 조회
			String pmPointId = resultInfo.getPmPointId();

			if(PointEnums.PointPayment.PROVISION.getCode().equals(pointSettingMgmRequestDto.getPointPaymentType())) {
				List<UploadInfoVo> accountUserPointList = pointSettingMgmRequestDto.getUploadUserList();

				PointRequestDto pointRequestDto = PointRequestDto.builder()
						.pmPointId(pmPointId)
						.build();
				UploadInfoVo vo = promotionPointService.getAdminAmountCheck(pointRequestDto);

				int issueValue = 0;
				if(accountUserPointList != null) {
					issueValue = accountUserPointList.size() * vo.getIssueVal();
				}else {
					issueValue = vo.getIssueVal();
				}

				// Validation 체크 역할그룹 총 적립금 확인   역할그룹 잔여 적립금 확인
				if(vo.getRoleValidityAmount() < (vo.getUseAmount() + issueValue)) {
					throw new BosCustomException(PointEnums.AdminPointCheck.USER_GROUP_POINT_LACK.getCode(), PointEnums.AdminPointCheck.USER_GROUP_POINT_LACK.getMessage());
				}
			}

			// 적립금 지급/차감 적립내역 처리
			ApiResult pointApiResult = pointUseService.applyAdminPoint(Long.parseLong(pmPointId));
			if ((boolean) pointApiResult.getData() == false) {
				throw new BaseException(pointApiResult.getMessage());
			}

			if(pointApiResult.getCode().equals(DEPOSIT_POINT_EXCEEDED)){
				depositPointExceededCheck = true;
			}

			if(PointEnums.PayMethodTypeName.POINT_EXCEL_LARGE_PAY.getCode().equals(pointSettingMgmRequestDto.getPayMethodType())) {
				// 이전 GR_PM_POINT_ID로 PM_POINT 테이블에서 삭제처리
				promotionPointService.removeGroupPointInfo(resultInfo.getBeforeGrPmPointId());
			}
		}

		if(depositPointExceededCheck) {
			return ApiResult.success(DEPOSIT_POINT_EXCEEDED);
		} else {
			return ApiResult.success(resultInfo);
		}

    }

    @Override
    @UserMaskingRun(system="BOS")
    public ApiResult<?> getPointDetail(PointRequestDto pointRequestDto) throws Exception{
    	PointResponseDto result = new PointResponseDto();
    	result = promotionPointService.getPointDetail(pointRequestDto);
    	return ApiResult.success(result);
    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public ApiResult<?> putPointStatus(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception{
    	promotionPointService.putPointStatus(pointSettingMgmRequestDto);
    	return ApiResult.success();
    }


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public ApiResult<?> updatePointName(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {

		promotionPointService.updatePointName(pointSettingMgmRequestDto);

		// 이전 쿠폰정보 조회
		ApprovalStatusVo history  = promotionPointService.getPointStatusHistory(pointSettingMgmRequestDto, "PREV");

		if(history != null) {
			history.setApprStat(history.getPrevApprStat());
			history.setMasterStat(history.getPrevMasterStat());
			history.setApprSubUserId(history.getApprSubUserId());
			// 쿠폰상태이력 등록
			promotionPointService.addPointStatusHistory(history);
		}

		return ApiResult.success();

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public ApiResult<?> updatePointIssueReason(PointSettingMgmRequestDto pointSettingMgmRequestDto) throws Exception {

    	// 엑셀 대량 지급건이 아닌 경우
    	if(pointSettingMgmRequestDto.getGrPmPointId().equals(pointSettingMgmRequestDto.getPmPointId())) {
    		pointSettingMgmRequestDto.setGrPmPointId(null);
		}
		promotionPointService.updatePointIssueReason(pointSettingMgmRequestDto);

		// 이전 적립금상태 히스토리 조회
		List<PointSettingResultVo> pmPointIdList = promotionPointService.getPmPointIdList(pointSettingMgmRequestDto);

		for (int i = 0; i < pmPointIdList.size(); i++) {
			pointSettingMgmRequestDto.setPmPointId(pmPointIdList.get(i).toString());
			ApprovalStatusVo history  = promotionPointService.getPointStatusHistory(pointSettingMgmRequestDto, "PREV");
			if (history != null) {
				history.setApprStat(history.getPrevApprStat());
				history.setMasterStat(history.getPrevMasterStat());
				history.setApprSubUserId(history.getApprSubUserId());

				// 적립금상태이력 등록
				promotionPointService.addPointStatusHistory(history);
			}
		}


		return ApiResult.success();
	}


    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public ApiResult<?> removePoint(PointRequestDto pointRequestDto) throws Exception{

		// 이전 적립금정보 조회
    	PointSettingMgmRequestDto pointSettingMgmRequestDto = new PointSettingMgmRequestDto();
    	pointSettingMgmRequestDto.setPmPointId(pointRequestDto.getPmPointId());
    	ApprovalStatusVo history  = promotionPointService.getPointStatusHistory(pointSettingMgmRequestDto, "PREV");
		if(history != null) {
			// 적립금상태이력 등록
//			history.setApprStat(ApprovalStatus.DISPOSAL.getCode());
			history.setCreateId(pointRequestDto.getUserVo().getUserId());
			promotionPointService.addPointStatusHistory(history);
		}

    	promotionPointService.removePoint(pointRequestDto);
    	return ApiResult.success();
    }

    @Override
    public GetPointInfoResponseDto getPointInfo(Long urUserId) throws Exception {
        GetPointInfoResponseDto result = new GetPointInfoResponseDto();
        result.setPointUsable(promotionPointService.getPointUsable(urUserId));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDay = LocalDate.now().format(dateTimeFormatter);
        String endDay = LocalDate.now().plusDays(PromotionConstants.EXPIRED_DATE).format(dateTimeFormatter);
        result.setPointExpectExpired(promotionPointService.getPointExpectExpired(urUserId, startDay, endDay));

        return result;
    }

    @Override
    public ApiResult<?> getPointListByUser(CommonGetPointListByUserRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        dto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        return ApiResult.success(promotionPointService.getPointListByUser(dto));
    }

    @Override
    public ApiResult<?> getPointExpectExpireList() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDay = LocalDate.now().format(dateTimeFormatter);
        String endDay = LocalDate.now().plusDays(PromotionConstants.EXPIRED_DATE).format(dateTimeFormatter);

        return ApiResult.success(promotionPointService.getPointExpectExpireList(Long.parseLong(buyerVo.getUrUserId()), startDay, endDay));
    }

    @Override
    public CommonCheckAddPointValidationByUserResponseDto checkPointValidationByUser(Long urUserId, Long pmPointId) throws Exception {
        return promotionPointService.checkAddPointValidationByUser(urUserId, pmPointId);
    }

    @Override
    public int getPointUsable(Long urUserId) throws Exception {
        return promotionPointService.getPointUsable(urUserId);
    }


    /**
	 * 적립금승인 목록 조회
	 *
	 * @param PointApprovalRequestDto
	 * @return PointApprovalResponseDto
	 */
	@Override
	public ApiResult<?> getApprovalPointList(PointApprovalRequestDto requestDto) {
		return ApiResult.success(promotionPointService.getApprovalPointList(requestDto));
	}

	/**
     * 적립금승인 요청철회
     *
     * @param PointApprovalRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putCancelRequestApprovalPoint(PointApprovalRequestDto requestDto) throws Exception {

    	if(CollectionUtils.isNotEmpty(requestDto.getPmPointIdList())) {
    		//dto.getPmPointIdList() 숫자만큼 업데이트 되었는지도 확인 필요 유무, 정책 결정 대기
    		for(String pmPointId : requestDto.getPmPointIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_POINT.getCode(), pmPointId);

    			if(ApprovalEnums.ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
    				MessageCommEnum emums = promotionPointService.putCancelRequestApprovalPoint(approvalVo);
    				if(!BaseEnums.Default.SUCCESS.equals(emums)) {
    					throw new BaseException(emums);
    				}
    			}else {
    				//스킵? 혹은 계속진행? 결정대기중
    				return apiResult;
    			}
    		}
    	}
    	else return ApiResult.fail();

    	return ApiResult.success();
    }

	/**
     * 적립금승인 폐기 처리
     *
     * @param PointApprovalRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putDisposalApprovalPoint(PointApprovalRequestDto requestDto) throws Exception {

    	if(CollectionUtils.isNotEmpty(requestDto.getPmPointIdList())) {
    		//dto.getPmPointIdList() 숫자만큼 업데이트 되었는지도 확인 필요 유무, 정책 결정 대기
    		for(String pmPointId : requestDto.getPmPointIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_POINT.getCode(), pmPointId);

    			if(ApprovalEnums.ApprovalValidation.DISPOSABLE.getCode().equals(apiResult.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
    				MessageCommEnum emums = promotionPointService.putDisposalApprovalPoint(approvalVo);
    				if(!BaseEnums.Default.SUCCESS.equals(emums)) {
    					throw new BaseException(emums);
    				}
    			}else {
    				//스킵? 혹은 계속진행? 결정대기중
    				return apiResult;
    			}
    		}
    	}
    	else return ApiResult.fail();

    	return ApiResult.success();
    }
    /**
     * 적립금승인 처리
     *
     * @param PointApprovalRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putApprovalProcessPoint(PointApprovalRequestDto requestDto) throws Exception {

    	String reqApprStat = requestDto.getApprStat();
    	if(!ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)
    			&& !ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
    		return ApiResult.result(ApprovalEnums.ApprovalValidation.NONE_REQUEST);
    	}

    	List pmPointList = new ArrayList();
		boolean depositPointExceededCheck = false;

    	if(CollectionUtils.isNotEmpty(requestDto.getPmPointIdList())) {
    		//dto.getPmPointIdList() 숫자만큼 업데이트 되었는지도 확인 필요 유무, 정책 결정 대기
			for (String pmPointId : requestDto.getPmPointIdList()) {
				if(pmPointList.size() > 0) {
					if(pmPointList.contains(pmPointId)) {
						continue;
					}
				}
				pmPointList.add(pmPointId);
				ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_POINT.getCode(), pmPointId);

				if (ApprovalEnums.ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
					ApprovalStatusVo approvalVo = (ApprovalStatusVo) apiResult.getData();
					if (ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
						approvalVo.setApprStat(reqApprStat);
						approvalVo.setStatusComment(requestDto.getStatusComment());
					}
					if (ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
							&& ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode().equals(approvalVo.getApprStat())
					) {
						PointRequestDto dto = new PointRequestDto();
						dto.setPmPointId(pmPointId);
						PointResponseDto result = new PointResponseDto();
						result = promotionPointService.getPointDetail(dto);
						if (PointEnums.PointType.ADMIN.getCode().equals(result.getRows().getPointType())
							//    							&& result.getRows().getPointMasterStat().equals(PointEnums.PointMasterStatus.APPROVED.getCode())
						) {        // 관리자  지급/차감 승인

							// 관리자 지급/차감 : 지급 Case
							if (PointEnums.PointPayment.PROVISION.getCode().equals(result.getRows().getPointPaymentType())) {
								//역할 사용 적립금 조회
								UploadInfoVo vo = promotionPointService.getAdminAmountCheck(dto);
								// Validation 체크 역할그룹 총 적립금 확인   역할그룹 잔여 적립금 확인
								if (vo.getRoleValidityAmount() < (vo.getUseAmount() + vo.getIssueVal())) {
									throw new BosCustomException(PointEnums.AdminPointCheck.APPROVED_INCREASE_POINT_GROUP.getCode(), PointEnums.AdminPointCheck.APPROVED_INCREASE_POINT_GROUP.getMessage());
								}
							}

						}

					}
					if (ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
							&& ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())
					) {
						approvalVo.setMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
						PointRequestDto dto = new PointRequestDto();
						dto.setPmPointId(pmPointId);
						PointResponseDto result = new PointResponseDto();
						result = promotionPointService.getPointDetail(dto);

						if (PointEnums.PointType.SERIAL_NUMBER.getCode().equals(result.getRows().getPointType())) {                        // 이용권 승인

							promotionPointService.putSerialNumberStatus(dto);
						} else if (PointEnums.PointType.ADMIN.getCode().equals(result.getRows().getPointType())
						) {        // 관리자  지급/차감 승인

							// 관리자 지급/차감 : 지급 Case
							if (PointEnums.PointPayment.PROVISION.getCode().equals(result.getRows().getPointPaymentType())) {

								//역할 사용 적립금 조회
								UploadInfoVo vo = promotionPointService.getAdminAmountCheck(dto);
								// Validation 체크 역할그룹 총 적립금 확인   역할그룹 잔여 적립금 확인
								if (vo.getRoleValidityAmount() < (vo.getUseAmount() + vo.getIssueVal())) {
									throw new BosCustomException(PointEnums.AdminPointCheck.APPROVED_INCREASE_POINT_GROUP.getCode(), PointEnums.AdminPointCheck.APPROVED_INCREASE_POINT_GROUP.getMessage());
								}
							}


							// 적립금 지급/차감 적립내역 처리
							ApiResult pointApiResult = pointUseService.applyAdminPoint(Long.parseLong(pmPointId));

							if ((boolean) pointApiResult.getData() == false) {
								log.error("관리자 적립/차감 처리 실패 : " + pointApiResult.getCode() + pointApiResult.getMessage());
								throw new BosCustomException(PointEnums.AdminPointCheck.APPLY_ADMIN_POINT_FAIL.getCode(), pointApiResult.getMessage());
							}

							// 적립금 초과로 미지급인 건이 있는 경우
							if(pointApiResult.getCode().equals(DEPOSIT_POINT_EXCEEDED)){
								depositPointExceededCheck = true;
							}

						}

					}
					MessageCommEnum emums = promotionPointService.putApprovalProcessPoint(approvalVo);
					if (!BaseEnums.Default.SUCCESS.equals(emums)) {
						throw new BaseException(emums);
					}
				} else {
					//스킵? 혹은 계속진행? 결정대기중
					return apiResult;
					//    				AUTH_DENIED("AUTH_DENIED", "허가되지 않은 권한입니다."),
					//    				NONE_REQUEST("NONE_REQUEST", "승인요청 상태가 없습니다."),
					//    				ALREADY_APPROVAL_REQUEST("ALREADY_APPROVAL_REQUEST", "이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
					//    				ALREADY_APPROVED("ALREADY_APPROVED", "이미 승인이 완료되어 철회가 불가능합니다."),
					//    				ALREADY_DENIED("ALREADY_DENIED", "이미 승인이 반려되었습니다. 반려 정보를 확인해 주세요."),
					//    				ALREADY_CANCEL_REQUEST("ALREADY_CANCEL_REQUEST", "승인요청자가 승인요청을 철회하였습니다."),
					//    				REQUIRED_APPROVAL_USER("REQUIRED_APPROVAL_USER", "최종승인관리자는 1명이상 등록되거나 정상상태이어야 합니다.");
				}
			}
    	}
    	else return ApiResult.fail();

    	if(depositPointExceededCheck) {
    		return ApiResult.success(DEPOSIT_POINT_EXCEEDED);
    	} else {
			return ApiResult.success();
		}

    }

	/**
	 * 적립금 조회 - 소멸예정 - 적립금 소멸예정 자동메일 발송용
	 *
	 * @param urUserId
	 * @return PointExpiredForEmailVo
	 */
	@Override
	public PointExpiredForEmailVo getPointExpectExpiredForEmail(Long urUserId) throws Exception{
		return promotionPointService.getPointExpectExpiredForEmail(urUserId);
	}

    /**
	 * 적립금 소멸예정 목록 조회 - 적립금 소멸예정 자동메일 발송용
	 *
	 * @param urUserId
	 * @return List<PointExpiredListForEmailVo>
	 */
	@Override
	public List<PointExpiredListForEmailVo> getPointExpectExpireListForEmail(Long urUserId) throws Exception{
		return promotionPointService.getPointExpectExpireListForEmail(urUserId);
	}

    @Override
    public ApiResult<?> getEventCallPointInfo(PointRequestDto pointRequestDto) {
        return promotionPointService.getEventCallPointInfo(pointRequestDto);
    }


    @Override
    public ApiResult<?> getPointSearchStatus(PointRequestDto pointRequestDto) {
        return promotionPointService.getPointSearchStatus(pointRequestDto);
    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public ApiResult<?> putTicketCollectStatus(PointRequestDto pointRequestDto) throws Exception {
    	promotionPointService.putTicketCollectStatus(pointRequestDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getAdminAmountCheck(PointRequestDto pointRequestDto) throws Exception{
    	UploadInfoVo result = new UploadInfoVo();
    	result = promotionPointService.getAdminAmountCheck(pointRequestDto);
    	return ApiResult.success(result);
    }

	@Override
	public List<GoodsFeedbackPointRewardSettingVo> getGoodsFeedbackPointRewardSettingList(Long urGroupId) {
		return promotionPointService.getGoodsFeedbackPointRewardSettingList(urGroupId);
	}

	/**
	 * @Desc 적립금 지급 목록 엑셀 다운로드 ( 승인요청 상태에서만 가능 )
	 * @param PointRequestDto : 적립금 지급 목록 검색 조건 request dto
	 * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
	 */
	public ExcelDownloadDto getPointPayListExportExcel(PointRequestDto pointRequestDto) {

		String excelFileName = "적립금 지급 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = {200, 200, 200};

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = {"center", "center", "center"};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = {"loginId", "userNm", "issueVal"};

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"회원 ID", "회원명", "적립금"};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼
		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
		 * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
		 */
		List<PointPayListVo> itemList = promotionPointService.getPointPayListExportExcel(pointRequestDto);

		firstWorkSheetDto.setExcelDataList(itemList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}


}

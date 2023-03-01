package kr.co.pulmuone.v1.approval.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalDuplicateRequestVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskInfoDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthManagerHistoryByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalStatusHistoryDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthInfoVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitTp;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalValidation;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.approval.auth.ApprovalAuthMapper;
import kr.co.pulmuone.v1.comm.util.ApprovalUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

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
 *  1.0		20210114		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalAuthService {

	@Autowired
	private final ApprovalAuthMapper approvalAuthMapper;

	/**
     * 업무별 승인권한 관리 목록 조회
     *
     * @param ArrayList<GetCodeListResultVo>
     * @return ApprovalAuthByTaskDto
     */
    protected ApprovalAuthByTaskDto getApprovalAuthByTaskList(List<GetCodeListResultVo> taskList) {
    	ApprovalAuthByTaskDto result = new ApprovalAuthByTaskDto();
    	List<ApprovalAuthByTaskInfoDto> rows = new ArrayList<ApprovalAuthByTaskInfoDto>();

    	for(GetCodeListResultVo codeVo : taskList) {
    		ApprovalAuthByTaskInfoDto taskDto = new  ApprovalAuthByTaskInfoDto();
    		taskDto.setTaskCode(codeVo.getCode());
    		taskDto.setTaskName(codeVo.getName());
			taskDto.setAuthManager1stList(approvalAuthMapper.getApprovalAuthManagerList(taskDto.getTaskCode(), ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode()));
			taskDto.setAuthManager2ndList(approvalAuthMapper.getApprovalAuthManagerList(taskDto.getTaskCode(), ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode()));
			rows.add(taskDto);
        }

    	result.setRows(rows);
    	return result;
    }
    /**
     * 업무별 승인권한 승인 관리자 조회
     *
     * @param taskCode
     * @return ApprovalAuthByTaskDto
     */
    protected ApprovalAuthByTaskInfoDto getApprovalAuthByTaskInfo(String taskCode) {

    	ApprovalAuthByTaskInfoDto taskDto = new  ApprovalAuthByTaskInfoDto();

		taskDto.setAuthManager1stList(approvalAuthMapper.getApprovalAuthManagerList(taskCode, ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode()));
		taskDto.setAuthManager2ndList(approvalAuthMapper.getApprovalAuthManagerList(taskCode, ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode()));

		return taskDto;
    }

    /**
     * 업무별 승인관리자 정보 저장
     *
     * @param ApprovalAuthByTaskInfoDto
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalAuthByTaskInfo(ApprovalAuthByTaskInfoDto reqData) throws Exception {
    	int addCnt = 0;

    	//validation 후에 일괄 업데이트?

    	//1차승인 목록은 선택값
    	//기존 1차승인 데이터 삭제 - 기존값 없어서 0일수도 있음
    	//1차승인 목록은 있는데, 비정상 데이터, .USER_ID 가 없으면 비정상데이터
    	//입력받은 목록 저장 - 입력값 갯수 != 저장값 갯수 < exception
    	//1차승인 목록 없으면 패스(삭제)
    	approvalAuthMapper.delApprovalAuthManager(reqData.getTaskCode(), ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode());
    	/*2차승인자를 1차승인자로 옮기면, DB Duplicate Key 오류 발생으로 1차 승인자 저장이 불가능하므로, 승인자 항목을 2차도 먼저 삭제 처리하고 진행
		2021-05-18 임상건 작업*/
		approvalAuthMapper.delApprovalAuthManager(reqData.getTaskCode(), ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode());

		if(CollectionUtils.isNotEmpty(reqData.getAuthManager1stList())) {
			for(ApprovalAuthManagerVo managerVo : reqData.getAuthManager1stList()) {
				if(StringUtil.isEmpty(managerVo.getApprUserId())) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);

				managerVo.setApprKindType(reqData.getTaskCode());
				managerVo.setApprManagerType(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode());
				addCnt += approvalAuthMapper.addApprovalAuthManager(managerVo);

			}
			if(addCnt != reqData.getAuthManager1stList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
		}
		//2차승인 목록은 필수값
		//목록 O, user_id O 인데, status가 정상, 일시정지 상태가 1명도 없는 경우, 최종승인관리자는 최소 1명이어야 함. > exception
		//2차승인 목록은 있는데, user_id 없으면 비정상데이터
		//기존 2차승인 데이터 삭제 - 기존값 없어서 0일수도 있음
		//입력받은 목록 저장 - 입력값 갯수 != 저장값 갯수 < exception
		if(CollectionUtils.isNotEmpty(reqData.getAuthManager2ndList())) {
			MessageCommEnum enums = this.validationApprovalAuthManager(reqData.getAuthManager2ndList());
			addCnt = 0;
			if(enums.getCode().equals(ApiResult.success().getCode())){
				for(ApprovalAuthManagerVo managerVo : reqData.getAuthManager2ndList()) {
    				if(StringUtil.isEmpty(managerVo.getApprUserId())) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);

    				managerVo.setApprKindType(reqData.getTaskCode());
    				managerVo.setApprManagerType(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode());
    				addCnt += approvalAuthMapper.addApprovalAuthManager(managerVo);

    			}
    		}
    		if(addCnt != reqData.getAuthManager2ndList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    	}else {
    		return BaseEnums.Default.FAIL;
    	}

    	return BaseEnums.Default.SUCCESS;
    }

    protected MessageCommEnum validationApprovalAuthManager(List<ApprovalAuthManagerVo> managerList) {
    	int authManagerNormalCnt = 0;

    	for(ApprovalAuthManagerVo managerVo : managerList) {
			if(StringUtil.isEmpty(managerVo.getApprUserId())){
				return BaseEnums.Default.FAIL;
			}
			/*
			 * managerVo.getUserStatus() 를 DB로 다시 조회?
			 */
			if(ApprovalUtil.isAbleEmployeeStatus(managerVo.getUserStatus())) authManagerNormalCnt++;
		}
    	if(authManagerNormalCnt < 1 ){
    		return ApprovalEnums.ApprovalValidation.REQUIRED_APPROVAL_USER;
		}
    	return BaseEnums.Default.SUCCESS;
    }

    /**
     * 업무별 직전 승인관리자 이력 조회
     *
     * @param taskCode
     * @return ApprovalAuthManagerHistoryByTaskDto
     */
    protected ApprovalAuthManagerHistoryByTaskDto getApprovalAuthManagerHistoryByTask(String taskCode) {

    	UserVo userVo = SessionUtil.getBosUserVO();
    	String userId = "";
    	if (userVo != null) {
    		userId = userVo.getUserId();
    	}

    	ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();

    	if(ApprovalAuthType.APPR_KIND_TP_COUPON.getCode().equals(taskCode)) {
    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryCoupon(userId);
    	}else if(ApprovalAuthType.APPR_KIND_TP_POINT.getCode().equals(taskCode)) {
    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryPoint(userId);
    	}else if(ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode().equals(taskCode)) {
    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryExhibit(userId, taskCode);
    	}else if(ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode().equals(taskCode)) {
    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryExhibit(userId, taskCode);
    	}else if(ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode().equals(taskCode)) {
    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryItem(userId, taskCode);
    	}else if(ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(taskCode)) {
    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryGoods(userId, taskCode);
    	}else if(ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode)) {
			apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryItemPrice(userId);
		}else if(ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)) {
			apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryItem(userId, taskCode);
		}else if(ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode)) {
			apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryGoods(userId, taskCode);
		}else if(ApprovalAuthType.APPR_KIND_TP_CS_REFUND.getCode().equals(taskCode)) {
			apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryCsRefund(userId);
		}else if(ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode)) {
			apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryGoodsDiscount(userId);
		}

//    	}else if(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ETC.getCode().equals(taskCode)) {
//    		apprManagerHistory = approvalAuthMapper.getApprovalAuthManagerHistoryETC(userId);
//    	}
    	return apprManagerHistory;
    }

    /**
     * 업무별 승인처리 정보 조회(승인담당자, 승인상태)
     *
     * @param taskCode, taskId
     * @return ApprovalAuthInfoVo
     */
    protected ApprovalAuthInfoVo getApprovalProcessInfo(String taskCode, String taskPk) {

    	ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();

    	if(ApprovalAuthType.APPR_KIND_TP_COUPON.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessCouponInfo(taskPk);
    	}else if(ApprovalAuthType.APPR_KIND_TP_POINT.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessPointInfo(taskPk);
    	}else if(ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessExhibitInfo(taskPk);
    	}else if(ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessExhibitInfo(taskPk);
    	}else if(ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessItemRegistInfo(taskPk);
    	}else if(ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessGoodsRegistInfo(taskPk);
    	}else if(ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode)) {
			approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessItemPriceInfo(taskPk);
		}else if(ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessItemRegistInfo(taskPk);
		}else if(ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode)) {
			approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessGoodsRegistInfo(taskPk);
		}else if(ApprovalAuthType.APPR_KIND_TP_CS_REFUND.getCode().equals(taskCode)) {
    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessCsRefundInfo(taskPk);
		}else if(ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode)) {
			approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessGoodsDiscountInfo(taskPk);
		}else if(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_CS_REFUND.getCode().equals(taskCode)) {
			approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessCsRefundInfo(taskPk);
//    	}else if(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ETC.getCode().equals(taskCode)) {
//    		approvalAuthInfoVo = approvalAuthMapper.getApprovalProcessETCInfo(taskPk);
    	}

    	approvalAuthInfoVo.setTaskCode(taskCode);
    	return approvalAuthInfoVo;
    }

    /**
     * 승인요청자 승인철회 가능한 상태 체크(승인요청자, 승인상태)
     *
     * @param ApprovalAuthInfoVo
     * @return MessageCommEnum
     */
    protected ApiResult<?> checkCancelable(ApprovalAuthInfoVo approvalAuthInfoVo) {
    	MessageCommEnum enums = ApprovalEnums.ApprovalValidation.UNCANCELABLE;

    	UserVo userVo = SessionUtil.getBosUserVO();
    	String userId = "";
    	if (userVo != null) {
    		userId = userVo.getUserId();
    	}
    	if(StringUtil.isEmpty(userId)) return ApiResult.result(BaseEnums.CommBase.NEED_LOGIN);

    	ApprovalStatusVo apprVo = ApprovalStatusVo.builder()
				.taskPk(approvalAuthInfoVo.getTaskPk())
				.prevMasterStat(approvalAuthInfoVo.getMasterStat())
				.prevApprStat(approvalAuthInfoVo.getApprStat())
				.approvalRequestUserId(approvalAuthInfoVo.getApprReqUserId())
				.ilItemCd(approvalAuthInfoVo.getIlItemCd())
				.ilGoodsId(approvalAuthInfoVo.getIlGoodsId())
				.build();

    	if (
    		ApprovalEnums.ApprovalStatus.REQUEST.getCode().equals(approvalAuthInfoVo.getApprStat())
    		&& (
    			ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(approvalAuthInfoVo.getTaskCode()) // 품목가격 승인 요청철회는 요청 당사자가 아니어도 가능
    			|| ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(approvalAuthInfoVo.getTaskCode()) // 상품할인 승인 요청철회는 요청 당사자가 아니어도 가능
    			|| userId.equals(approvalAuthInfoVo.getApprReqUserId())
    		)
    	) {
    		enums = ApprovalEnums.ApprovalValidation.CANCELABLE;
			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.CANCEL.getCode());
    	}
    	else enums = ApprovalEnums.ApprovalValidation.AUTH_DENIED;

    	return ApiResult.result(apprVo, enums);
    }
    /**
     * 승인담담자 승인가능 상태 체크(승인담당자, 승인상태)
     *
     * @param ApprovalAuthInfoVo
     * @return MessageCommEnum
     */
    protected ApiResult<?> checkApprovable(ApprovalAuthInfoVo approvalAuthInfoVo) {
    	MessageCommEnum enums = ApprovalEnums.ApprovalValidation.NOTAPPROVABLE;

    	UserVo userVo = SessionUtil.getBosUserVO();
    	String userId = "";
    	if (userVo != null) {
    		userId = userVo.getUserId();
    	}

    	if(StringUtil.isEmpty(userId)) return ApiResult.result(BaseEnums.CommBase.NEED_LOGIN);

    	ApprovalStatusVo apprVo = ApprovalStatusVo.builder()
				.taskPk(approvalAuthInfoVo.getTaskPk())
				.prevMasterStat(approvalAuthInfoVo.getMasterStat())
				.prevApprStat(approvalAuthInfoVo.getApprStat())
				.approvalRequestUserId(approvalAuthInfoVo.getApprReqUserId())
				.apprUserId(approvalAuthInfoVo.getApprUserId())
				.apprSubUserId(approvalAuthInfoVo.getApprSubUserId())
				.ilItemCd(approvalAuthInfoVo.getIlItemCd())
				.ilGoodsId(approvalAuthInfoVo.getIlGoodsId())
				.discountTp(approvalAuthInfoVo.getDiscountTp())
				.build();

    	if(ApprovalEnums.ApprovalStatus.REQUEST.getCode().equals(approvalAuthInfoVo.getApprStat())){

    		if((userId.equals(approvalAuthInfoVo.getApprSubUserId()) && ApprovalUtil.isAbleEmployeeStatus(approvalAuthInfoVo.getApprSubUserStatus()))
				|| (userId.equals(approvalAuthInfoVo.getGrantSubAuthUserId()) && ApprovalUtil.isAbleEmployeeStatus(approvalAuthInfoVo.getGrantSubUserStatus()))
				) {
    			enums = ApprovalEnums.ApprovalValidation.APPROVABLE;
    			apprVo.setApprStep(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode());
    			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode());
    		}

			else if(StringUtil.isEmpty(approvalAuthInfoVo.getApprSubUserId())
					&& ((userId.equals(approvalAuthInfoVo.getApprUserId()) && ApprovalUtil.isAbleEmployeeStatus(approvalAuthInfoVo.getApprUserStatus()))
							|| (userId.equals(approvalAuthInfoVo.getGrantAuthUserId()) && ApprovalUtil.isAbleEmployeeStatus(approvalAuthInfoVo.getGrantUserStatus()))
						)
				) {
				enums = ApprovalEnums.ApprovalValidation.APPROVABLE;
				apprVo.setApprStep(ApprovalEnums.ApprovalStatus.APPROVED.getCode());
    			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.APPROVED.getCode());
			}

			else enums = ApprovalEnums.ApprovalValidation.AUTH_DENIED;

		}else if(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode().equals(approvalAuthInfoVo.getApprStat())){

			if((userId.equals(approvalAuthInfoVo.getApprUserId()) && ApprovalUtil.isAbleEmployeeStatus(approvalAuthInfoVo.getApprUserStatus()))
				|| (userId.equals(approvalAuthInfoVo.getGrantAuthUserId()) && ApprovalUtil.isAbleEmployeeStatus(approvalAuthInfoVo.getGrantUserStatus()))
				) {
				enums = ApprovalEnums.ApprovalValidation.APPROVABLE;
				apprVo.setApprStep(ApprovalEnums.ApprovalStatus.APPROVED.getCode());
    			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.APPROVED.getCode());
			}

			else enums = ApprovalEnums.ApprovalValidation.REQUEST_ALREADY_APPROVED;
		}

    	return ApiResult.result(apprVo, enums);
    }

    /**
     * 승인요청자 폐기 가능한 상태 체크(승인요청자, 승인상태)
     *
     * @param ApprovalAuthInfoVo
     * @return MessageCommEnum
     */
    protected ApiResult<?> checkDisposable(String taskCode, String taskPk) {
    	MessageCommEnum enums = ApprovalEnums.ApprovalValidation.UNDISPOSABLE;

    	UserVo userVo = (UserVo) SessionUtil.getBosUserVO();
    	String userId = "";
    	if (userVo != null) {
    		userId = userVo.getUserId();
    	}
    	if(StringUtil.isEmpty(userId)) return ApiResult.result(BaseEnums.CommBase.NEED_LOGIN);

    	ApprovalAuthInfoVo approvalAuthInfoVo = this.getApprovalProcessInfo(taskCode, taskPk);

    	//철회가능상태 - 요청
    	String apprStatus = approvalAuthInfoVo.getApprStat();

    	ApprovalStatusVo apprVo = ApprovalStatusVo.builder()
				.taskPk(approvalAuthInfoVo.getTaskPk())
				.prevMasterStat(approvalAuthInfoVo.getMasterStat())
				.prevApprStat(apprStatus)
				.build();

    	if(StringUtil.isEmpty(apprStatus)
    			|| ApprovalEnums.ApprovalStatus.NONE.getCode().equals(apprStatus)
    			) {
    		enums = ApprovalEnums.ApprovalValidation.NONE_REQUEST;
    	}
    	else if(ApprovalEnums.ApprovalStatus.CANCEL.getCode().equals(apprStatus)
    			|| ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(apprStatus)
    			){

    		if (
				ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode) // 품목가격등록
				|| ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode) // 상품할인등록
				|| ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode) // 거래처품목수정
				|| ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode) // 거래처상품수정
			) {
    			if (userId.equals(approvalAuthInfoVo.getApprReqUserId())) {
        	    	enums = ApprovalEnums.ApprovalValidation.DISPOSABLE;
        			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode());
    			}
    			else {
    				enums = ApprovalEnums.ApprovalValidation.AUTH_DENIED;
    			}
    		}
    		else {
    	    	enums = ApprovalEnums.ApprovalValidation.DISPOSABLE;
    			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode());
    		}

    	}
    	else if(ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode().equals(apprStatus)){

    		enums = ApprovalEnums.ApprovalValidation.ALREADY_APPROVED;
    	}
    	else if(ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(apprStatus)){
    		if (
				ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode) // 품목가격등록
				|| ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode) // 상품할인등록
			) {
    	    	enums = ApprovalEnums.ApprovalValidation.DISPOSABLE;
    			apprVo.setApprStat(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode());
    		}
    		else {
        		enums = ApprovalEnums.ApprovalValidation.ALREADY_APPROVED;
    		}
    	}
    	else if(ApprovalEnums.ApprovalStatus.REQUEST.getCode().equals(apprStatus)){

    		enums = ApprovalEnums.ApprovalValidation.UNDISPOSABLE;
    	}
    	else {
    		//상태이상.
    		enums = ApprovalEnums.ApprovalValidation.NONE_REQUEST;
    	}
    	return ApiResult.result(apprVo, enums);
    }

    /**
     * 업무별 승인내역 이력 조회
     *
     * @param taskCode, taskPk
     * @return ApprovalAuthManagerHistoryByTaskDto
     */
    protected ApprovalStatusHistoryDto getApprovalHistory(String taskCode, String taskPk) {

    	ApprovalStatusHistoryDto result = new ApprovalStatusHistoryDto();

    	List<ApprovalStatusVo> rows = new ArrayList<>();

		if (ApprovalAuthType.APPR_KIND_TP_COUPON.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryCoupon(taskPk);
		} else if (ApprovalAuthType.APPR_KIND_TP_POINT.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryPoint(taskPk);
		} else if (ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryExhibit(taskPk, ExhibitTp.SELECT.getCode());
		} else if (ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryExhibit(taskPk, ExhibitTp.GIFT.getCode());
		} else if (ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryGoods(taskPk, ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode());
		} else if (ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryGoods(taskPk, ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
		} else if (ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryItem(taskPk, ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode());
		} else if (ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryItem(taskPk, ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode());
		} else if (ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode)) {
			rows = approvalAuthMapper.getApprovalHistoryItemPrice(taskPk);
		}

//    	else if(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ETC.getCode().equals(taskCode)) {
//    		rows = approvalAuthMapper.getApprovalHistoryETC(taskPk);
//    	}

		result.setRows(rows);
		return result;
    }

    /**
	 * 품목 상품 등록 거래처 관련 중복 승인 정보 조회
	 * @return
	 */
	protected ApiResult<?> getDuplicateApprGoodsItemRequest(String taskCode, String taskPk) throws Exception {
		String itemCode = this.getApprItemCode(taskCode, taskPk);
		List<ApprovalDuplicateRequestVo> duplicateResultList = new ArrayList<>();
		List<ApprovalDuplicateRequestVo> duplicateRequestList = approvalAuthMapper.getDuplicateApprovalRegist(itemCode);
		if (ObjectUtils.isEmpty(duplicateRequestList)) {
			log.error("승인 대상이 존재하지 않습니다.");
		}

		if(ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)) {

			duplicateResultList = duplicateRequestList.stream()
										.filter(e -> ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(e.getApprKindTp()))
										.filter(e -> ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(e.getApprKindTp()))
										.filter(e -> ApprovalStatus.REQUEST.getCode().equals(e.getApprStat())
												  || ApprovalStatus.SUB_APPROVED.getCode().equals(e.getApprStat()))
										.collect(Collectors.toList());

		} else if(ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(taskCode)) {

			duplicateResultList = duplicateRequestList.stream()
										.filter(e -> ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(e.getApprKindTp()))
										.filter(e -> ApprovalStatus.REQUEST.getCode().equals(e.getApprStat())
												  || ApprovalStatus.SUB_APPROVED.getCode().equals(e.getApprStat()))
										.collect(Collectors.toList());

    	} else if(ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode)) {

			duplicateResultList = duplicateRequestList.stream()
										.filter(e -> ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(e.getApprKindTp()))
										.filter(e -> ApprovalStatus.REQUEST.getCode().equals(e.getApprStat())
												  || ApprovalStatus.SUB_APPROVED.getCode().equals(e.getApprStat()))
										.collect(Collectors.toList());

		}

		if (!ObjectUtils.isEmpty(duplicateResultList)) {
			duplicateResultList.forEach(e ->
					log.error(String.join(":", e.getApprId(), e.getApprKindTp(), e.getApprStat()))
			);
			return ApiResult.result(duplicateRequestList, ApprovalValidation.DUPLICATE_APPROVAL_REQUEST);
		}

		return ApiResult.success();

	}


	/**
	 * 품목 가격 상품 할인 관련 중복 승인 정보 조회
	 * @return
	 */
	protected ApiResult<?> getDuplicateApprGoodsItemPriceDiscountByItemCode(String taskCode, String itemCode) {
		List<ApprovalDuplicateRequestVo> duplicateResultList = new ArrayList<>();
		List<ApprovalDuplicateRequestVo> duplicateRequestList = approvalAuthMapper.getDuplicateApprovalPrice(itemCode);

		if (ObjectUtils.isEmpty(duplicateRequestList)) {
			log.error("승인 대상이 존재하지 않습니다.");
		}

		if(ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode)) {

			duplicateResultList = duplicateRequestList.stream()
										.filter(e -> ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(e.getApprKindTp()))
										.filter(e -> ApprovalStatus.REQUEST.getCode().equals(e.getApprStat())
												  || ApprovalStatus.SUB_APPROVED.getCode().equals(e.getApprStat()))
										.collect(Collectors.toList());

		} else if(ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode)) {

			duplicateResultList = duplicateRequestList.stream()
										.filter(e -> ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(e.getApprKindTp()))
										.filter(e -> ApprovalStatus.REQUEST.getCode().equals(e.getApprStat())
												  || ApprovalStatus.SUB_APPROVED.getCode().equals(e.getApprStat()))
										.collect(Collectors.toList());

    	}

		if (!ObjectUtils.isEmpty(duplicateResultList)) {
			duplicateResultList.forEach(e ->
					log.error(String.join(":", e.getApprId(), e.getApprKindTp(), e.getApprStat()))
			);
			return ApiResult.result(duplicateRequestList, ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode) ?
					ApprovalEnums.ApprovalValidation.HAS_GOODS_DISCOUNT_APPR : ApprovalEnums.ApprovalValidation.HAS_ITEM_PRICE_APPR);
		}

		return ApiResult.success();
	}

	/**
	 * 품목 가격 상품 할인 관련 중복 승인 정보 조회
	 * @return
	 */
	protected ApiResult<?> getDuplicateApprGoodsItemPriceDiscount(String taskCode, String taskPk) {
		String itemCode = this.getApprItemCode(taskCode, taskPk);
    	return getDuplicateApprGoodsItemPriceDiscountByItemCode(taskCode, itemCode);
	}

	/**
	 * 상품 코드 가져오기
	 * @param taskCode
	 * @param taskPk
	 * @return
	 */
	private String getApprItemCode(String taskCode, String taskPk) {

    	if (ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)) {

    		return approvalAuthMapper.getItemApprItemCode(taskPk);

		} else if (ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode)) {

			return approvalAuthMapper.getGoodsApprItemCode(taskPk);

		} else if (ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode)) {

			return approvalAuthMapper.getItemPriceApprItemCode(taskPk);

		} else if (ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode)) {

			return approvalAuthMapper.getGoodsDiscountApprItemCode(taskPk);

		}

    	return null;

	}

}

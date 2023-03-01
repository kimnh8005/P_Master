package kr.co.pulmuone.v1.approval.auth.service;

import kr.co.pulmuone.v1.comm.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskInfoDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthInfoVo;
import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalValidation;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;

@Service
public class ApprovalAuthBizImpl implements ApprovalAuthBiz {

    @Autowired
    private ApprovalAuthService approvalAuthService;

    @Autowired
    private StComnBiz stComnBiz;


	/**
	 * 현재 승인 요청 상태 조회
	 * @param taskCode
	 * @param taskPk
	 * @return
	 */
    public ApiResult<?> getApprovailProcessBizInfo(String taskCode, String taskPk) {
    	return ApiResult.success(approvalAuthService.getApprovalProcessInfo(taskCode, taskPk));
	}


    /**
     * 업무별 승인권한 관리 목록 조회
     *
     * @param masterApprovalKindType
     * @return ApprovalAuthByTaskDto
     */
    public ApiResult<?> getApprovalAuthByTaskList(String masterApprovalKindType) {

    	GetCodeListRequestDto codeListReqDto = new GetCodeListRequestDto();
    	codeListReqDto.setStCommonCodeMasterCode(masterApprovalKindType);
    	codeListReqDto.setUseYn("Y");
    	ApiResult<?> codeListApi =	stComnBiz.getCodeList(codeListReqDto);

    	if(codeListApi.getCode().equals(ApiResult.success().getCode()))
    		return ApiResult.success(approvalAuthService.getApprovalAuthByTaskList(((GetCodeListResponseDto)codeListApi.getData()).getRows()));
    	else
    		return codeListApi;
    }
    /**
     * 업무별 승인관리자 정보 조회
     *
     * @param taskCode
     * @return ApprovalAuthByTaskInfoDto
     */
    public ApiResult<?> getApprovalAuthByTaskInfo(String taskCode) {
    	ApprovalAuthByTaskInfoDto taskDto = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);

    	taskDto.setTaskCode(taskCode);
		taskDto.setSubRequired(false);
		taskDto.setTaskName(stComnBiz.getCodeName(taskCode));
		taskDto.setApprReqUsrId(SessionUtil.getBosUserVO().getUserId());
		taskDto.setApprReqLoginId(SessionUtil.getBosUserVO().getLoginId());

    	if (ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode)) {
			taskDto.setSubRequired(true);
		}

    	return ApiResult.success(taskDto);
    }
    /**
     * 업무별 승인관리자 정보 저장
     *
     * @param ApprovalAuthByTaskInfoDto
     * @return int
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putApprovalAuthByTaskInfo(ApprovalAuthByTaskInfoDto reqData) throws Exception {
    	return ApiResult.result(approvalAuthService.putApprovalAuthByTaskInfo(reqData));
    }
    /**
     * 업무별 직전 승인관리자 이력 조회
     *
     * @param taskCode
     * @return ApprovalAuthByTaskInfoDto
     */
    public ApiResult<?> getApprovalAuthManagerHistoryByTask(String taskCode) {
    	return ApiResult.success(approvalAuthService.getApprovalAuthManagerHistoryByTask(taskCode));
    }
    /**
     * 승인철회 검증
     * 승인요청자 승인철회 가능한 상태 체크(승인요청자, 승인상태)
     *
     * @param taskCode, taskPk
     * @return ApprovalAuthInfoVo
     */
    public ApiResult<?> checkCancelable(String taskCode, String taskPk) {
    	MessageCommEnum enums = BaseEnums.Default.FAIL;

    	ApprovalAuthInfoVo approvalAuthInfoVo = approvalAuthService.getApprovalProcessInfo(taskCode, taskPk);

    	//철회가능상태 - 요청
    	//* 사용하지 않음으로 변경됨. 모든 업무 승인상태코드를 동일하게 사용으로 변경됨 (2021.02.04)
//    	String apprStatus = ApprovalUtil.replaceStatusCode(approvalAuthInfoVo.getApprStat());
    	String apprStatus = approvalAuthInfoVo.getApprStat();

    	if(StringUtil.isEmpty(apprStatus)
    			|| ApprovalStatus.NONE.getCode().equals(apprStatus)
    			) {

    		enums = ApprovalValidation.NONE_REQUEST;
    	}else if(ApprovalStatus.CANCEL.getCode().equals(apprStatus)){

    		enums = ApprovalValidation.ALREADY_CANCEL_REQUEST;
    	}else if(ApprovalStatus.DENIED.getCode().equals(apprStatus)){

    		enums = ApprovalValidation.ALREADY_DENIED;
    	}else if(ApprovalStatus.SUB_APPROVED.getCode().equals(apprStatus)
    			|| ApprovalStatus.APPROVED.getCode().equals(apprStatus)){

    		enums = ApprovalValidation.ALREADY_APPROVED;
    	}else if(ApprovalStatus.REQUEST.getCode().equals(apprStatus)){

    		return approvalAuthService.checkCancelable(approvalAuthInfoVo);
    	}else {
    		//상태이상.
    		enums = ApprovalValidation.NONE_REQUEST;
    	}

    	return ApiResult.result(enums);
    }

    /**
     * 승인절차 검증
     * 업무별 승인처리 정보 조회(승인담당자, 승인상태)
     *
     * @param taskCode, taskPk
     * @return ApprovalAuthInfoVo
     */
    public ApiResult<?> checkApprovalProcess(String taskCode, String taskPk) {

    	MessageCommEnum enums = BaseEnums.Default.FAIL;

    	ApprovalAuthInfoVo approvalAuthInfoVo = approvalAuthService.getApprovalProcessInfo(taskCode, taskPk);

    	//반려가능상태 - 요청, 1차승인
    	//승인가능상태 - 요청, 1차승인

    	//* 사용하지 않음으로 변경됨. 모든 업무 승인상태코드를 동일하게 사용으로 변경됨 (2021.02.04)
//    	String apprStatus = ApprovalUtil.replaceStatusCode(approvalAuthInfoVo.getApprStat());
    	String apprStatus = approvalAuthInfoVo.getApprStat();

		if (StringUtil.isEmpty(apprStatus)
				|| ApprovalStatus.NONE.getCode().equals(apprStatus)) {

			enums = ApprovalValidation.NONE_REQUEST;

		} else if (ApprovalStatus.CANCEL.getCode().equals(apprStatus)) {

			enums = ApprovalValidation.ALREADY_CANCEL_REQUEST;

		} else if (ApprovalStatus.DENIED.getCode().equals(apprStatus)) {

			enums = ApprovalValidation.ALREADY_DENIED;

		} else if (ApprovalStatus.APPROVED.getCode().equals(apprStatus)) {

			enums = ApprovalValidation.ALREADY_APPROVED;

		} else if (ApprovalStatus.REQUEST.getCode().equals(apprStatus)
				|| ApprovalStatus.SUB_APPROVED.getCode().equals(apprStatus)) {

			return approvalAuthService.checkApprovable(approvalAuthInfoVo);

		} else {
			//상태이상.
			enums = ApprovalValidation.NONE_REQUEST;
		}

    	return ApiResult.result(enums);
    }


	/**
	 * 상품 품목 승인 중복 승인 검증
	 * @param taskCode
	 * @param taskPk
	 * @return
	 */
    public ApiResult<?> checkDuplicateApproval(String taskCode, String taskPk) throws Exception {

    	//상품 등록, 거래처 품목 수정, 거래처 상품 수정 중복 수정 방지
		if((ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode().equals(taskCode))
		) {
			ApiResult<?> duplicateRequest = approvalAuthService.getDuplicateApprGoodsItemRequest(taskCode, taskPk);
			if (!ApiResult.success().getCode().equals(duplicateRequest.getCode())) {
				return duplicateRequest;
			}
		}

		//품목 가격, 상품 할인 중복 승인 처리 방지
		if((ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode().equals(taskCode)
			|| ApprovalAuthType.APPR_KIND_TP_GOODS_DISCOUNT.getCode().equals(taskCode))
		) {
			ApiResult<?> duplicatePrice = approvalAuthService.getDuplicateApprGoodsItemPriceDiscount(taskCode, taskPk);

			if (!ApiResult.success().getCode().equals(duplicatePrice.getCode())) {
				return duplicatePrice;
			}
		}

		return ApiResult.success();
	}

	/**
	 * 품목 가격 승인 요청, 상품 할인 승인 요청 중복 확인
	 * @param taskCode
	 * @param itemCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> checkDuplicatePriceApproval(String taskCode, String itemCode) throws Exception {
		//품목 가격, 상품 할인 중복 승인 처리 방지
		ApiResult<?> duplicatePrice = approvalAuthService.getDuplicateApprGoodsItemPriceDiscountByItemCode(taskCode, itemCode);

		if (!ApiResult.success().getCode().equals(duplicatePrice.getCode())) {
			return duplicatePrice;
		}

		return ApiResult.success();
	}


	/**
     * 승인폐기 검증
     * 승인요청자 승인폐기 가능한 상태 체크(승인요청자, 승인상태)
     *
     * @param taskCode, taskPk
     * @return ApprovalAuthInfoVo
     */
    public ApiResult<?> checkDisposable(String taskCode, String taskPk) {
    	return approvalAuthService.checkDisposable(taskCode, taskPk);
    }

    /**
     * 업무별 승인내역 이력 조회
     *
     * @param taskCode, taskPk
     * @return ApprovalStatusHistoryDto
     */
    public ApiResult<?> getApprovalHistory(String taskCode, String taskPk) {
    	return ApiResult.success(approvalAuthService.getApprovalHistory(taskCode, taskPk));
    }
}
package kr.co.pulmuone.v1.approval.auth.service;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskInfoDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface ApprovalAuthBiz {

	ApiResult<?> getApprovailProcessBizInfo(String taskCode, String taskPk);

	ApiResult<?> getApprovalAuthByTaskList(String masterApprovalKindType);

	ApiResult<?> getApprovalAuthByTaskInfo(String taskCode);

	ApiResult<?> putApprovalAuthByTaskInfo(ApprovalAuthByTaskInfoDto reqData) throws Exception;

	ApiResult<?> getApprovalAuthManagerHistoryByTask(String taskCode);

	ApiResult<?> checkCancelable(String taskCode, String taskPk);

	ApiResult<?> checkApprovalProcess(String taskCode, String taskPk);

	ApiResult<?> checkDisposable(String taskCode, String taskPk);

	ApiResult<?> getApprovalHistory(String taskCode, String taskPk);

	ApiResult<?> checkDuplicateApproval(String taskCode, String taskPk) throws Exception;

	ApiResult<?> checkDuplicatePriceApproval(String taskCode, String itemCode) throws Exception;

}

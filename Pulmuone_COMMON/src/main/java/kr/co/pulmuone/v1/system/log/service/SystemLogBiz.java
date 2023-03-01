package kr.co.pulmuone.v1.system.log.service;

import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.log.dto.*;
import kr.co.pulmuone.v1.system.log.dto.vo.DeviceLogVo;

import java.util.List;

public interface SystemLogBiz {

	/** 접속로그 목록 */
	ApiResult<?> getConnectLogList(GetConnectLogListRequestDto getConnectLogListRequestDto);

	/** 배치로그 목록  */
	ApiResult<?> getBatchLogList(GetBatchLogListRequestDto dto);

	ApiResult<?> getExcelDownloadLogList(ExcelDownLogRequestDto excelDownLogRequestDto);

	void addExcelDownloadAsync(ExcelDownloadAsyncRequestDto requestDto);

	void putExcelDownloadAsyncSetUse(Long stExcelDownloadAsyncId);

	void putExcelDownloadAsyncSetError(Long stExcelDownloadAsyncId);

	ExcelDownloadAsyncResponseDto getExcelDownloadAsyncUseYn(Long stExcelDownloadAsyncId);

	/** 메뉴사용이력 목록  */
	ApiResult<?> getMenuOperLogList(MenuOperLogRequestDto dto);

	/** 개인정보 처리 이력  목록 */
	ApiResult<?> getPrivacyMenuOperLogList(PrivacyMenuOperLogRequestDto dto);

	Boolean addDeviceLogUserJoin(String urPcidCd, Long urUserId);

	Boolean addDeviceLogLoginFail(String urPcidCd, Long urUserId);

	Boolean addIllegalLogStolenLostCard(String urPcidCd, Long urUserId);

	Boolean addIllegalLogTransactionNotCard(String urPcidCd, Long urUserId);

	void addIllegalLogList(List<IllegalLogRequestDto> dtoList);

	List<DeviceLogVo> getDeviceLogDetect(DetectDeviceLogRequestDto dto);

}
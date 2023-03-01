package kr.co.pulmuone.v1.system.log.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.log.dto.*;
import kr.co.pulmuone.v1.system.log.dto.vo.DeviceLogVo;
import kr.co.pulmuone.v1.system.log.dto.vo.ExcelDownloadLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.MenuOperLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.PrivacyMenuOperLogResultVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SystemLogBizImpl implements SystemLogBiz {

	private final SystemLogService service;

	/**
	 * @Desc접속로그 목록
	 * @param getConnectLogListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getConnectLogList(GetConnectLogListRequestDto getConnectLogListRequestDto) {
		return ApiResult.success(service.getConnectLogList(getConnectLogListRequestDto));
	}


	/**
	 * @Desc 배치로그 목록
	 * @param dto
	 * @return
	 */
	@Override
	public ApiResult<?> getBatchLogList(GetBatchLogListRequestDto dto) {
		return ApiResult.success(service.getBatchLogList(dto));
	}


	/**
	 * @Desc 엑셀다운로드 로그 리스트 조회
	 * @param excelDownLogRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getExcelDownloadLogList(ExcelDownLogRequestDto excelDownLogRequestDto) {
		ExcelDownloadLogResponseDto excelDownloadLogResponseDto = new ExcelDownloadLogResponseDto();
		Page<ExcelDownloadLogResultVo> excelDownloadLogList = service.getExcelDownloadLogList(excelDownLogRequestDto);

		excelDownloadLogResponseDto.setTotal(excelDownloadLogList.getTotal());
		excelDownloadLogResponseDto.setRows(excelDownloadLogList.getResult());

	    return ApiResult.success(excelDownloadLogResponseDto);

	}

	@Override
	public void addExcelDownloadAsync(ExcelDownloadAsyncRequestDto requestDto) {
		service.addExcelDownloadAsync(requestDto);
	}

	@Override
	public void putExcelDownloadAsyncSetUse(Long stExcelDownloadAsyncId) {
		service.putExcelDownloadAsyncSetUse(stExcelDownloadAsyncId);
	}

	@Override
	public void putExcelDownloadAsyncSetError(Long stExcelDownloadAsyncId) {
		service.putExcelDownloadAsyncSetError(stExcelDownloadAsyncId);
	}

	@Override
	public ExcelDownloadAsyncResponseDto getExcelDownloadAsyncUseYn(Long stExcelDownloadAsyncId) {
		return ExcelDownloadAsyncResponseDto.builder()
				.rows(service.getExcelDownloadAsyncUseYn(stExcelDownloadAsyncId))
				.build();
	}

	/**
	 * @Desc 메뉴사용이력 리스트 조회
	 * @param dto
	 * @return
	 */
	@Override
	public ApiResult<?> getMenuOperLogList(MenuOperLogRequestDto dto) {
		MenuOperLogResponseDto menuOperLogResponseDto = new MenuOperLogResponseDto();
		Page<MenuOperLogResultVo> menuOperLogList = service.getMenuOperLogList(dto);

		menuOperLogResponseDto.setTotal(menuOperLogList.getTotal());
		menuOperLogResponseDto.setRows(menuOperLogList.getResult());

	    return ApiResult.success(menuOperLogResponseDto);
	}


	/**
	 * @Desc 개인정보 처리 이력  목록
	 * @param dto
	 * @return
	 */
	@Override
	public ApiResult<?> getPrivacyMenuOperLogList(PrivacyMenuOperLogRequestDto dto) {
		PrivacyMenuOperLogResponseDto privacyMenuOperLogResponseDto = new PrivacyMenuOperLogResponseDto();
		Page<PrivacyMenuOperLogResultVo> privacyMenuOperLogVoList = service.getPrivacyMenuOperLogList(dto);

		privacyMenuOperLogResponseDto.setTotal(privacyMenuOperLogVoList.getTotal());
		privacyMenuOperLogResponseDto.setRows(privacyMenuOperLogVoList.getResult());

	    return ApiResult.success(privacyMenuOperLogResponseDto);

	}

	@Override
	public Boolean addDeviceLogUserJoin(String urPcidCd, Long urUserId) {
		return service.addDeviceLogUserJoin(urPcidCd, urUserId);
	}

	@Override
	public Boolean addDeviceLogLoginFail(String urPcidCd, Long urUserId) {
		return service.addDeviceLogLoginFail(urPcidCd, urUserId);
	}

	@Override
	public Boolean addIllegalLogStolenLostCard(String urPcidCd, Long urUserId) {
		return service.addIllegalLogStolenLostCard(urPcidCd, urUserId);
	}

	@Override
	public Boolean addIllegalLogTransactionNotCard(String urPcidCd, Long urUserId) {
		return service.addIllegalLogTransactionNotCard(urPcidCd, urUserId);
	}

	@Override
	public void addIllegalLogList(List<IllegalLogRequestDto> dtoList) {
		service.addIllegalLogList(dtoList);
	}

	@Override
	public List<DeviceLogVo> getDeviceLogDetect(DetectDeviceLogRequestDto dto) {
		return service.getDeviceLogDetect(dto);
	}

}


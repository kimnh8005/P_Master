package kr.co.pulmuone.v1.promotion.pointhistory.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointDetailHistoryRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListRequestDto;

public interface PointHistoryBiz {

	/**
	 * 적립금 내역 리스트 조회
	 * @param pointHistoryListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> getPointNormalHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;

	/**
	 * 올가 적립금 내역 리스트조회
	 * @param pointHistoryListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> getPointHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;


	/**
	 * 적립금 상세 내역 조회
	 * @param pointDetailHistoryRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> getPointDetailHistory(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception;



	/**
	 * 로그인 정보 조회
	 * @param pointDetailHistoryRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> getLoginInfo(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception;


	public ApiResult<?> getTotalPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;

	ExcelDownloadDto getPointHistoryListExportExcel(PointHistoryListRequestDto pointHistoryListRequestDto,String legalType) throws Exception;
}

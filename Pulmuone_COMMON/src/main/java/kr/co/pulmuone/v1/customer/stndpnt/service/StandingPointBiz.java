package kr.co.pulmuone.v1.customer.stndpnt.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointRequestDto;

public interface StandingPointBiz {

	// 상품입점상담 관리 목록조회
	ApiResult<?> getStandingPointList(StandingPointRequestDto standingPointRequestDto) throws Exception;

	//상품입점상담 관리 목록조회 엑셀 다운로드
	ExcelDownloadDto getStandingPointExportExcel(StandingPointRequestDto standingPointRequestDto);

	//상품입점상담 관리 상세조회
	ApiResult<?> getDetailStandingPoint(StandingPointRequestDto standingPointRequestDto) throws Exception;

	//상품입점 승인상태 변경
	ApiResult<?> putStandingPointStatus(StandingPointRequestDto standingPointRequestDto) throws Exception;

	void addStandingPointQna(StandingPointMallRequestDto dto) throws Exception;

}
